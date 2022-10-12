package AST.Access;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;

public abstract class AccessNode extends ExpressionNode {

    protected Encadenado encadenado;
    protected boolean isAssignable;

    public AccessNode(Token token) {
        super(token);
    }

    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    public Encadenado getEncadenado() {
        return this.encadenado;
    }

    public boolean isAssignable() {
        return this.isAssignable;
    }

    public void setIsNotAssignable() {
        this.isAssignable = false;
    }
}
