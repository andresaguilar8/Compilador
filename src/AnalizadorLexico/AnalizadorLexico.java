package AnalizadorLexico;

import ManejadorDeArchivo.ManejadorDeArchivo;

import java.io.IOException;
import java.util.Map;

public class AnalizadorLexico {

    private char caracterActual;
    private String lexema;
    private int nro_linea;
    private Map<String, String> mapeoDePalabrasClave;
    private ManejadorDeArchivo manejadorDeArchivo;

    public AnalizadorLexico(ManejadorDeArchivo manejadorDeArchivo, Map<String, String> mapeoDePalabrasClave) {
        this.manejadorDeArchivo = manejadorDeArchivo;
        this.caracterActual = manejadorDeArchivo.obtenerCaracterActual();
        this.mapeoDePalabrasClave = mapeoDePalabrasClave;
    }

    private void actualizarLexema() {
        this.lexema = this.lexema + this.caracterActual;
    }

    private void actualizarCaracterActual () throws IOException {
        this.manejadorDeArchivo.proximoCaracter();
        this.caracterActual = manejadorDeArchivo.obtenerCaracterActual();
    }

    public Token proximoToken() throws IOException, ExcepcionLexica {
        this.lexema = "";
        return estado_0();
    }

    private Token estado_0() throws IOException, ExcepcionLexica {
        if (Character.isWhitespace(this.caracterActual)) {
            this.actualizarCaracterActual();
            return this.estado_0();
        }
        else
            if (Character.isDigit(this.caracterActual)) {
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.estado1();
            }
            else
                if (Character.isLetter(this.caracterActual)){
                    this.actualizarLexema();
                    this.actualizarCaracterActual();
                    return this.estado2();
                }
                else
                    if (this.caracterActual == '>') {
                        this.actualizarLexema();
                        this.actualizarCaracterActual();
                        return this.estado3();
                    }
                    else
                        if (this.caracterActual == '/') {
                            this.actualizarLexema();
                            this.actualizarCaracterActual();
                            return this.estado14();
                        }
                        else {
                            this.actualizarLexema();
                            throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
                        }
    }

    private Token estado1() throws IOException {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado1();
        }
        else
            return new Token("entero", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado2() throws IOException {
        if (Character.isLetter(this.caracterActual) || Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado2();
        }
        else
            if (this.mapeoDePalabrasClave.containsKey(this.lexema)) {
                return new Token(this.mapeoDePalabrasClave.get(this.lexema), this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
            }
            else
                return new Token("identificador", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado3() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado4();
        }
        else
            return new Token("mayor", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado4() {
            return new Token("mayor_igual", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado14() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '/') {
            this.manejadorDeArchivo.comentarioSimple();
            this.actualizarCaracterActual();
            return this.proximoToken();
        }
        else
            if (this.caracterActual == '*') {
                this.manejadorDeArchivo.comentarioMultiLinea();
                this.actualizarCaracterActual();
                return this.proximoToken();
            }
        else
            return new Token("division", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    public boolean quedanTokens() {
        return !this.manejadorDeArchivo.endOfFile();
    }
}
