package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public abstract class Type {

    private Token tokenType;

    public Type(Token tokenType) {
        this.tokenType = tokenType;
    }

}
