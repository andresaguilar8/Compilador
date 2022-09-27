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

    public String getClassName() {
        return this.tokenType.getLexeme();
    }

    public abstract boolean isPrimitive();

    public Token getToken() {
        return this.tokenType;
    }

}
