package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class SemanticError {

    private Token errorToken;
    private String errorMessage;

    public SemanticError(Token errorToken, String error) {
        this.errorToken = errorToken;
        this.errorMessage = error;
    }

    public Token getErrorToken() {
        return this.errorToken;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}