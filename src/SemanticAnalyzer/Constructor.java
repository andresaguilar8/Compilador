package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class Constructor {

    Token constructorToken;

    public Constructor(Token constructorToken) {
        this.constructorToken = constructorToken;
    }

    public Token getConstructorToken() {
        return this.constructorToken;
    }
}
