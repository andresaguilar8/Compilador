package AST.Access;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

import java.util.ArrayList;

public class LlamadaEncadenada extends Encadenado {

    private ArrayList<ExpressionNode> expressionNodesList;

    public LlamadaEncadenada(Token token, ArrayList<ExpressionNode> expressionNodesList) {
        super(token);
        this.expressionNodesList = expressionNodesList;
    }

    @Override
    public Type check(Type leftSideType) throws SemanticExceptionSimple {
        //todo el chequeo de que la clase leftsidetype exista donde va?
        Type accessMethodType;
        //todo verificar con tipo void que ande
        if (!leftSideType.isPrimitive()) {
            ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(leftSideType.getClassName());
            if (!concreteClass.getMethods().containsKey(this.token.getLexeme()))
                throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es un metodo clase " + concreteClass.getClassName());
            else {
                Method method = concreteClass.getMethods().get(this.token.getLexeme());
                if (method.getParametersList().size() > 0)
                    this.checkArguments(method);
                accessMethodType = method.getReturnType();
                if (accessMethodType.isPrimitive())
                    throw new SemanticExceptionSimple(this.token, "el metodo " + this.token.getLexeme() + " debe retornar un tipo que no sea int, boolean, char, ni void");
            }
        }
        else
            throw new SemanticExceptionSimple(leftSideType.getToken(), "el lado izquierdo de una  llamada encadenada no puede ser un tipo primitivo");
        return accessMethodType;
    }

    private void checkArguments(Method method) throws SemanticExceptionSimple {
        if (this.expressionNodesList == null || this.expressionNodesList.size() != method.getParametersList().size())
            throw new SemanticExceptionSimple(this.token, "metodo mal invocado, la cantidad de parametros es incorrecta");
        ArrayList<Parameter> parametersList = method.getParametersList();
        Type parameterType;
        Type expressionType;
        int index = 0;
        for (ExpressionNode expressionNode: this.expressionNodesList) {
            parameterType = parametersList.get(index).getParameterType();
            expressionType = expressionNode.check();
            index += 1;
            if (!parameterType.isCompatibleWithType(expressionType))
                throw new SemanticExceptionSimple(this.token, "tipos incompatibles en el pasaje de parametros");
        }
    }

    @Override
    public Type check() throws SemanticExceptionSimple {

        System.out.println("eekee");
        return null;
    }

    @Override
    public void printExpression() {
        if (this.encadenado == null)
            System.out.println("llamada encadenada con nombre: " + this.token.getLexeme());
        else {
            System.out.println("llamada encadenada con nombre: " + this.token.getLexeme());
            this.encadenado.printExpression();
        }

    }

    @Override
    public void setType() {

    }
}
