package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.PrimitiveType;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public class EmptyExpression extends ExpressionNode {

    public EmptyExpression(Token token) {
        super(token);
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        return new PrimitiveType(new Token("pr_void", "void", this.token.getLineNumber()));
    }

    @Override
    public void printExpression() {
        System.out.println("ahora se");
    }

    @Override
    public void setType() {

    }
}
