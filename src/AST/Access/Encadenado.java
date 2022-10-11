package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public abstract class Encadenado extends AccessNode {

//    protected Encadenado encadenado;

    public Encadenado(Token token) {
        super(token);
    }

    @Override
    public abstract void setEncadenado(Encadenado encadenado);

    @Override
    public Type check() throws SemanticExceptionSimple {
        return null;
    }

    @Override
    public abstract void printExpression();

    @Override
    public void setType() {

    }
}
