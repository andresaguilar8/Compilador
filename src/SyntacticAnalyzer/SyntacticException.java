package SyntacticAnalyzer;

import LexicalAnalyzer.Token;

public class SyntacticException extends Exception {

    private Token currentToken;
    private String tokenId;

    public SyntacticException(Token currentToken, String tokenId) {
        this.currentToken = currentToken;
        this.tokenId = tokenId;
    }

    public String getMessage() {
        return "Error Sintactico en linea "
                + this.currentToken.getLineNumber()
                + ": se esperaba "
                + this.tokenId
                + " y se encontro "
                + this.currentToken.getTokenId()
                + this.generateStringError();
    }

    private String generateStringError() {
        return "\n\n[Error:" +
                this.currentToken.getLexeme()
                + "|"
                + this.currentToken.getLineNumber()
                + "]\n\n";
    }

}
