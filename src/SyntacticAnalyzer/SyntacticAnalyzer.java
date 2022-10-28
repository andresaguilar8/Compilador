package SyntacticAnalyzer;

import AST.Access.*;
import AST.Encadenado.Encadenado;
import AST.Encadenado.LlamadaEncadenada;
import AST.Encadenado.VarEncadenada;
import AST.Expression.*;
import AST.Sentence.*;
import LexicalAnalyzer.LexicalAnalyzer;
import LexicalAnalyzer.Token;
import LexicalAnalyzer.LexicalException;
import SemanticAnalyzer.*;
import SemanticAnalyzer.Class;
import SemanticAnalyzer.ConcreteClass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SyntacticAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException, SyntacticException, SemanticException, SemanticExceptionSimple {
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

    private void inicial() throws LexicalException, IOException, SyntacticException, SemanticException, SemanticExceptionSimple {
        this.listaClases();
        Token EOFToken = this.currentToken;
        SymbolTable.getInstance().setEOFToken(EOFToken);
        match("EOF");
    }

    private void listaClases() throws LexicalException, IOException, SyntacticException, SemanticException, SemanticExceptionSimple {
        if (Arrays.asList("pr_class", "pr_interface").contains(this.currentToken.getTokenId())) {
            this.clase();
            this.listaClases();
        } else {
            //epsilon, no hago nada
        }
    }

    private void clase() throws LexicalException, IOException, SyntacticException, SemanticException, SemanticExceptionSimple {
        if (this.currentToken.getTokenId().equals("pr_class")) {
            this.claseConcreta();
        } else if (this.currentToken.getTokenId().equals("pr_interface")) {
            this.interface_();
        } else
            throw new SyntacticException(this.currentToken, "class o interface");
    }

    private void claseConcreta() throws LexicalException, IOException, SyntacticException, SemanticException, SemanticExceptionSimple {
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
            currentClass.addAncestorInterface(interfaceToAdd);
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

    private void listaMiembros() throws LexicalException, SyntacticException, IOException, SemanticException, SemanticExceptionSimple {
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

    private void miembro() throws LexicalException, IOException, SyntacticException, SemanticException, SemanticExceptionSimple {
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

    private void metodo() throws LexicalException, IOException, SyntacticException, SemanticException, SemanticExceptionSimple {
        if (Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            this.encabezadoMetodo();
            BlockNode principalBlock = new BlockNode(null, null);
            SymbolTable.getInstance().getCurrentMethod().setPrincipalBlock(principalBlock);
            this.bloque(principalBlock);
        }
         else
            throw new SyntacticException(this.currentToken, "public, void, idClase, boolean, char o int");
    }

    private void encabezadoMetodo() throws LexicalException, IOException, SyntacticException, SemanticException {
        if (Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(this.currentToken.getTokenId())) {
            String staticMethod = this.estaticoOpt();
            Type methodType = this.tipoMetodo();
            Token methodToken = this.currentToken;
            String currentClassName = SymbolTable.getInstance().getCurrentClass().getClassName();
            Method method = new Method(methodToken, staticMethod, methodType, currentClassName);
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

    private BlockNode bloque(BlockNode blockNode) throws LexicalException, SyntacticException, IOException, SemanticExceptionSimple {
        BlockNode toReturn = blockNode;
        SymbolTable.getInstance().getCurrentMethod().setCurrentBlock(blockNode);
        if (this.currentToken.getTokenId().equals("{")) {
            this.match("{");
            this.listaSentencias();
            this.match("}");
            if (blockNode.getAncestorBlock() != null)
                SymbolTable.getInstance().getCurrentMethod().setCurrentBlock(blockNode.getAncestorBlock());
        } else
            throw new SyntacticException(this.currentToken, "{");
        return toReturn;
    }

    private SentenceNode sentenciaPrima(AccessNode accessNode) throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("=", "+=", "-=").contains(this.currentToken.getTokenId())) {
            Token tokenAsignacion = this.tipoAsignacion();
            ExpressionNode expressionNode = this.expresion();
            return new AssignmentNode(tokenAsignacion, accessNode, expressionNode);
        }
        else {
            //epsilon, no hago nada
            return new CallNode(accessNode.getToken(), accessNode);
        }
    }

    private void listaSentencias() throws LexicalException, SyntacticException, IOException, SemanticExceptionSimple {
        if (Arrays.asList(";", "idMV", "pr_this", "pr_new", "idClase", "(", "pr_return", "pr_if", "pr_while", "{", "pr_var").contains(this.currentToken.getTokenId())) {
            SentenceNode sentenceNode = this.sentencia();
            SymbolTable.getInstance().getCurrentMethod().getCurrentBlock().addSentence(sentenceNode);
            this.listaSentencias();
        } else {
            // epsilon, no hago nada
        }
    }

    private SentenceNode sentencia() throws LexicalException, SyntacticException, IOException, SemanticExceptionSimple {
        SentenceNode sentenceNode;
        if (this.currentToken.getTokenId().equals(";")) {
            sentenceNode = new EmptySentence(this.currentToken);
            this.match(";");
        }
        else if (Arrays.asList("idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            AccessNode accessNode = this.acceso();
            sentenceNode = this.sentenciaPrima(accessNode);
            this.match(";");
        } else if (this.currentToken.getTokenId().equals("pr_var")) {
            sentenceNode = this.varLocal();
            this.match(";");
        } else if (this.currentToken.getTokenId().equals("pr_return")) {
            sentenceNode = this.noTerminalReturn();
            this.match(";");
        } else if (this.currentToken.getTokenId().equals("pr_if"))
            sentenceNode = this.noTerminalIf();
        else if (this.currentToken.getTokenId().equals("pr_while"))
            sentenceNode = this.noTerminalWhile();
        else if (this.currentToken.getTokenId().equals("{")) {
            BlockNode currentBlock = SymbolTable.getInstance().getCurrentMethod().getCurrentBlock();
            BlockNode newBlock = new BlockNode(SymbolTable.getInstance().getCurrentMethod().getMethodToken(), currentBlock);
            sentenceNode = this.bloque(newBlock);
        }
        else
            throw new SyntacticException(this.currentToken, ";, this, new, idClase, (, var, return, if, while o {");
        return sentenceNode;
    }

    private Token tipoAsignacion() throws LexicalException, SyntacticException, IOException {
        Token asignacionToken;
        if (this.currentToken.getTokenId().equals("=")) {
            asignacionToken = this.currentToken;
            this.match("=");
        }
        else if (this.currentToken.getTokenId().equals("+=")) {
            asignacionToken = this.currentToken;
            this.match("+=");
        }
        else if (this.currentToken.getTokenId().equals("-=")) {
            asignacionToken = this.currentToken;
            this.match("-=");
        }
        else
            throw new SyntacticException(this.currentToken, "=, += o -=");
        return asignacionToken;
    }

    private LocalVarDeclarationNode varLocal() throws LexicalException, SyntacticException, IOException, SemanticExceptionSimple {
        if (this.currentToken.getTokenId().equals("pr_var")) {
            this.match("pr_var");
            Token varNodeToken = this.currentToken;
            this.match("idMV");
            Token operatorToken = this.currentToken;
            this.match("=");
            ExpressionNode expressionNode = this.expresion();
            LocalVarDeclarationNode varNode = new LocalVarDeclarationNode(varNodeToken, expressionNode, operatorToken);
            return varNode;
        } else
            throw new SyntacticException(this.currentToken, "var");
    }

    private ReturnNode noTerminalReturn() throws LexicalException, SyntacticException, IOException {
        ExpressionNode expressionNode;
        if (this.currentToken.getTokenId().equals("pr_return")) {
            Token returnToken = this.currentToken;
            this.match("pr_return");
            expressionNode = this.expresionOpt();
            return new ReturnNode(returnToken, expressionNode);
        } else
            throw new SyntacticException(this.currentToken, "return");
    }

    private ExpressionNode expresionOpt() throws SyntacticException, LexicalException, IOException {
        ExpressionNode expressionNode = null;
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId()))
            expressionNode =  this.expresion();
        else
            if (this.currentToken.getTokenId().equals(";"))
                expressionNode = new EmptyExpressionNode(this.currentToken);
        return expressionNode;
    }

    private IfNode noTerminalIf() throws LexicalException, SyntacticException, IOException, SemanticExceptionSimple {
        IfNode ifNode;
        if (this.currentToken.getTokenId().equals("pr_if")) {
            Token ifToken = this.currentToken;
            this.match("pr_if");
            this.match("(");
            ExpressionNode expressionNode = this.expresion();
            this.match(")");
            SentenceNode sentenceNode = this.sentencia();
            ifNode = new IfNode(ifToken, expressionNode, sentenceNode);
            SentenceNode elseSentence = this.noTerminalIfPrima();
            ifNode.setElseSentence(elseSentence);
        } else
            throw new SyntacticException(this.currentToken, "if");
        return ifNode;
    }

    private SentenceNode noTerminalIfPrima() throws LexicalException, SyntacticException, IOException, SemanticExceptionSimple {
        if (this.currentToken.getTokenId().equals("pr_else")) {
            this.match("pr_else");
            return this.sentencia();
        } else {
            return null;
        }
    }

    private WhileNode noTerminalWhile() throws LexicalException, SyntacticException, IOException, SemanticExceptionSimple {
        WhileNode whileNode;
        if (this.currentToken.getTokenId().equals("pr_while")) {
            Token whileToken = this.currentToken;
            this.match("pr_while");
            this.match("(");
            ExpressionNode expressionNode = this.expresion();
            this.match(")");
            SentenceNode sentenceNode = this.sentencia();
            whileNode = new WhileNode(whileToken, expressionNode, sentenceNode);
        } else
            throw new SyntacticException(this.currentToken, "while");
        return whileNode;
    }

    private ExpressionNode expresion() throws SyntacticException, LexicalException, IOException {
        ExpressionNode expressionToReturn;
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            ExpressionNode expressionNode = this.expresionUnaria();
            expressionToReturn = this.expresionPrima(expressionNode);
        }
        else
            throw new SyntacticException(this.currentToken, "+, -, !, null, true, false, intLiteral, charLiteral o stringLiteral");
        return expressionToReturn;
    }

    private ExpressionNode expresionPrima(ExpressionNode expressionLeftSide) throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%").contains(this.currentToken.getTokenId())) {
            Token binaryOperator = this.operadorBinario();
            ExpressionNode expressionRightSide = this.expresionUnaria();
            BinaryExpressionNode binaryExpressionNode = new BinaryExpressionNode(binaryOperator, expressionLeftSide, expressionRightSide);
            return this.expresionPrima(binaryExpressionNode);
        } else
            return expressionLeftSide;
    }

    private ExpressionNode expresionUnaria() throws SyntacticException, LexicalException, IOException {
        ExpressionNode expressionNode;
        if (Arrays.asList("+", "-", "!").contains(this.currentToken.getTokenId())) {
            Token operator = this.operadorUnario();
            OperandNode operandNode = this.operando();
            expressionNode = new UnaryExpressionNode(operator, operandNode);
        } else if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this", "idMV", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId()))
            expressionNode = this.operando();
        else
            throw new SyntacticException(this.currentToken, "+, -, !, null, true, false, intLiteral, charLiteral, stringLiteral, this, idMV, new, idClase o (");
        return expressionNode;
    }

    private Token operadorUnario() throws LexicalException, SyntacticException, IOException {
        Token operator;
        if (this.currentToken.getTokenId().equals("+")) {
            operator = this.currentToken;
            this.match("+");
        }
        else if (this.currentToken.getTokenId().equals("-")) {
            operator = this.currentToken;
            this.match("-");
        }
        else if (this.currentToken.getTokenId().equals("!")) {
            operator = this.currentToken;
            this.match("!");
        }
        else
            throw new SyntacticException(this.currentToken, "+, - o !");
        return operator;
    }

    private Token operadorBinario() throws LexicalException, SyntacticException, IOException {
        Token tokenToReturn;
        if (this.currentToken.getTokenId().equals("||")) {
            tokenToReturn = this.currentToken;
            this.match("||");
        }
        else if (this.currentToken.getTokenId().equals("&&")) {
            tokenToReturn = this.currentToken;
            this.match("&&");
        }
        else if (this.currentToken.getTokenId().equals("==")) {
            tokenToReturn = this.currentToken;
            this.match("==");
        }
        else if (this.currentToken.getTokenId().equals("!=")) {
            tokenToReturn = this.currentToken;
            this.match("!=");
        }
        else if (this.currentToken.getTokenId().equals("<")) {
            tokenToReturn = this.currentToken;
            this.match("<");
        }
        else if (this.currentToken.getTokenId().equals(">")) {
            tokenToReturn = this.currentToken;
            this.match(">");
        }
        else if (this.currentToken.getTokenId().equals("<=")) {
            tokenToReturn = this.currentToken;
            this.match("<=");
        }
        else if (this.currentToken.getTokenId().equals(">=")) {
            tokenToReturn = this.currentToken;
            this.match(">=");
        }
        else if (this.currentToken.getTokenId().equals("+")) {
            tokenToReturn = this.currentToken;
            this.match("+");
        }
        else if (this.currentToken.getTokenId().equals("-")) {
            tokenToReturn = this.currentToken;
            this.match("-");
        }
        else if (this.currentToken.getTokenId().equals("*")) {
            tokenToReturn = this.currentToken;
            this.match("*");
        }
        else if (this.currentToken.getTokenId().equals("/")) {
            tokenToReturn = this.currentToken;
            this.match("/");
        }
        else if (this.currentToken.getTokenId().equals("%")) {
            tokenToReturn = this.currentToken;
            this.match("%");
        }
        else
            throw new SyntacticException(this.currentToken, "+, -, *, /, %, >=, <=, >, <, !=, ==, && o ||");
        return tokenToReturn;
    }

    private OperandNode operando() throws SyntacticException, LexicalException, IOException {
        if (Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral").contains(this.currentToken.getTokenId()))
            return this.literal();
        else if (Arrays.asList("idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            return this.acceso();
        }
        else
            throw new SyntacticException(this.currentToken, "null, true, false, intLiteral, charLiteral, stringLiteral, idMV, this, new, idClase o (");
    }

    private LiteralOperandNode literal() throws SyntacticException, LexicalException, IOException {
        LiteralOperandNode literalOperandNode;
        if (this.currentToken.getTokenId().equals("pr_null")) {
            literalOperandNode = new NullNode(this.currentToken);
            this.match("pr_null");
        }
        else if (this.currentToken.getTokenId().equals("pr_true")) {
            literalOperandNode = new BooleanNode(this.currentToken);
            this.match("pr_true");
        }
        else if (this.currentToken.getTokenId().equals("pr_false")) {
            literalOperandNode = new BooleanNode(this.currentToken);
            this.match("pr_false");
        }
        else if (this.currentToken.getTokenId().equals("intLiteral")) {
            literalOperandNode = new IntNode(this.currentToken);
            this.match("intLiteral");
        }
        else if (this.currentToken.getTokenId().equals("charLiteral")) {
            literalOperandNode = new CharNode(this.currentToken);
            this.match("charLiteral");
        }
        else if (this.currentToken.getTokenId().equals("stringLiteral")) {
            literalOperandNode = new StringNode(this.currentToken);
            this.match("stringLiteral");
        }
        else
            throw new SyntacticException(this.currentToken, "null, true, false, intLiteral, charLiteral o stringLiteral");
        return literalOperandNode;
    }

    private AccessNode acceso() throws SyntacticException, LexicalException, IOException {
        AccessNode primarioAccessNode;
        if (Arrays.asList("pr_this", "idMV", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            primarioAccessNode = this.primario();
            Encadenado encadenado = this.encadenadoOpt(null);
            primarioAccessNode.setEncadenado(encadenado);
        } else
            throw new SyntacticException(this.currentToken, "this, idMV, new, idClase o (");
        return primarioAccessNode;
    }

    private AccessNode primario() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("idMV")) {
            Token idMVToken = this.currentToken;
            this.match("idMV");
            return this.primarioPrima(idMVToken);
        } else if (this.currentToken.getTokenId().equals("pr_this"))
            return this.accesoThis();
        else if (this.currentToken.getTokenId().equals("pr_new"))
            return this.accesoConstructor();
        else if (this.currentToken.getTokenId().equals("idClase"))
            return this.accesoMetodoEstatico();
        else if (this.currentToken.getTokenId().equals("("))
            return this.expresionParentizada();
        else
            throw new SyntacticException(this.currentToken, "this, idMV, pnew, idClase o (");
    }

    private AccessNode primarioPrima(Token idMVToken) throws LexicalException, SyntacticException, IOException {
        AccessNode accessNodeToReturn;
        if (this.currentToken.getTokenId().equals("(")) {
            ArrayList<ExpressionNode> expressionNodesList = this.argsActuales();
            accessNodeToReturn = new MethodAccess(idMVToken, expressionNodesList);
            accessNodeToReturn.setIsNotAssignable();
        }
        else
             accessNodeToReturn = new VarAccessNode(idMVToken);
        return accessNodeToReturn;
    }

    private ThisAccessNode accesoThis() throws LexicalException, SyntacticException, IOException {
        ThisAccessNode thisAccessNode;
        if (this.currentToken.getTokenId().equals("pr_this")) {
            thisAccessNode = new ThisAccessNode(this.currentToken, SymbolTable.getInstance().getCurrentClass().getClassName());
            this.match("pr_this");
        }
        else
            throw new SyntacticException(this.currentToken, "this");
        return thisAccessNode;
    }

    private ConstructorAccess accesoConstructor() throws LexicalException, SyntacticException, IOException {
        ConstructorAccess constructorAccess;
        if (this.currentToken.getTokenId().equals("pr_new")) {
            this.match("pr_new");
            constructorAccess = new ConstructorAccess(this.currentToken);
            constructorAccess.setIsNotAssignable();
            this.match("idClase");
            this.match("(");
            this.match(")");
        }
        else
            throw new SyntacticException(this.currentToken, "new");
        return constructorAccess;
    }

    private ParenthesizedExpressionNode expresionParentizada() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("(")) {
            this.match("(");
            ExpressionNode expression = this.expresion();
            this.match(")");
            ParenthesizedExpressionNode parenthesizedExpressionNode = new ParenthesizedExpressionNode(null, expression);
            parenthesizedExpressionNode.setIsNotAssignable();
            return parenthesizedExpressionNode;
        } else
            throw new SyntacticException(this.currentToken, "(");
    }

    private StaticMethodAccessNode accesoMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals("idClase")) {
            Token classNameToken = this.currentToken;
            this.match("idClase");
            this.match(".");
            Token methodNameToken = this.currentToken;
            this.match("idMV");
            ArrayList<ExpressionNode> expressionNodesList = this.argsActuales();
            StaticMethodAccessNode staticMethodAccessNode = new StaticMethodAccessNode(classNameToken, methodNameToken, expressionNodesList);
            staticMethodAccessNode.setIsNotAssignable();
            return staticMethodAccessNode;
        } else
            throw new SyntacticException(this.currentToken, "idClase");
    }

    private ArrayList<ExpressionNode> argsActuales() throws LexicalException, SyntacticException, IOException {
        ArrayList<ExpressionNode> expressionNodesList = new ArrayList<>();
        if (this.currentToken.getTokenId().equals("(")) {
            this.match("(");
            expressionNodesList = this.listaExpsOpt(expressionNodesList);
            this.match(")");
            return expressionNodesList;
        } else
            throw new SyntacticException(this.currentToken, "(");
    }

    private ArrayList<ExpressionNode> listaExpsOpt(ArrayList<ExpressionNode> expressionNodesList) throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId()))
            return this.listaExps(expressionNodesList);
        else {
            // epsilon, no hago nada
            return null;
        }
    }

    private ArrayList<ExpressionNode> listaExps(ArrayList<ExpressionNode> expressionNodesList) throws LexicalException, SyntacticException, IOException {
        if (Arrays.asList("+", "-", "!", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "idMV", "pr_this", "pr_new", "idClase", "(").contains(this.currentToken.getTokenId())) {
            ExpressionNode expressionNode = this.expresion();
            expressionNodesList.add(expressionNode);
            return this.listaExpsPrima(expressionNodesList);
        }
        else
            throw new SyntacticException(this.currentToken, "+, -, !, null, true, false, intLiteral, charLiteral, stringLiteral, idMV, this, new, idClase, (");
    }

    private ArrayList<ExpressionNode> listaExpsPrima(ArrayList<ExpressionNode> expressionNodesList) throws LexicalException, SyntacticException, IOException {
        if (this.currentToken.getTokenId().equals(",")) {
            this.match(",");
            return this.listaExps(expressionNodesList);
        } else {
            return expressionNodesList;
            // epsilon, no hago nada
        }
    }

    private Encadenado encadenadoOpt(Encadenado encadenadoParametro) throws LexicalException, SyntacticException, IOException {
        Encadenado encadenado;
        if (this.currentToken.getTokenId().equals(".")) {
            this.match(".");
            Token encadenadoToken = this.currentToken;
            this.match("idMV");
            encadenado = this.encadenadoOptPrima(encadenadoToken);
            if (encadenado == null)
                encadenado = new VarEncadenada(encadenadoToken);
            if (encadenadoParametro != null)
                encadenadoParametro.setEncadenado(encadenado);
        }
        else
            return null;
        return encadenado;
    }

    private Encadenado encadenadoOptPrima(Token encadenadoToken) throws LexicalException, SyntacticException, IOException {
        Encadenado encadenado;
        if (this.currentToken.getTokenId().equals(".")) {
            encadenado = new VarEncadenada(encadenadoToken);
            this.encadenadoOpt(encadenado);
            return encadenado;
        }
        else
            if (this.currentToken.getTokenId().equals("(")) {
                ArrayList<ExpressionNode> expressionNodesList = this.argsActuales();
                encadenado = new LlamadaEncadenada(encadenadoToken, expressionNodesList);
                encadenado.setIsNotAssignable();
                this.encadenadoOpt(encadenado);
                return encadenado;
            }
            else
                // epsilon,  no hago nada
                return null;
    }

}