package LexicalAnalyzer;

import FileHandler.FileHandler;

import java.io.IOException;
import java.util.Map;

public class LexicalAnalyzer {

    private int currentCharacter;
    private String lexeme;
    private Map<String, String> mapeoDePalabrasClave;
    private FileHandler fileHandler;

    public LexicalAnalyzer(FileHandler fileHandler, Map<String, String> mapeoDePalabrasClave) throws IOException {
        this.fileHandler = fileHandler;
        this.updateCurrentCharacter();
        this.mapeoDePalabrasClave = mapeoDePalabrasClave;
    }

    private void updateLexeme() {
        this.lexeme = this.lexeme + (char) this.currentCharacter;
    }

    private void updateCurrentCharacter() throws IOException {
        this.fileHandler.readNextCharacter();
        this.currentCharacter = fileHandler.getCurrentCharacter();
    }

    public Token nextToken() throws IOException, LexicalException {
        this.lexeme = "";
        return estado0();
    }

    private Token estado0() throws IOException, LexicalException {
        if (Character.isWhitespace(this.currentCharacter)) {
            this.updateCurrentCharacter();
            return this.estado0();
        }
        else
            if (Character.isDigit(this.currentCharacter)) {
                this.updateLexeme();
                this.updateCurrentCharacter();
                return this.estado1();
            }
            else
                if (Character.isLetter(this.currentCharacter) && Character.isUpperCase(this.currentCharacter)){
                    this.updateLexeme();
                    this.updateCurrentCharacter();
                    return this.estado2();
                }
                else
                    if (Character.isLetter(this.currentCharacter) && Character.isLowerCase(this.currentCharacter)){
                        this.updateLexeme();
                        this.updateCurrentCharacter();
                        return this.estado48();
                    }
                    else
                        if (this.currentCharacter == '>') {
                            this.updateLexeme();
                            this.updateCurrentCharacter();
                            return this.estado3();
                        }
                        else
                            if (this.currentCharacter == '<') {
                                this.updateLexeme();
                                this.updateCurrentCharacter();
                                return this.estado5();
                            }
                            else
                                if (this.currentCharacter == '!') {
                                    this.updateLexeme();
                                    this.updateCurrentCharacter();
                                    return this.estado7();
                                }
                                else
                                    if (this.currentCharacter == '=') {
                                        this.updateLexeme();
                                        this.updateCurrentCharacter();
                                        return this.estado9();
                                    }
                                    else
                                        if (this.currentCharacter == '*') {
                                            this.updateLexeme();
                                            this.updateCurrentCharacter();
                                            return this.estado11();
                                        }
                                        else
                                            if (this.currentCharacter == '-') {
                                                this.updateLexeme();
                                                this.updateCurrentCharacter();
                                                return this.estado12();
                                            }
                                            else
                                                if (this.currentCharacter == '+') {
                                                    this.updateLexeme();
                                                    this.updateCurrentCharacter();
                                                    return this.estado14();
                                                }
                                                else
                                                    if (this.currentCharacter == '%') {
                                                        this.updateLexeme();
                                                        this.updateCurrentCharacter();
                                                        return this.estado16();
                                                    }
                                                    else
                                                        if (this.currentCharacter == '&') {
                                                            this.updateLexeme();
                                                            this.updateCurrentCharacter();
                                                            return this.estado17();
                                                        }
                                                        else
                                                            if (this.currentCharacter == '|') {
                                                                this.updateLexeme();
                                                                this.updateCurrentCharacter();
                                                                return this.estado19();
                                                            }
                                                            else
                                                                if (this.currentCharacter == '(') {
                                                                    this.updateLexeme();
                                                                    this.updateCurrentCharacter();
                                                                    return this.estado21();
                                                                }
                                                                else
                                                                    if (this.currentCharacter == ')') {
                                                                        this.updateLexeme();
                                                                        this.updateCurrentCharacter();
                                                                        return this.estado22();
                                                                    }
                                                                    else
                                                                        if (this.currentCharacter == '{') {
                                                                            this.updateLexeme();
                                                                            this.updateCurrentCharacter();
                                                                            return this.estado23();
                                                                        }
                                                                        else
                                                                            if (this.currentCharacter == '}') {
                                                                                this.updateLexeme();
                                                                                this.updateCurrentCharacter();
                                                                                return this.estado24();
                                                                            }
                                                                            else
                                                                                if (this.currentCharacter == ';') {
                                                                                    this.updateLexeme();
                                                                                    this.updateCurrentCharacter();
                                                                                    return this.estado25();
                                                                                }
                                                                                else
                                                                                    if (this.currentCharacter == '.') {
                                                                                        this.updateLexeme();
                                                                                        this.updateCurrentCharacter();
                                                                                        return this.estado26();
                                                                                    }
                                                                                    else
                                                                                        if (this.currentCharacter == ',') {
                                                                                            this.updateLexeme();
                                                                                            this.updateCurrentCharacter();
                                                                                            return this.estado27();
                                                                                        }
                                                                                        else
                                                                                            if (this.currentCharacter == '/') {
                                                                                                this.updateLexeme();
                                                                                                this.updateCurrentCharacter();
                                                                                                return this.estado28();
                                                                                            }
                                                                                            else
                                                                                                if (this.currentCharacter == '"') {
                                                                                                    this.updateLexeme();
                                                                                                    this.updateCurrentCharacter();
                                                                                                    return this.estado32();
                                                                                                }
                                                                                                else
                                                                                                    if (this.currentCharacter == '\'') {
                                                                                                        this.updateLexeme();
                                                                                                        this.updateCurrentCharacter();
                                                                                                        return this.estado35();
                                                                                                    }
                                                                                                    else
                                                                                                        if (this.currentCharacter == -1) {
                                                                                                            return this.estado49();
                                                                                                        }
                                                                                                        else {
                                                                                                            this.updateLexeme();
                                                                                                            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), this.lexeme + " no es un símbolo válido", this.fileHandler.getRowWithError());
                                                                                                        }
    }

    private Token estado1() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado40();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado2() throws IOException {
        if (Character.isLetter(this.currentCharacter) || Character.isDigit(this.currentCharacter) || this.currentCharacter == '_') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado2();
        }
        else
