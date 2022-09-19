package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class PrimitiveType extends Type {

    public PrimitiveType(Token tokenType) {
        super(tokenType);
    }

    public boolean isPrimitive() {
        return true;
    }
}
