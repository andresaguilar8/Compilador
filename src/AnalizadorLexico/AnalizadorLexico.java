package AnalizadorLexico;

import ManejadorDeArchivo.ManejadorDeArchivo;

import java.io.IOException;
import java.util.Map;

public class AnalizadorLexico {

    private int caracterActual;
    private String lexema;
//    private int nro_linea;
    private Map<String, String> mapeoDePalabrasClave;
    private ManejadorDeArchivo manejadorDeArchivo;

    public AnalizadorLexico(ManejadorDeArchivo manejadorDeArchivo, Map<String, String> mapeoDePalabrasClave) {
        this.manejadorDeArchivo = manejadorDeArchivo;
        this.caracterActual = manejadorDeArchivo.obtenerCaracterActual();
        this.mapeoDePalabrasClave = mapeoDePalabrasClave;
    }

    private void actualizarLexema() {
        this.lexema = this.lexema + (char) this.caracterActual;
    }

    private void actualizarCaracterActual () throws IOException {
        this.manejadorDeArchivo.leerProximoCaracter();
        this.caracterActual = manejadorDeArchivo.obtenerCaracterActual();
    }

    public boolean quedanTokens() {
        return this.caracterActual != -1;
//        return this.manejadorDeArchivo.endOfFile();
    }

    public Token proximoToken() throws IOException, ExcepcionLexica {
        this.lexema = "";
        return estado0();
    }

