package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public abstract class Type {

    private Token tokenType;

    public Type(Token tokenType) {
        this.tokenType = tokenType;
    }

    public String toString() {
        return this.tokenType.getLexeme();
    }

    public String getTypeName() {
        return this.tokenType.getLexeme();
    }

    public boolean isPrimitive() {
        return this.getTypeName().equals("int") || this.getTypeName().equals("char") || this.getTypeName().equals("boolean");
    }
}
