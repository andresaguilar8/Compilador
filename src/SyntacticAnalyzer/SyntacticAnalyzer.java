package SyntacticAnalyzer;

import LexicalAnalyzer.LexicalAnalyzer;
import LexicalAnalyzer.Token;
import LexicalAnalyzer.LexicalException;
import SemanticAnalyzer.*;
import SemanticAnalyzer.Class;
import SemanticAnalyzer.ConcreteClass;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class SyntacticAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException, SyntacticException, SemanticException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.currentToken = this.lexicalAnalyzer.nextToken();
        this.inicial();
    }

    private void match(String tokenId) throws SyntacticException, IOException, LexicalException {
        if (tokenId.equals(this.currentToken.getTokenId())) {
            this.currentToken = this.lexicalAnalyzer.nextToken();
        } else
            throw new SyntacticException(this.currentToken, tokenId);
    }

    private void inicial() throws LexicalException, IOException, SyntacticException, SemanticException {
        this.listaClases();
        Token EOFToken = this.currentToken;
        SymbolTable.getInstance().setEOFToken(EOFToken);
        match("EOF");
    }

    private void listaClases() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_class", "pr_interface").contains(this.currentToken.getTokenId())) {
            this.clase();
            this.listaClases();
        } else {
            //epsilon, no hago nada
        }
    }

    private void clase() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (this.currentToken.getTokenId().equals("pr_class")) {
            this.claseConcreta();
        } else if (this.currentToken.getTokenId().equals("pr_interface")) {
            this.interface_();
        } else
            throw new SyntacticException(this.currentToken, "class o interface");
    }

    private void claseConcreta() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (this.currentToken.getTokenId().equals("pr_class")) {
            this.match("pr_class");
            Token currentTokenClass = this.currentToken;
            this.match("idClase");
            Token ancestorToken = this.heredaDe();
            ConcreteClass currentClass = new ConcreteClass(currentTokenClass, ancestorToken);
            SymbolTable.getInstance().setActualClass(currentClass);
            SymbolTable.getInstance().insertConcreteClass(currentClass);
            this.implementaA();
            match("{");
            this.listaMiembros();
            match("}");
        } else
            throw new SyntacticException(this.currentToken, "class");
    }

    private void interface_() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (this.currentToken.getTokenId().equals("pr_interface")) {
            this.match("pr_interface");
            Interface currentInterface = new Interface(this.currentToken);
            SymbolTable.getInstance().insertInterface(currentInterface);
            SymbolTable.getInstance().setActualClass(currentInterface);
            this.match("idClase");
            this.extiendeA();
            match("{");
            this.listaEncabezados();
            match("}");
        } else
            throw new SyntacticException(this.currentToken, "interface");
    }

    private Token heredaDe() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals("pr_extends")) {
            match("pr_extends");
            Token ancestorToken = this.currentToken;
            match("idClase");
            return ancestorToken;
        }
        else
            return SymbolTable.getInstance().getConcreteClass("Object").getToken();
    }

    private void implementaA() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals("pr_implements")) {
            this.match("pr_implements");
            this.listaTipoReferencia();
        } else {
            // epsilon, no hago nada
        }
    }

    private void extiendeA() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals("pr_extends")) {
            this.match("pr_extends");
            this.listaTipoReferencia();
        } else {
            //epsilon, no hago nada
        }
    }

    private void listaTipoReferencia() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals("idClase")) {
            Token interfaceToken = this.currentToken;
            Interface interfaceToAdd = new Interface(interfaceToken);
            Class currentClass = SymbolTable.getInstance().getCurrentClass();
            HashSet<Interface> interfaces = currentClass.getInterfaces();
            interfaces.add(interfaceToAdd);
            this.match("idClase");
            this.listaTipoReferenciaPrima();
        } else
            throw new SyntacticException(this.currentToken, "idClase");
    }

    private void listaTipoReferenciaPrima() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            this.listaTipoReferencia();
        } else {
            //epsilon, no hago nada
        }
    }

    private void listaMiembros() throws LexicalException, SyntacticException, IOException, SemanticException {
        if (Arrays.asList("pr_public", "pr_private", "pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            this.miembro();
            this.listaMiembros();
        } else {
            //epsilon, no hago nada
        }
    }

    private void listaEncabezados() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            this.encabezadoMetodo();
            this.match(";");
            this.listaEncabezados();
        }
        else {
            //epsilon, no hago nada
        }
    }

    private void miembro() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_public", "pr_private").contains(this.currentToken.getTokenId()))
            this.atributo();
        else
            if (Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId()))
                this.metodo();
            else
                throw new SyntacticException(this.currentToken, "public, private, static, void, idClase, boolean, char o int");
    }

    private void atributo() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_public", "pr_private").contains(this.currentToken.getTokenId())) {
            String visibility = this.visibilidad();
            Type type = this.tipo();
            this.listaDecAtrs(visibility, type);
            this.match(";");
        } else
            throw new SyntacticException(this.currentToken, "public o private");
    }

    private void metodo() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            this.encabezadoMetodo();
            this.bloque();
        }
         else
            throw new SyntacticException(this.currentToken, "public, void, idClase, boolean, char o int");
    }

    private void encabezadoMetodo() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            String staticMethod = this.estaticoOpt();
            Type methodType = this.tipoMetodo();
            Token methodToken = this.currentToken;
            Method method = new Method(methodToken, staticMethod, methodType);
            SymbolTable.getInstance().setCurrentMethod(method);
            SymbolTable.getInstance().getCurrentClass().insertMethod(method);
            this.match("idMV");
            this.argsFormales();
        } else
            throw new SyntacticException(this.currentToken, "static, void, idClase, boolean, char o int");
    }

    private String visibilidad() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals("pr_public")) {
            this.match("pr_public");
            return "public";
        } else if (this.currentToken.getTokenId().equals("pr_private")) {
            this.match("pr_private");
            return "private";
        } else
            throw new SyntacticException(this.currentToken, "public o private");
    }

    private Type tipo() throws LexicalException, IOException, SyntacticException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            return this.tipoPrimitivo();
        } else
            if (this.currentToken.getTokenId().equals("idClase")) {
                Type typeToReturn = new ReferenceType(this.currentToken);
                this.match("idClase");
                return typeToReturn;
            }
            else
                throw new SyntacticException(this.currentToken, "boolean, char, int o idClase");
    }

    private Type tipoPrimitivo() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals("pr_boolean")) {
            Type typeToReturn = new PrimitiveType(this.currentToken);
            this.match("pr_boolean");
            return typeToReturn;
        }
        else if (this.currentToken.getTokenId().equals("pr_char")) {
            Type typeToReturn = new PrimitiveType(this.currentToken);
            this.match("pr_char");
            return typeToReturn;
        }
        else if (this.currentToken.getTokenId().equals("pr_int")) {
            Type typeToReturn = new PrimitiveType(this.currentToken);
            this.match("pr_int");
            return typeToReturn;
        }
        else
            throw new SyntacticException(this.currentToken, "boolean, char o int");
    }

    private void listaDecAtrs(String atributeVisibility, Type type) throws LexicalException, IOException, SyntacticException, SemanticException {
        if (this.currentToken.getTokenId().equals("idMV")) {
            Token atributeToken = this.currentToken;
            this.match("idMV");
            Attribute atribute = new Attribute(atributeToken, type, atributeVisibility);
            ConcreteClass concreteCurrentClass = (ConcreteClass) SymbolTable.getInstance().getCurrentClass();
            concreteCurrentClass.insertAttribute(atribute);
            this.listaDecAtrsPrima(atributeVisibility, type);
        } else
            throw new SyntacticException(this.currentToken, "idMV");
    }

    private void listaDecAtrsPrima(String visibility, Type type) throws LexicalException, IOException, SyntacticException, SemanticException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            this.listaDecAtrs(visibility, type);
        } else {
            // epsilon, no hago nada
        }
    }

    private String estaticoOpt() throws LexicalException, IOException, SyntacticException {
        if (this.currentToken.getTokenId().equals("pr_static")) {
            this.match("pr_static");
            return "static";
        }
        else
            return "";
    }

    private Type tipoMetodo() throws LexicalException, IOException, SyntacticException {
        if (Arrays.asList("idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId()))
            return this.tipo();
        else
            if (this.currentToken.getTokenId().equals("pr_void")) {
                Type typeToReturn = new PrimitiveType(this.currentToken);
                this.match("pr_void");
                return typeToReturn;
            }
        else
            throw new SyntacticException(this.currentToken, "idClase, boolean, char, int o void");
    }

    private void argsFormales() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (this.currentToken.getTokenId().equals("(")) {
            this.match("(");
            this.listaArgsFormalesOpt();
            this.match(")");
        }
        else
            throw new SyntacticException(this.currentToken, "(");
    }

    private void listaArgsFormalesOpt() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(this.currentToken.getTokenId()))
            this.listaArgsFormales();
        else {
            // epsilon, no hago nada
        }
    }

    private void listaArgsFormales() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(this.currentToken.getTokenId())) {
            this.argFormal();
            this.listaArgsFormalesPrima();
        } else
            throw new SyntacticException(this.currentToken, "boolean, char o int");
    }

    private void listaArgsFormalesPrima() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            this.listaArgsFormales();
        } else {
            // epsilon, no hago nada
        }
    }

    private void argFormal() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(this.currentToken.getTokenId())) {
            Type parameterType = this.tipo();
            Parameter parameter = new Parameter(this.currentToken, parameterType);
            SymbolTable.getInstance().getCurrentMethod().insertParameter(parameter);
            this.match("idMV");
        } else
            throw new SyntacticException(this.currentToken, "boolean, char, int o idClase");
    }

    private void bloque() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("{")) {
            this.match("{");
            this.listaSentencias();
            this.match("}");
        } else
            throw new SyntacticException(this.currentToken, "{");
    }

    private void sentenciaPrima() throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("=", "+=", "-=").contains(this.currentToken.getTokenId())) {
            this.tipoAsignacion();
            this.expresion();
        }
        else {
            //epsilon, no hago nada
        }
    }

    private void listaSentencias() throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList(";", "idMV", "pr_this", "pr_new", "idClase", "(", "pr_return", "pr_if", "pr_while", "{", "pr_var").contains(this.currentToken.getTokenId())) {
            this.sentencia();
            this.listaSentencias();
        } else {
            // epsilon, no hago nada
        }
    }

    private void sentencia() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals(";"))
            this.match(";");
        else if (Arrays.asList("idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            this.acceso();
            this.sentenciaPrima();
            this.match(";");
        } else if (this.currentToken.getTokenId().equals("pr_var")) {
            this.varLocal();
            this.match(";");
        } else if (this.currentToken.getTokenId().equals("pr_return")) {
            this.noTerminalReturn();
            this.match(";");
        } else if (this.currentToken.getTokenId().equals("pr_if"))
            this.noTerminalIf();
        else if (this.currentToken.getTokenId().equals("pr_while"))
            this.noTerminalWhile();
        else if (this.currentToken.getTokenId().equals("{"))
            this.bloque();
        else
            throw new SyntacticException(this.currentToken, ";, this, new, idClase, (, var, return, if, while o {");
    }

    private void tipoAsignacion() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("="))
            this.match("=");
        else if (this.currentToken.getTokenId().equals("+="))
            this.match("+=");
        else if (this.currentToken.getTokenId().equals("-="))
            this.match("-=");
        else
            throw new SyntacticException(this.currentToken, "=, += o -=");
    }

    private void varLocal() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("pr_var")) {
            this.match("pr_var");
            this.match("idMV");
            this.match("=");
            this.expresion();
        } else
            throw new SyntacticException(this.currentToken, "var");
    }

    private void noTerminalReturn() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("pr_return")) {
            this.match("pr_return");
            this.expresionOpt();
        } else
            throw new SyntacticException(this.currentToken, "return");
    }

    private void expresionOpt() throws SyntacticException, LexicalException, IOException {
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId()))
            this.expresion();
        else {
            //epsilon, no hago nada
        }
    }

    private void noTerminalIf() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("pr_if")) {
            this.match("pr_if");
            this.match("(");
            this.expresion();
            this.match(")");
            this.sentencia();
            this.noTerminalIfPrima();
        } else
            throw new SyntacticException(this.currentToken, "if");
    }

    private void noTerminalIfPrima() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("pr_else")) {
            this.match("pr_else");
            this.sentencia();
        } else {
            // epsilon, no hago nada
        }
    }

    private void noTerminalWhile() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("pr_while")) {
            this.match("pr_while");
            this.match("(");
            this.expresion();
            this.match(")");
            this.sentencia();
        } else
            throw new SyntacticException(this.currentToken, "while");
    }

    private void expresion() throws SyntacticException, LexicalException, IOException {
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            this.expresionUnaria();
            this.expresionPrima();
        }
        else
            throw new SyntacticException(this.currentToken, "+, -, !, null, true, false, intLiteral, charLiteral o stringLiteral");
    }

    private void expresionPrima() throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%").contains(this.currentToken.getTokenId())) {
            this.operadorBinario();
            this.expresionUnaria();
            this.expresionPrima();
        } else {
            // epsilon, no hago nada
        }
    }

    private void operadorBinario() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("||"))
            this.match("||");
        else if (this.currentToken.getTokenId().equals("&&"))
            this.match("&&");
        else if (this.currentToken.getTokenId().equals("=="))
            this.match("==");
        else if (this.currentToken.getTokenId().equals("!="))
            this.match("!=");
        else if (this.currentToken.getTokenId().equals("<"))
            this.match("<");
        else if (this.currentToken.getTokenId().equals(">"))
            this.match(">");
        else if (this.currentToken.getTokenId().equals("<="))
            this.match("<=");
        else if (this.currentToken.getTokenId().equals(">="))
            this.match(">=");
        else if (this.currentToken.getTokenId().equals("+"))
            this.match("+");
        else if (this.currentToken.getTokenId().equals("-"))
            this.match("-");
        else if (this.currentToken.getTokenId().equals("*"))
            this.match("*");
        else if (this.currentToken.getTokenId().equals("/"))
            this.match("/");
        else if (this.currentToken.getTokenId().equals("%"))
            this.match("%");
        else
            throw new SyntacticException(this.currentToken, "+, -, *, /, %, >=, <=, >, <, !=, ==, && o ||");
    }

    private void expresionUnaria() throws SyntacticException, LexicalException, IOException {
        if (Arrays.asList("+", "-", "!").contains(this.currentToken.getTokenId())) {
            this.operadorUnario();
            this.operando();
        } else if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMV", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId()))
            this.operando();
        else
            throw new SyntacticException(this.currentToken, "+, -, !, null, true, false, intLiteral, charLiteral, stringLiteral, this, idMV, new, idClase o (");
    }

    private void operadorUnario() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("+"))
            this.match("+");
        else if (this.currentToken.getTokenId().equals("-"))
            this.match("-");
        else if (this.currentToken.getTokenId().equals("!"))
            this.match("!");
        else
            throw new SyntacticException(this.currentToken, "+, - o !");
    }

    private void operando() throws SyntacticException, LexicalException, IOException {
        if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral").contains(this.currentToken.getTokenId()))
            this.literal();
        else if (Arrays.asList("idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId()))
            this.acceso();
        else
            throw new SyntacticException(this.currentToken, "null, true, false, intLiteral, charLiteral, stringLiteral, idMV, this, new, idClase o (");
    }

    private void literal() throws SyntacticException, LexicalException, IOException {
        if (this.currentToken.getTokenId().equals("pr_null"))
            this.match("pr_null");
        else if (this.currentToken.getTokenId().equals("pr_true"))
            this.match("pr_true");
        else if (this.currentToken.getTokenId().equals("pr_false"))
            this.match("pr_false");
        else if (this.currentToken.getTokenId().equals("intLiteral"))
            this.match("intLiteral");
        else if (this.currentToken.getTokenId().equals("charLiteral"))
            this.match("charLiteral");
        else if (this.currentToken.getTokenId().equals("stringLiteral"))
            this.match("stringLiteral");
        else
            throw new SyntacticException(this.currentToken, "null, true, false, intLiteral, charLiteral o stringLiteral");
    }

    private void acceso() throws SyntacticException, LexicalException, IOException {
        if (Arrays.asList("pr_this", "idMV", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            this.primario();
            this.encadenadoOpt();
        } else
            throw new SyntacticException(this.currentToken, "this, idMV, new, idClase o (");
    }

    private void primario() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("idMV")) {
            this.match("idMV");
            this.primarioPrima();
        } else if (this.currentToken.getTokenId().equals("pr_this"))
            this.accesoThis();
        else if (this.currentToken.getTokenId().equals("pr_new"))
            this.accesoConstructor();
        else if (this.currentToken.getTokenId().equals("idClase"))
            this.accesoMetodoEstatico();
        else if (this.currentToken.getTokenId().equals("("))
            this.expresionParentizada();
        else
            throw new SyntacticException(this.currentToken, "this, idMV, pnew, idClase o (");
    }

    private void primarioPrima() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("("))
            this.argsActuales();
        else {
            // epsilon, no hago nada
        }
    }

    private void accesoThis() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("pr_this"))
            this.match("pr_this");
        else
            throw new SyntacticException(this.currentToken, "this");
    }

    private void accesoConstructor() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("pr_new")) {
            this.match("pr_new");
            this.match("idClase");
            this.match("(");
            this.match(")");
        }
        else
            throw new SyntacticException(this.currentToken, "new");
    }

    private void expresionParentizada() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("(")) {
            this.match("(");
            this.expresion();
            this.match(")");
        } else
            throw new SyntacticException(this.currentToken, "(");
    }

    private void accesoMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("idClase")) {
            this.match("idClase");
            this.match(".");
            this.match("idMV");
            this.argsActuales();
        } else
            throw new SyntacticException(this.currentToken, "idClase");
    }

    private void argsActuales() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("(")) {
            this.match("(");
            this.listaExpsOpt();
            this.match(")");
        } else
            throw new SyntacticException(this.currentToken, "(");
    }

    private void listaExpsOpt() throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId()))
            this.listaExps();
        else {
            // epsilon, no hago nada
        }
    }

    private void listaExps() throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            this.expresion();
            this.listaExpsPrima();
        }
        else
            throw new SyntacticException(this.currentToken, "+, -, !, null, true, false, intLiteral, charLiteral, stringLiteral, idMV, this, new, idClase, (");
    }

    private void listaExpsPrima() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            this.listaExps();
        } else {
            // epsilon, no hago nada
        }
    }

    private void encadenadoOpt() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals(".")) {
            this.match(".");
            this.match("idMV");
            this.encadenadoOptPrima();
        }
        else {
            // epsilon, no hago nada
        }
    }

    private void encadenadoOptPrima() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("."))
            this.encadenadoOpt();
        else
            if (this.currentToken.getTokenId().equals("(")) {
                this.argsActuales();
                this.encadenadoOpt();
            }
            else {
                // epsilon,  no hago nada
            }
    }

}