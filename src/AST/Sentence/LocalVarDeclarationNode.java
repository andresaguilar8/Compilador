package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.Method;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.SymbolTable;
import SemanticAnalyzer.Type;

public class LocalVarDeclarationNode extends SentenceNode {

    private ExpressionNode expressionNode;
    private Type localVarType;
    private Token operatorToken;

    public LocalVarDeclarationNode(Token nodeToken, ExpressionNode expressionNode, Token operatorToken) {
        super(nodeToken);
        this.expressionNode = expressionNode;
        this.operatorToken = operatorToken;
    }

    @Override
    public void printSentence() {
        System.out.print("variable: " + this.token.getLexeme() + " con expresion: ");
        this.expressionNode.printExpression();
        System.out.println();
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        Method currentMethod = SymbolTable.getInstance().getCurrentMethod();
        if (!SymbolTable.getInstance().isMethodParameter(this.token.getLexeme(), currentMethod)) {
            Type localVarType = this.expressionNode.check();
            if (localVarType.getClassName().equals("null") || localVarType.getClassName().equals("void"))
                throw new SemanticExceptionSimple(this.operatorToken, "no se puede inferir el tipo de la variable");
            this.setType(localVarType);
            SymbolTable.getInstance().getCurrentBlock().insertLocalVar(this);
        }
        else
            throw new SemanticExceptionSimple(this.token, "el nombre para la variable ya esta utilizado en un parametro");
    }

    public Type getLocalVarType() {
        return this.localVarType;
    }

    public void setType(Type localVarType) {
        this.localVarType = localVarType;
    }

    public String getVarName() {
        return this.token.getLexeme();
    }

    public Token getVarToken() {
        return this.token;
    }
}