    private Token estado0() throws IOException, ExcepcionLexica {
        if (Character.isWhitespace(this.caracterActual)) {
            this.actualizarCaracterActual();
            return this.estado0();
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
                        if (this.caracterActual == '<') {
                            this.actualizarLexema();
                            this.actualizarCaracterActual();
                            return this.estado5();
                        }
                        else
                            if (this.caracterActual == '!') {
                                this.actualizarLexema();
                                this.actualizarCaracterActual();
                                return this.estado7();
                            }
                            else
                                if (this.caracterActual == '=') {
                                    this.actualizarLexema();
                                    this.actualizarCaracterActual();
                                    return this.estado9();
                                }
                                else
                                    if (this.caracterActual == '*') {
                                        this.actualizarLexema();
                                        this.actualizarCaracterActual();
                                        return this.estado11();
                                    }
                                    else
                                        if (this.caracterActual == '-') {
                                            this.actualizarLexema();
                                            this.actualizarCaracterActual();
                                            return this.estado12();
                                        }
                                        else
                                            if (this.caracterActual == '+') {
                                                this.actualizarLexema();
                                                this.actualizarCaracterActual();
                                                return this.estado14();
                                            }
                                            else
                                                if (this.caracterActual == '%') {
                                                    this.actualizarLexema();
                                                    this.actualizarCaracterActual();
                                                    return this.estado16();
                                                }
                                                else
                                                    if (this.caracterActual == '&') {
                                                        this.actualizarLexema();
                                                        this.actualizarCaracterActual();
                                                        return this.estado17();
                                                    }
                                                    else
                                                        if (this.caracterActual == '|') {
                                                            this.actualizarLexema();
                                                            this.actualizarCaracterActual();
                                                            return this.estado19();
                                                        }
                                                        else
                                                            if (this.caracterActual == '(') {
                                                                this.actualizarLexema();
                                                                this.actualizarCaracterActual();
                                                                return this.estado21();
                                                            }
                                                            else
                                                                if (this.caracterActual == ')') {
                                                                    this.actualizarLexema();
                                                                    this.actualizarCaracterActual();
                                                                    return this.estado22();
                                                                }
                                                                else
                                                                    if (this.caracterActual == '{') {
                                                                        this.actualizarLexema();
                                                                        this.actualizarCaracterActual();
                                                                        return this.estado23();
                                                                    }
                                                                    else
                                                                        if (this.caracterActual == '}') {
                                                                            this.actualizarLexema();
                                                                            this.actualizarCaracterActual();
                                                                            return this.estado24();
                                                                        }
                                                                        else
                                                                            if (this.caracterActual == ';') {
                                                                                this.actualizarLexema();
                                                                                this.actualizarCaracterActual();
                                                                                return this.estado25();
                                                                            }
                                                                            else
                                                                                if (this.caracterActual == '.') {
                                                                                    this.actualizarLexema();
                                                                                    this.actualizarCaracterActual();
                                                                                    return this.estado26();
                                                                                }
                                                                                else
                                                                                    if (this.caracterActual == ',') {
                                                                                        this.actualizarLexema();
                                                                                        this.actualizarCaracterActual();
                                                                                        return this.estado27();
                                                                                    }
                                                                                    else
                                                                                        if (this.caracterActual == '/') {
                                                                                            this.actualizarLexema();
                                                                                            this.actualizarCaracterActual();
                                                                                            return this.estado28();
                                                                                        }
                                                                                        else
                                                                                            if (this.caracterActual == '"') {
                                                                                                this.actualizarLexema();
                                                                                                this.actualizarCaracterActual();
                                                                                                return this.estado32();
                                                                                            }
                                                                                            else
                                                                                                if (this.caracterActual == '\'') {
                                                                                                    this.actualizarLexema();
                                                                                                    this.actualizarCaracterActual();
                                                                                                    return this.estado35();
                                                                                                }
                                                                                                else
                                                                                                    if (this.caracterActual == -1) {
                                                                        //                                this.actualizarLexema();
                                                                                                        return this.estado40();
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

    private Token estado5() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado6();
        }
        else
            return new Token("menor", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado6() throws IOException, ExcepcionLexica {
        return new Token("menor_igual", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado7() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado8();
        }
        else
            return new Token("negacion", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado8() {
        return new Token("distinto", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado9() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado10();
        }
        else
            return new Token("igual", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado10() {
        return new Token("comparacion", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado11() {
        return new Token("op_multiplicar", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado12() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado13();
        }
        else
            return new Token("op_resta", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado13() { //todo que ponerle al -=
        return new Token("-=", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado14() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado15();
        }
        else
            return new Token("op_suma", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado15() { //todo que ponerle al +=
        return new Token("+=", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado16() {
        return new Token("op_modulo", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado17() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '&') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado18();
        }
        else {
            throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
        }
    }

    private Token estado18() {
        return new Token("op_logico_and", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado19() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '|') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado20();
        }
        else {
            throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
        }
    }

    private Token estado20() {
        return new Token("op_logico_or", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado21() {
        return new Token("parentesis_abre", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado22() {
        return new Token("parentesis_cierra", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado23() {
        return new Token("llave_abre", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado24() {
        return new Token("llave_cierra", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado25() {
        return new Token("punto_y_coma", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado26() {
        return new Token("punto", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado27() {
        return new Token("coma", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado28() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '/') {
            this.actualizarCaracterActual();
            //todo esta bien esto?
            this.lexema = "";
            return this.estado29();
        }
        else
            if (this.caracterActual == '*') {
//                this.lexema = "";
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.estado30();
            }
        else
            return new Token("division", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado29() throws IOException, ExcepcionLexica {
        if (this.caracterActual != '\n' && this.caracterActual != -1) {
            this.actualizarCaracterActual();
            return this.estado29();
        }
        else {
            if (this.caracterActual == -1)
                return this.estado40();
            else
                this.actualizarCaracterActual();
                return this.estado0();
        }
    }

    private Token estado30() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '*') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado31();
        }
        else
            if (this.caracterActual == -1) {
//                System.out.println("lexema: "+this.lexema);
                throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
//                return this.estado40();
            }

            else {
                //todo revisar que pasa si viene /* y nunca */
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.estado30();
            }
    }

    private Token estado31() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '/') {
            this.actualizarCaracterActual();
            //todo esta bien esto?
            this.lexema = "";
            return this.estado0();
        }
        else
            if (this.caracterActual == '*') {
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.estado31();
            }
            else
                if (this.caracterActual == -1) {
                    System.out.println("lexema: " + this.lexema);
                    throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
                }
                else {
                    this.actualizarLexema();
                    this.actualizarCaracterActual();
                    return this.estado30();
                }
    }

    private Token estado32() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '"') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado33();
        }
        else
            if (this.caracterActual == '\n' || this.caracterActual == -1) {
                throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
            }
            else
                if (this.caracterActual == '/') {
                    this.actualizarCaracterActual();
                    return this.estado34();
                }
                else {
                    this.actualizarLexema();
                    this.actualizarCaracterActual();
                    return this.estado32();
                }
    }

    private Token estado33()  {
        //todo preguntar este replace
        return new Token("String", this.lexema.replaceAll("\"", ""), this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado34() throws IOException, ExcepcionLexica {
        if (this.esCaracterEspecialString()) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado32();
        }
        else
            throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado35() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '\\') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado38();
        }
        else
            if (this.caracterActual == '\n' || this.caracterActual == -1)
                throw new ExcepcionLexica("'", this.manejadorDeArchivo.obtenerNumeroLinea());
            else {
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.estado36();
            }
    }

    private Token estado36() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '\'') {
            this.actualizarCaracterActual();
            return this.estado37();
        }
        else {
            this.actualizarLexema();
            throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
        }

    }

    private Token estado37()  {
        return new Token("caracter", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado38() throws IOException, ExcepcionLexica {
        //todo acomodar esto, creo que puede ser cualquier caracter
        if (this.esCaracterEspecialString()) {
            this.lexema = "";
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.estado39();
        }
        else {
            this.actualizarLexema();
            throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
        }
    }

    private Token estado39() throws IOException, ExcepcionLexica {
        if (this.caracterActual == '\'') {
            this.actualizarCaracterActual();
            return this.estado37();
        }
        else
            throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private Token estado40() throws IOException, ExcepcionLexica {
        return new Token("EOF", this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
    }

    private boolean esCaracterEspecialString() {
        return (this.caracterActual == '"' || this.caracterActual == '\'' || this.caracterActual == '\\' || this.caracterActual == 't' ||this.caracterActual == 'b' || this.caracterActual == 'r'
                || this.caracterActual == 'f' || this.caracterActual == 'n');
    }





}
