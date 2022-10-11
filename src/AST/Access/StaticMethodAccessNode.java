package AST.Access;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.Parameter;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

import java.util.ArrayList;

public class StaticMethodAccessNode extends AccessNode {

    protected Token methodNameToken;
    protected ArrayList<ExpressionNode> arrayList;

    public StaticMethodAccessNode(Token classNameToken, Token methodNameToken, ArrayList<ExpressionNode> paramaterList) {
        super(classNameToken);
        this.methodNameToken = methodNameToken;
        this.arrayList = paramaterList;
    }

    @Override
    public void setEncadenado(Encadenado encadenado) {

    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        return null;
    }

    @Override
    public void printExpression() {

    }

    @Override
    public void setType() {

    }
}
