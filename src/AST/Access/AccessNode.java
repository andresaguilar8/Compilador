package AST.Access;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;

public abstract class AccessNode extends ExpressionNode {

//    protected Token token;

    protected Encadenado encadenado;

    public AccessNode(Token token) {
        super(token);
    }

    public abstract void setEncadenado(Encadenado encadenado);
}
