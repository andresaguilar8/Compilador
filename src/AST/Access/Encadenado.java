package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public abstract class Encadenado {

    protected Token token;
    protected Encadenado encadenado;
    protected boolean isAssignable;

    public Encadenado(Token token) {
        this.token = token;
    }

    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    public abstract Type check(Type type) throws SemanticExceptionSimple;

    public abstract void printExpression();

    public abstract boolean isAssignable();

    public void setIsNotAssignable() {
        this.isAssignable = false;
    }

    public Encadenado getEncadenado() {
        return this.encadenado;
    }
}
