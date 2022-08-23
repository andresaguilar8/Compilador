package AnalizadorLexico;

public class ExcepcionLexica extends Throwable {

    private String lexema;
    private int nroLinea;

    public ExcepcionLexica(String lexema, int nroLinea) {
        this.lexema = lexema;
        this.nroLinea = nroLinea;
    }

    public String getMessage() {

        return "Error Léxico en linea "+this.nroLinea + ": " +this.lexema+ " no es un símbolo válido \n\n[Error:"+this.lexema+"|"+this.nroLinea+"]";
    }
}
