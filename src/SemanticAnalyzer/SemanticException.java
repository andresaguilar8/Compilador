package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class SemanticException extends Exception {

    private Token errorToken;
    private String errorMessage;

    public SemanticException(Token errorToken, String errorMessage) {
        this.errorToken = errorToken;
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return "Error Semantico en linea "
                + this.errorToken.getLineNumber()
                + ": "
                + this.errorMessage
                + this.generateStringError();
    }

    private String generateStringError() {
        return "\n\n[Error:" +
                this.errorToken.getLexeme()
                + "|"
                + this.errorToken.getLineNumber()
                + "]\n\n";
    }

}
