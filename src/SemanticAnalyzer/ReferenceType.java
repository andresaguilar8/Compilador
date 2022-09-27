package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class ReferenceType extends Type {

    public ReferenceType(Token tokenType) {
        super(tokenType);
    }

    public boolean isPrimitive() {
        return false;
    }

}
