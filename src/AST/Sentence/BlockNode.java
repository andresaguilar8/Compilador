package AST.Sentence;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.SymbolTable;

import java.util.ArrayList;
import java.util.Hashtable;

public class BlockNode extends SentenceNode {

    private ArrayList<SentenceNode> sentencesList;
    private Hashtable<String, LocalVarNode> localVarTable;
    private BlockNode ancestorBlock;

    public BlockNode(Token token, BlockNode ancestorBlock) {
        super(token);
        this.sentencesList = new ArrayList<>();
        this.localVarTable = new Hashtable<>();
        this.ancestorBlock = ancestorBlock;
    }

    public Hashtable<String, LocalVarNode> getLocalVarTable() {
        return this.localVarTable;
    }

    public void insertLocalVar(LocalVarNode localVarNode) throws SemanticExceptionSimple {
        //todo todos los chequeos son dps de la consolidacion,
        //al momento que yo inserrto un varLocal, en el sintactico es una sentencia,
        //dps de chequear esa sentencia, ahi si es una varLocal
        //todo aca chequear que no exista
        //todo y chequear que no sea un parametro: le pido el metodo actual a la tabla de simbolos
//        if (!this.parametersTable.containsKey(localVarNode.getVarName())) {
            if (!this.localVarTable.containsKey(localVarNode.getVarName()))
                this.localVarTable.put(localVarNode.getVarName(), localVarNode);
            else
                throw new SemanticExceptionSimple(localVarNode.getVarToken(), "Ya existe una variable local con nombre " + localVarNode.getVarName() + " dentro del metodo " + "\"" + this.methodToken.getLexeme() + "\"");
//        }
//        else
//            throw new SemanticExceptionSimple(localVarNode.getVarToken(), "El nombre " + localVarNode.getVarName() + " ya esta utilizado en un parametro dentro del metodo " + "\"" + this.methodToken.getLexeme() + "\"");
    }

    @Override
    public void printSentence() {
        System.out.println("bloque: sentencias dentro del bloque: "+this.sentencesList.size());
        for (SentenceNode sentenceNode: this.sentencesList)
            System.out.println(sentenceNode);
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        SymbolTable.getInstance().setCurrentBlock(this);
        for (SentenceNode sentenceNode: this.sentencesList) {
            System.out.print("chequeo sentencia: ");
            sentenceNode.printSentence();
            sentenceNode.check();
        }
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
}
