package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.Method;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.SymbolTable;
import SemanticAnalyzer.Type;
import Traductor.Traductor;

import java.io.IOException;

public class LocalVarDeclarationNode extends SentenceNode {

    private ExpressionNode expressionNode;
    private Type localVarType;
    private Token operatorToken;
    private int offset;

    public LocalVarDeclarationNode(Token nodeToken, ExpressionNode expressionNode, Token operatorToken) {
        super(nodeToken);
        this.expressionNode = expressionNode;
        this.operatorToken = operatorToken;
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        Method currentMethod = SymbolTable.getInstance().getCurrentMethod();
        if (!SymbolTable.getInstance().isMethodParameter(this.token.getLexeme(), currentMethod)) {
            Type localVarType = this.expressionNode.check();
            if (localVarType.getClassName().equals("null") || localVarType.getClassName().equals("void"))
                throw new SemanticExceptionSimple(this.operatorToken, "no se puede inferir el tipo de la variable");
            this.setType(localVarType);
            SymbolTable.getInstance().getCurrentBlock().insertLocalVar(this);
        }
        else
            throw new SemanticExceptionSimple(this.token, "el nombre para la variable ya esta utilizado en un parametro");
    }

    @Override
    protected void generateCode() throws IOException {
        //todo chequear
        Traductor.getInstance().gen("RMEM 1 ; Se reserva espacio para una variable local");
        this.expressionNode.generateCode();
        Traductor.getInstance().gen("STORE " + this.offset + " ; Se almacena el valor de la expresion en la variable local " + this.token.getLexeme());
    }

    public Type getLocalVarType() {
        return this.localVarType;
    }

    public void setType(Type localVarType) {
        this.localVarType = localVarType;
    }

    public String getVarName() {
        return this.token.getLexeme();
    }

    public Token getVarToken() {
        return this.token;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return this.offset;
    }
}
