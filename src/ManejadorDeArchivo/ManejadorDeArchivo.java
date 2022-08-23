package ManejadorDeArchivo;

import java.io.*;

public class ManejadorDeArchivo {

    private File file;
    private int nroLineaActual = 1;
    private int nroColumnaActual = 0;
    private int caracterActual;
    private String lineaConError;
    BufferedReader reader;

    public ManejadorDeArchivo(File file) throws IOException {
        this.file = file;
        reader = new BufferedReader(new FileReader(file));
        this.caracterActual = reader.read();
    }

    public int leerProximoCaracter() throws IOException {
        if (this.caracterActual == '\r' || this.caracterActual == '\n') {
            this.nroLineaActual += 1;
            this.nroColumnaActual = 0;
//            this.lineaConError = this.reader.readLine();
        }

        this.nroColumnaActual += 1;
        this.caracterActual =  this.reader.read();

        if (this.caracterActual == '\r')
            this.caracterActual = this.reader.read();

        return this.caracterActual;
    }

    public int obtenerNumeroLinea() {
        return this.nroLineaActual;
    }

    public String obtenerLineaConError() throws IOException {
        reader = new BufferedReader(new FileReader(file));

        int nroLinea = 1;
//        this.reader.close();

        System.out.println("linea act"+this.nroLineaActual);
        while (nroLinea < this.nroLineaActual) {
            reader.readLine();
            nroLinea += 1;
        }

        this.lineaConError = reader.readLine();
//        return "";
        return this.lineaConError;
    }
    public int obtenerNumeroColumna() {
        return this.nroColumnaActual;
    }
    public int obtenerCaracterActual() {
        return this.caracterActual;
    }

}
