package AST.Access;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

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
    public Type check() throws SemanticExceptionSimple {
        ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.token.getLexeme());
        if (concreteClass == null)
            throw new SemanticExceptionSimple(this.token, "la clase " + this.token.getLexeme() + " no esta declarada");
        Method staticMethod = concreteClass.getMethod(this.methodNameToken.getLexeme());
        if (staticMethod == null)
            throw new SemanticExceptionSimple(this.methodNameToken, "El metodo " + this.methodNameToken.getLexeme() + " no esta declarado en la clase " + concreteClass.getClassName());
        if (!staticMethod.getStaticHeader().equals("static"))
            throw new SemanticExceptionSimple(this.methodNameToken, "El metodo " + this.methodNameToken.getLexeme() + " no tiene alcance estatico");
        Type staticMethodType = staticMethod.getReturnType();
        //todo chequear parametros del metodo con los de la llamada,

        return staticMethodType;
    }

    @Override
    public void printExpression() {

    }

    @Override
    public void setType() {

    }
}
