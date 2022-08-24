package LexicalAnalyzer;

public class LexicalException extends Throwable {

    private String lexema;
    private int nroLinea;
    private int nroColumna;
    private String lexicalErrorLine;
    private String errorType;

    public LexicalException(String lexemeWithError, int nroLinea, int nroColumna, String errorDetail, String lexicalErrorLine) {
        this.lexema = lexemeWithError;
        this.nroLinea = nroLinea;
        this.nroColumna = nroColumna;
        this.errorType = errorDetail;
        this.lexicalErrorLine = lexicalErrorLine;
    }

    public String getMessage() {
        return this.formarString();
    }

    public String formarString() {
        return "Error Léxico en línea " +this.nroLinea+ ", columna " +this.nroColumna+ ": " /*+this.lexema */ +this.errorType+ "\n"
                + this.generarDetalleDeError() + "\n[Error:"+this.lexema+"|"+this.nroLinea + "]\n\n";
    }

    public String generarDetalleDeError() {
        String errorAMostrar = "Detalle: ";
        int longitudMensajeDeInicio = errorAMostrar.length();
        errorAMostrar += this.lexicalErrorLine;
        String punteroQueSeñalaError = "";
        for (int totalDesplazamientoDePuntero = 1; totalDesplazamientoDePuntero < (this.nroColumna + longitudMensajeDeInicio); totalDesplazamientoDePuntero++)
            punteroQueSeñalaError+= " ";
        punteroQueSeñalaError += "^";
        return errorAMostrar + "\n" + punteroQueSeñalaError;
    }


}
