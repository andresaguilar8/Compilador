package AST.Encadenado;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import SemanticAnalyzer.Class;
import Traductor.Traductor;

import java.io.IOException;
import java.util.ArrayList;

public class LlamadaEncadenada extends Encadenado {

    private ArrayList<ExpressionNode> expressionNodesList;
    private Method method;

    public LlamadaEncadenada(Token token, ArrayList<ExpressionNode> expressionNodesList) {
        super(token);
        this.expressionNodesList = expressionNodesList;
    }

    @Override
    public Type check(Type leftSideType) throws SemanticExceptionSimple {
        Type accessMethodType;
        Class classOrInterface = SymbolTable.getInstance().getClass(leftSideType.getClassName());
        if (!classOrInterface.getMethods().containsKey(this.token.getLexeme()))
            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es metodo un de " + classOrInterface.getClassName());
        else {
            method = classOrInterface.getMethods().get(this.token.getLexeme());
            if (method.getParametersList().size() > 0 || this.expressionNodesList != null)
                this.checkArguments(method);
            accessMethodType = method.getReturnType();
            if (this.encadenado != null) {
                if (accessMethodType.isPrimitive())
                    throw new SemanticExceptionSimple(this.token, "el metodo " + "\"" + this.token.getLexeme() + "\"" + " debe retornar un tipo no primitivo porque tiene un encadenado");
                return this.encadenado.check(accessMethodType);
            }
        }
        return accessMethodType;
    }

    public void generateCode() throws IOException {

        if (!method.getStaticHeader().equals("static")) {
            if (!method.getReturnType().getClassName().equals("void")) {
                Traductor.getInstance().gen("RMEM 1 ; Se reserva lugar para el valor de retorno del metodo");
                Traductor.getInstance().gen("SWAP 1");
            }

            //genero codigo de parametros
            if (this.expressionNodesList != null)
                for (ExpressionNode expressionNode: this.expressionNodesList) {
                    expressionNode.generateCode();  //genero codigo de cada parametro
                    Traductor.getInstance().gen("SWAP");
                }

            Traductor.getInstance().gen("DUP ; Se duplica el this porque al hacer LOADREF se pierde");
            Traductor.getInstance().gen("LOADREF 0 ; Se carga la VT");
            Traductor.getInstance().gen("LOADREF " + method.getOffset());
            System.out.println(method.getMethodName() + " offset: " + method.getOffset());
            Traductor.getInstance().gen("CALL");
    }
    else {
        //metodo es estatico
    }

        if (this.getEncadenado() != null)
            encadenado.generateCode();
    }

    @Override
    public boolean isAssignable() {
        return false;
    }

    @Override
    public boolean isCallable() {
        return true;
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
            System.out.println(parameterType.getClassName());
            System.out.println(expressionType.getClassName());
            if (!parameterType.getClassName().equals(expressionType.getClassName()))
                if (!parameterType.isCompatibleWithType(expressionType))
                    throw new SemanticExceptionSimple(this.token, "tipos incompatibles en el pasaje de parametros");
        }
    }

}
