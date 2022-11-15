package AST.Sentence;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Method;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.SymbolTable;
import InstructionGenerator.InstructionGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class BlockNode extends SentenceNode {

    private ArrayList<SentenceNode> sentencesList;
    private Hashtable<String, LocalVarDeclarationNode> localVarTable;
    private BlockNode ancestorBlock;
    private int availableLocalVarOffset;
    private int totalVars;

    public BlockNode(Token token, BlockNode ancestorBlock) {
        super(token);
        this.sentencesList = new ArrayList<>();
        this.localVarTable = new Hashtable<>();
        this.ancestorBlock = ancestorBlock;
        this.availableLocalVarOffset = 1;
        this.totalVars = 0;
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
            if (!this.localVarTable.containsKey(localVarNode.getVarName())) {
                this.localVarTable.put(localVarNode.getVarName(), localVarNode);
//                this.totalVars += 1;
            }
            else
                throw new SemanticExceptionSimple(localVarNode.getVarToken(), "Ya existe una variable local con nombre " + localVarNode.getVarName() + " dentro del alcance");
        }
        else
            throw new SemanticExceptionSimple(localVarNode.getVarToken(), "El nombre " + localVarNode.getVarName() + " ya esta utilizado en un parametro dentro del metodo " + "\"" + currentMethod.getMethodName() + "\"");

        this.setLocalVarOffset(localVarNode);
    }

    public void increaseTotalBlockVars() {
        this.totalVars += 1;
    }

    private int getAncestorAvailableOffset() {
        BlockNode ancBlock = this.ancestorBlock;
        while (ancBlock != null) {
            if (ancBlock.getAvailableLocalVarOffset() != 1)
                return ancBlock.getAvailableLocalVarOffset();
            ancBlock = ancBlock.getAncestorBlock();
        }
        return 1;
    }

    private void setLocalVarOffset(LocalVarDeclarationNode localVarNode) {
        //si tiene bloque padre y es la primer variable del bloque actual que voy a insertar
        if (this.ancestorBlock != null && this.availableLocalVarOffset == 1)
                this.availableLocalVarOffset = this.getAncestorAvailableOffset();

        localVarNode.setVarOffset(this.availableLocalVarOffset - 1);
        this.availableLocalVarOffset -= 1;
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

    public BlockNode getAncestorBlock() {
        return this.ancestorBlock;
    }

    public void generateCode() throws IOException {
        for (SentenceNode sentenceNode: this.sentencesList)
            sentenceNode.generateCode();
        this.freeMem();
    }

    private void freeMem() throws IOException {
        InstructionGenerator.getInstance().generateInstruction("FMEM " + this.totalVars);
    }

    public int getTotalVars() {
        int total;
        if (this.ancestorBlock != null) {
            total = this.ancestorBlock.getTotalVars() + this.totalVars;
        }
        else
            total = this.totalVars;
        return total;
    }
}
