package ManejadorDeArchivo;

import java.io.*;

public class ManejadorDeArchivo {

    private File file;
    private int nroLineaActual = 1;
    private int caracterActual;
    BufferedReader reader;

    public ManejadorDeArchivo(File file) throws IOException {
        this.file = file;
        reader = new BufferedReader(new FileReader(file));
        this.caracterActual = reader.read();
    }

    public int leerProximoCaracter() throws IOException {
        if (this.caracterActual == '\r' || this.caracterActual == '\n')
            this.nroLineaActual += 1;

        this.caracterActual =  this.reader.read();

        if (this.caracterActual == '\r')
            this.caracterActual = this.reader.read();





        return this.caracterActual;
    }

    public int obtenerNumeroLinea() {
        return this.nroLineaActual;
    }

    public int obtenerCaracterActual() {
        return this.caracterActual;
    }

    public boolean endOfFile() {
        return this.caracterActual != -1;
    }

    public void comentarioSimple() throws IOException {
        this.reader.readLine();
        this.nroLineaActual += 1;
    }

    public void comentarioMultiLinea() throws IOException {
        boolean encontreCierreDeComentario = false;
        while (!encontreCierreDeComentario) {
            if (this.leerProximoCaracter() == '*')
                if (this.leerProximoCaracter() == '/')
                    encontreCierreDeComentario = true;
        }
    }
}
