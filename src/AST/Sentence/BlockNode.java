package AST.Sentence;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Method;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.SymbolTable;
import Traductor.Traductor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class BlockNode extends SentenceNode {

    private ArrayList<SentenceNode> sentencesList;
    private Hashtable<String, LocalVarDeclarationNode> localVarTable;
    private BlockNode ancestorBlock;
    private int availableLocalVarOffset;

    public BlockNode(Token token, BlockNode ancestorBlock) {
        super(token);
        this.sentencesList = new ArrayList<>();
        this.localVarTable = new Hashtable<>();
        this.ancestorBlock = ancestorBlock;
        this.availableLocalVarOffset = 1;
    }

    public Hashtable<String, LocalVarDeclarationNode> getLocalVarTable() {
        return this.localVarTable;
    }

    private int getAvailableLocalVarOffset() {
        return this.availableLocalVarOffset;
    }

    public void insertLocalVar(LocalVarDeclarationNode localVarNode) throws SemanticExceptionSimple {
        if (this.ancestorBlock != null) {
            for (LocalVarDeclarationNode localVarInAncestorBlock: ancestorBlock.getLocalVarTable().values())
                this.localVarTable.put(localVarInAncestorBlock.getVarName(), localVarInAncestorBlock);
        }
        Method currentMethod = SymbolTable.getInstance().getCurrentMethod();
        if (!currentMethod.getParametersList().contains(localVarNode.getVarName())) {
            if (!this.localVarTable.containsKey(localVarNode.getVarName()))
                this.localVarTable.put(localVarNode.getVarName(), localVarNode);
            else
                throw new SemanticExceptionSimple(localVarNode.getVarToken(), "Ya existe una variable local con nombre " + localVarNode.getVarName() + " dentro del alcance");
        }
        else
            throw new SemanticExceptionSimple(localVarNode.getVarToken(), "El nombre " + localVarNode.getVarName() + " ya esta utilizado en un parametro dentro del metodo " + "\"" + currentMethod.getMethodName() + "\"");

        this.setLocalVarOffset(localVarNode);
        System.out.println("localvar: " + localVarNode.getVarName() + " offset: " + localVarNode.getOffset());
    }

    private void setLocalVarOffset(LocalVarDeclarationNode localVarNode) {
        if (this.ancestorBlock != null) {
            localVarNode.setOffset(this.ancestorBlock.getAvailableLocalVarOffset() - 1);
            this.availableLocalVarOffset = this.ancestorBlock.getAvailableLocalVarOffset() - 1;
        }
        else {
            localVarNode.setOffset(this.availableLocalVarOffset - 1);
            this.availableLocalVarOffset -= 1;
        }
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        SymbolTable.getInstance().setCurrentBlock(this);
        for (SentenceNode sentenceNode: this.sentencesList)
            sentenceNode.check();
        if (this.getAncestorBlock() != null)
            SymbolTable.getInstance().setCurrentBlock(this.ancestorBlock);
    }

    public void addSentence(SentenceNode sentenceNode) {
        this.sentencesList.add(sentenceNode);
    }

    public ArrayList<SentenceNode> getSentencesList() {
        return this.sentencesList;
    }

    public BlockNode getAncestorBlock() {
        return this.ancestorBlock;
    }

    public void generateCode() throws IOException {
        //todo hay que liberar memoria de las var locales de un bloque
        for (SentenceNode sentenceNode: this.sentencesList)
            sentenceNode.generateCode();
        this.freeMem();
    }

    private void freeMem() throws IOException {
        Traductor.getInstance().gen("FMEM " + ((this.availableLocalVarOffset * -1) + 1));
    }
}
