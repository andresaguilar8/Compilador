package LexicalAnalyzer;

public class Token {

    private String token_id;
    private String lexeme;
    private int lineNumber;

    public Token(String token_id, String lexeme, int lineNumber) {
        this.token_id = token_id;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public String getTokenId() {
        return this.token_id;
    }

    public String toString() {
        return "("+this.token_id+","+this.lexeme +","+this.lineNumber +")";
    }
}
