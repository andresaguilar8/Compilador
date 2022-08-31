package LexicalAnalyzer;

public class LexicalException extends Exception {

    private String lexemeWithError;
    private int lineNumber;
    private int columnNumber;
    private String lexicalErrorLine;
    private String errorType;

    public LexicalException(String lexemeWithError, int lineNumber, int columnNumber, String errorDetail, String lexicalErrorLine) {
        this.lexemeWithError = lexemeWithError;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.errorType = errorDetail;
        this.lexicalErrorLine = lexicalErrorLine;
    }

    public String getMessage() {
        return this.generateStringError();
    }

    public String generateStringError() {
        return "Error Léxico en línea " +this.lineNumber + ", columna " +this.columnNumber + ": " /*+this.lexema */ +this.errorType+ "\n"
                + this.generateErrorDetail() + "\n[Error:"+this.lexemeWithError +"|"+this.lineNumber + "]\n\n";
    }

    public String generateErrorDetail() {
        String errorToShow = "Detalle: ";
        int initStringLength = errorToShow.length();
        errorToShow += this.lexicalErrorLine;
        String errorPointer = "";
        for (int totalPointerDisplacement = 1; totalPointerDisplacement < (this.columnNumber + initStringLength); totalPointerDisplacement++)
            errorPointer+= " ";
        errorPointer += "^";
        return errorToShow + "\n" + errorPointer;
    }

}