//            if (this.mapeoDePalabrasClave.containsKey(this.lexeme)) {
//                return new Token(this.mapeoDePalabrasClave.get(this.lexeme), this.lexeme, this.fileHandler.getCurrentRowNumber());
//            }
//            else
                return new Token("idClase", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado3() throws IOException {
        if (this.currentCharacter == '=') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado4();
        }
        else
            return new Token("mayor", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado4() {
        return new Token("mayor_igual", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado5() throws IOException, LexicalException {
        if (this.currentCharacter == '=') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado6();
        }
        else
            return new Token("menor", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado6() {
        return new Token("menor_igual", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado7() throws IOException {
        if (this.currentCharacter == '=') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado8();
        }
        else
            return new Token("negacion", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado8() {
        return new Token("distinto", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado9() throws IOException {
        if (this.currentCharacter == '=') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado10();
        }
        else
            return new Token("igual", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado10() {
        return new Token("comparacion", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado11() {
        return new Token("op_multiplicar", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado12() throws IOException {
        if (this.currentCharacter == '=') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado13();
        }
        else
            return new Token("op_resta", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado13() { //todo que ponerle al -=
        return new Token("-=", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado14() throws IOException {
        if (this.currentCharacter == '=') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado15();
        }
        else
            return new Token("op_suma", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado15() { //todo que ponerle al +=
        return new Token("+=", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado16() {
        return new Token("op_modulo", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado17() throws IOException, LexicalException {
        if (this.currentCharacter == '&') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado18();
        } else
            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber() - 1, this.lexeme + " no es un símbolo válido", this.fileHandler.getRowWithError());
    }

    private Token estado18() {
        return new Token("op_AND", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado19() throws IOException, LexicalException {
        if (this.currentCharacter == '|') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado20();
        }
        else {
            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber() - 1, " no es un símbolo válido", this.fileHandler.getRowWithError());        }
    }

    private Token estado20() {
        return new Token("op_OR", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado21() {
        return new Token("parentesis_abre", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado22() {
        return new Token("parentesis_cierra", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado23() {
        return new Token("llave_abre", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado24() {
        return new Token("llave_cierra", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado25() {
        return new Token("punto_y_coma", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado26() {
        return new Token("punto", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado27() {
        return new Token("coma", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado28() throws IOException, LexicalException {
        if (this.currentCharacter == '/') {
            this.updateCurrentCharacter();
            this.lexeme = "";
            return this.estado29();
        }
        else
            if (this.currentCharacter == '*') {
                this.updateLexeme();
                this.updateCurrentCharacter();
                return this.estado30();
            }
        else
            return new Token("division", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado29() throws IOException, LexicalException {
        if (this.currentCharacter != '\n' && this.currentCharacter != -1) {
            this.updateCurrentCharacter();
            return this.estado29();
        }
        else {
            if (this.currentCharacter == -1)
                return this.estado49();
            else
                this.updateCurrentCharacter();
                return this.estado0();
        }
    }

    private Token estado30() throws IOException, LexicalException {
        if (this.currentCharacter == '*') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado31();
        }
        else
            if (this.currentCharacter == -1)
                throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), "es un comentario multilinea sin cerrar ", this.fileHandler.getRowWithError());
            else {
                //todo revisar que pasa si viene /* y nunca */ ya anda, ver el lexema resultante
                if (this.currentCharacter != '\n')
                    this.updateLexeme();
                this.updateCurrentCharacter();
                return this.estado30();
            }
    }

    private Token estado31() throws IOException, LexicalException {
        if (this.currentCharacter == '/') {
            this.updateCurrentCharacter();
            this.lexeme = "";
            return this.estado0();
        }
        else
            if (this.currentCharacter == '*') {
                this.updateLexeme();
                this.updateCurrentCharacter();
                return this.estado31();
            }
            else
                if (this.currentCharacter == -1) {
                    System.out.println("lexema: " + this.lexeme);
                    throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), "comentario multilinea sin cerrar", this.fileHandler.getRowWithError());
                }
                else {
                    if (this.currentCharacter != '\n')
                        this.updateLexeme();
                    this.updateCurrentCharacter();
                    return this.estado30();
                }
    }

    private Token estado32() throws IOException, LexicalException {
        if (this.currentCharacter == '"') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado33();
        }
        else
            if (this.currentCharacter == '\n' || this.currentCharacter == -1)
                throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), this.lexeme + " no es un String válido", this.fileHandler.getRowWithError());
            else
                if (this.currentCharacter == '\\') {
                    this.updateLexeme();
                    this.updateCurrentCharacter();
                    return this.estado34();
                }
                else {
                    this.updateLexeme();
                    this.updateCurrentCharacter();
                    return this.estado32();
                }
    }

    private Token estado33()  {
        return new Token("String", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado34() throws IOException, LexicalException {
        if (this.currentCharacter == '"') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado32();
        }
        else {
            if (this.currentCharacter != '\n')
                this.updateLexeme();
            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), this.lexeme + " no es un símbolo válido dentro de un String", this.fileHandler.getRowWithError());        }
    }

    private Token estado35() throws IOException, LexicalException {
        if (this.currentCharacter == '\\') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado38();
        }
        else
            if (this.currentCharacter == '\n' || this.currentCharacter == -1)
                throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), "no es un caracter válido", this.fileHandler.getRowWithError());
            else {
                this.updateLexeme();
                this.updateCurrentCharacter();
                return this.estado36();
            }
    }

    private Token estado36() throws IOException, LexicalException {
        if (this.currentCharacter == '\'') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado37();
        }
        else {
            this.updateLexeme();
            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), "no es un caracter válido", this.fileHandler.getRowWithError());        }

    }

    private Token estado37()  {
        return new Token("caracter", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado38() throws IOException, LexicalException {
        if (this.currentCharacter != -1 || this.currentCharacter != '\n') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado39();
        }
        else {
            this.updateLexeme();
            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), "no es un caracter válido", this.fileHandler.getRowWithError());        }
    }

    private Token estado39() throws IOException, LexicalException {
        if (this.currentCharacter == '\'') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado37();
        }
        else {
            this.updateLexeme();
            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), "no es un caracter válido", this.fileHandler.getRowWithError());
        }
    }

    private Token estado40() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado41();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado41() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado42();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado42() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado43();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado43() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado44();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado44() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado45();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado45() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado46();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado46() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado47();
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado47() throws IOException, LexicalException {
        if (Character.isDigit(this.currentCharacter)) {
            this.updateLexeme();
            throw new LexicalException(this.lexeme, this.fileHandler.getCurrentRowNumber(), this.fileHandler.getCurrentColumnNumber(), this.lexeme+ " tiene más de 9 dígitos", this.fileHandler.getRowWithError());
        }
        else
            return new Token("entero", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado48() throws IOException {
        if (Character.isLetter(this.currentCharacter) || Character.isDigit(this.currentCharacter) || this.currentCharacter == '_') {
            this.updateLexeme();
            this.updateCurrentCharacter();
            return this.estado48();
        }
        else
            return new Token("idMV", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

    private Token estado49() {
        return new Token("EOF", this.lexeme, this.fileHandler.getCurrentRowNumber());
    }

}
