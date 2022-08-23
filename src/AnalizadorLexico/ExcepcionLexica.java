package AnalizadorLexico;

public class ExcepcionLexica extends Throwable {

    private String lexema;
    private int nroLinea;
    private int nroColumna;
    private String lineaCompletaConError;

    public ExcepcionLexica(String lexema, int nroLinea, int nroColumna, String lineaCompletaConError) {
        this.lexema = lexema;
        this.nroLinea = nroLinea;
        this.nroColumna = nroColumna;
        this.lineaCompletaConError = lineaCompletaConError;
    }

    public String getMessage() {
        String detalle = "Detalle:";
        int x = detalle.length();
        System.out.println(x);
        detalle += this.lineaCompletaConError;
        String cositoAMostrar = "";
        for (int i = 1; i <= (this.nroColumna + x); i++)
            cositoAMostrar+= " ";
        cositoAMostrar += "^";
        return "Error Léxico en linea "+this.nroLinea + " y en columna: "
                +this.nroColumna + ": " +this.lexema
                + " no es un símbolo válido \n\n[Error:"+this.lexema+"|"+this.nroLinea
                +"]\n\n"+detalle+"\n"+cositoAMostrar;
    }
}
