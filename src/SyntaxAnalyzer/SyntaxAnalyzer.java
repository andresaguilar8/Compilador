package SyntaxAnalyzer;

import LexicalAnalyzer.LexicalAnalyzer;
import LexicalAnalyzer.Token;
import LexicalAnalyzer.LexicalException;
import java.io.IOException;
import java.util.Arrays;

public class SyntaxAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException, SyntaxException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.currentToken = this.lexicalAnalyzer.nextToken();
        this.inicial();
    }

    public void match(String tokenId) throws SyntaxException, IOException, LexicalException {
        if (tokenId.equals(this.currentToken.getTokenId())) {
            this.currentToken = this.lexicalAnalyzer.nextToken();
        } else
            throw new SyntaxException(this.currentToken, tokenId);
    }

    public void inicial() throws LexicalException, IOException, SyntaxException {
        this.listaClases();
        match("EOF");
    }

    public void listaClases() throws LexicalException, IOException, SyntaxException {
        if (Arrays.asList("pr_class", "pr_interface").contains(this.currentToken.getTokenId())) {
            this.clase();
            this.listaClases();
        } else {
            //epsilon, no hago nada
        }
    }

    public void clase() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_class")) {
            this.claseConcreta();
        }
        else
            if (this.currentToken.getTokenId().equals("pr_interface")) {
                this.interface_();
            }
            else
                throw new SyntaxException(this.currentToken, "class o interface");
    }

    public void claseConcreta() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_class")) {
            this.match("pr_class");
            this.match("idClase");
            this.heredaDe();
            this.implementaA();
            match("{");
            this.listaMiembros();
            match("}");
        } else
              throw new SyntaxException(this.currentToken, "class");
    }

    public void interface_() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_interface")) {
            this.match("pr_interface");
            this.match("idClase");
            this.extiendeA();
            match("{");
            this.listaEncabezados();
            match("}");
        }
        else
            throw new SyntaxException(this.currentToken, "interface");
    }

    public void heredaDe() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_extends")) {
            match("pr_extends");
        } else {
            //todo epsilon, no hago nada
        }
    }

    public void implementaA() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_implements")) {
            this.match("pr_implements");
            this.listaTipoReferencia();
        } else {
            //todo epsilon, no hago nada
        }
    }

    public void extiendeA() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_extends")) {
            this.match("pr_extends");
            this.listaTipoReferencia();
        } else {
            //epsilon, no hago nada
        }
    }

    public void listaTipoReferencia() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("idClase")) {
            this.match("idClase");
            this.listaTipoReferenciaPrima();
        } else
            throw new SyntaxException(this.currentToken, "idClase");
    }

    public void listaTipoReferenciaPrima() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            this.listaTipoReferencia();
        } else {
            //epsilon, no hago nada
        }
    }

    public void listaMiembros() throws LexicalException, SyntaxException, IOException {
        if (Arrays.asList("pr_public", "pr_private", "pr_static").contains(this.currentToken.getTokenId())) {
            this.miembro();
            this.listaMiembros();
        } else {
            //epsilon, no hago nada
        }
    }

    public void listaEncabezados() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_static")) {
            this.encabezadoMetodo();
            this.match(";");
            this.listaEncabezados();
        } else {
            //epsilon, no hago nada
        }
    }

    public void miembro() throws LexicalException, IOException, SyntaxException {
        //todo acomodar y hacer en una sola linea
        if (Arrays.asList("pr_public", "pr_private").contains(this.currentToken.getTokenId()))
            this.atributo();
        else
            if (this.currentToken.getTokenId().equals("pr_static"))
                this.metodo();
            else
                throw new SyntaxException(this.currentToken, "pr_public, pr_private o pr_static");
    }

    public void atributo() throws LexicalException, IOException, SyntaxException {
        if (Arrays.asList("pr_public", "pr_private").contains(this.currentToken.getTokenId())) {
            this.visibilidad();
            this.tipo();
            this.listaDecAtrs();
            this.match(";");
        } else
            throw new SyntaxException(this.currentToken, "pr_public, pr_private");
    }

    public void metodo() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_static")) {
            this.encabezadoMetodo();
            this.bloque();
        } else {
            //todo epsilon, no hago nada
        }
    }

    private void encabezadoMetodo() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_static")) {
            this.estaticoOpt();
            this.tipoMetodo();
            this.match("idMV");
            this.argsFormales();
        } else
            throw new SyntaxException(this.currentToken, "pr_static");
    }

    private void visibilidad() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_public")) {
            this.match("pr_public");
        } else if (this.currentToken.getTokenId().equals("pr_private")) {
            this.match("pr_private");
        } else
            throw new SyntaxException(this.currentToken, "pr_public");
    }

    private void tipo() throws LexicalException, IOException, SyntaxException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            this.tipoPrimitivo();
            this.match("idClase");
        } else
            throw new SyntaxException(this.currentToken, "pr_boolean, pr_char o pr_int");
    }

    private void tipoPrimitivo() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_boolean"))
            this.match("pr_boolean");
        else
            if (this.currentToken.getTokenId().equals("pr_char"))
                this.match("pr_char");
            else
                if (this.currentToken.getTokenId().equals("pr_int"))
                    this.match("pr_int");
                else
                    throw new SyntaxException(this.currentToken, "pr_boolean, pr_char o pr_int");
    }

    private void listaDecAtrs() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("idMV")) {
            this.match("idMV");
            this.listaDecAtrsPrima();
        }
        else
            throw new SyntaxException(this.currentToken, "idMV");
    }

    private void listaDecAtrsPrima() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            this.listaDecAtrs();
        } else {
            // epsilon, no hago nada
        }
    }

    private void estaticoOpt() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("pr_static"))
            this.match("pr_static");
        else {
            // epsilon, no hago nada
        }
    }

    private void tipoMetodo() throws LexicalException, IOException, SyntaxException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId()))
            this.tipo();
        else
            if (this.currentToken.equals("void"))
                this.match("void");
            else
                throw new SyntaxException(this.currentToken, "pr_boolean, pr_char, pr_int o void");
    }

    private void argsFormales() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals("(")) {
            this.match("(");
            this.listaArgsFormalesOpt();
            this.match(")");
        }
        else
            throw new SyntaxException(this.currentToken, "(");
    }

    private void listaArgsFormalesOpt() throws LexicalException, IOException, SyntaxException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId()))
            this.listaArgsFormales();
        else {
            // epsilon, no hago nada
        }
    }

    private void listaArgsFormales() throws LexicalException, IOException, SyntaxException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            this.argsFormales();
            this.listaArgsFormalesPrima();
        }
        else
            throw new SyntaxException(this.currentToken, "pr_boolean, pr_char o pr_int");
    }

    private void listaArgsFormalesPrima() throws LexicalException, IOException, SyntaxException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            this.listaArgsFormales();
        } else {
            // epsilon, no hago nada
        }
    }

    private void argFormal() throws LexicalException, IOException, SyntaxException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            this.tipo();
            this.match("idMV");
        } else
            throw new SyntaxException(this.currentToken, "pr_boolean, pr_char o pr_int");
    }

    private void bloque() {
        if (this.currentToken.getTokenId().equals("{")) {
            this.match("{");
        }
    }

}