package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.SymbolTable;
import SemanticAnalyzer.Type;

public class LocalVarNode extends SentenceNode {

    private ExpressionNode expressionNode;
    private Type localVarType;

    public LocalVarNode(Token nodeToken, ExpressionNode expressionNode) {
        super(nodeToken);
        this.expressionNode = expressionNode;
    }

    @Override
    public void printSentence() {
        System.out.print("variable: " + this.token.getLexeme() + " con expresion: ");
        this.expressionNode.printExpression();
        System.out.println();
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        Type localVarType = this.expressionNode.check();
        System.out.println(localVarType.getClassName());
        this.setType(localVarType);
        SymbolTable.getInstance().getCurrentBlock().insertLocalVar(this);
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
}
