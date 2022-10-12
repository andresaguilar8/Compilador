package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public abstract class Encadenado extends AccessNode {

    public Encadenado(Token token) {
        super(token);
    }

    public abstract Type check(Type type) throws SemanticExceptionSimple;

}
