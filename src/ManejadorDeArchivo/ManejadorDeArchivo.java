package ManejadorDeArchivo;

import java.io.*;

public class ManejadorDeArchivo {

    private File file;
    private int nroLineaActual = 1;
    private char caracterActual;
    BufferedReader reader;

    public ManejadorDeArchivo(File file) throws IOException {
        this.file = file;
        reader = new BufferedReader(new FileReader(file));
        this.caracterActual = ((char) reader.read());
    }

    public char proximoCaracter() throws IOException {
        if (this.caracterActual == '\r') {
            this.nroLineaActual += 1;
        }


        this.caracterActual = ((char) this.reader.read());
//        if (caracterActual == EOF)

//        if (caracterActual == '\r') {
//            System.out.print("fin de archivo");
//        }

        return this.caracterActual;
    }

    public int obtenerNumeroLinea() {
        return this.nroLineaActual;
    }

    public char obtenerCaracterActual() {
        return this.caracterActual;
    }

    public boolean endOfFile() {
        return (Object)this.caracterActual == null;
    }

    public void comentarioSimple() throws IOException {
        this.reader.readLine();
        this.nroLineaActual += 1;
    }

    public void comentarioMultiLinea() throws IOException {
        boolean encontreCierreDeComentario = false;
        while (!encontreCierreDeComentario) {
            if (this.proximoCaracter() == '*')
                if (this.proximoCaracter() == '/')
                    encontreCierreDeComentario = true;
        }
    }
}
