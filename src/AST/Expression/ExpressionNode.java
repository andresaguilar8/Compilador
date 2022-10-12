package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public abstract class ExpressionNode {

    protected Token token;
    protected Type expressionType;

    public ExpressionNode(Token token){
        this.token = token;
    }

    public abstract Type check() throws SemanticExceptionSimple;

    public abstract void printExpression();

    public Token getToken() {
        return this.token;
    }

    public abstract void setType();

}
