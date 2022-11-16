package AST.Access;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import InstructionGenerator.InstructionGenerator;

import java.io.IOException;
import java.util.ArrayList;

public class MethodAccess extends AccessNode {

    ArrayList<ExpressionNode> expressionNodesList;
    private Method method;

    public MethodAccess(Token token, ArrayList<ExpressionNode> expressionNodesList) {
        super(token);
        this.expressionNodesList = expressionNodesList;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        ConcreteClass concreteClass = (ConcreteClass) SymbolTable.getInstance().getCurrentClass();
        if (!this.classContainsThisMethod(concreteClass))
            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es un metodo de la clase " + concreteClass.getClassName());
        this.method = concreteClass.getMethods().get(this.token.getLexeme());
        if (SymbolTable.getInstance().getCurrentMethod().getStaticHeader().equals("static") && !method.getStaticHeader().equals("static"))
            throw new SemanticExceptionSimple(this.token, "no se puede llamar a un metodo dinamico dentro de un metodo con alcance estatico");
        if (this.method.getParametersList().size() > 0)
            this.checkArguments(method);
        if (this.encadenado == null)
            return method.getReturnType();
        else
            if (!this.method.getReturnType().isPrimitive())
                return this.encadenado.check(this.method.getReturnType());
            else
                throw new SemanticExceptionSimple(this.token, "el metodo " + this.token.getLexeme() + " retorna un tipo primitivo y tiene un encadenado");
    }

    @Override
    public void generateCode() throws IOException {
        if (this.method.isStatic()) {
            this.generateCodeForStaticMethod();
        } else
            this.generateCodeForDynamicMethod();

        if (this.encadenado != null)
            encadenado.generateCode();
    }

    private void generateCodeForStaticMethod() throws IOException {
        if (!this.method.getReturnType().getClassName().equals("void"))
            InstructionGenerator.getInstance().generateInstruction("RMEM 1 ; Se reserva lugar para el valor de retorno del metodo");

        this.generateParametersCode();

        InstructionGenerator.getInstance().generateInstruction("PUSH " + this.method.getMethodLabel());
        InstructionGenerator.getInstance().generateInstruction("CALL");

    }

    private void generateCodeForDynamicMethod() throws IOException {
        InstructionGenerator.getInstance().generateInstruction("LOAD 3        ; Se apila el this");

        if (!this.method.getReturnType().getClassName().equals("void")) {
            InstructionGenerator.getInstance().generateInstruction("RMEM 1 ; Se reserva lugar para el valor de retorno del metodo");
            InstructionGenerator.getInstance().generateInstruction("SWAP");
        }

        this.generateParametersCode();

        InstructionGenerator.getInstance().generateInstruction("DUP ; Se duplica el this porque al hacer LOADREF se pierde");
        InstructionGenerator.getInstance().generateInstruction("LOADREF 0 ; Se carga la VT");
        InstructionGenerator.getInstance().generateInstruction("LOADREF " + method.getOffset());
        InstructionGenerator.getInstance().generateInstruction("CALL");
    }

    private void generateParametersCode() throws IOException {
        if (this.expressionNodesList != null)
            for (int index = this.expressionNodesList.size() - 1; index >= 0; index--) {
                this.expressionNodesList.get(index).generateCode();  //genero codigo de cada parametro
                if (!this.method.isStatic())
                    InstructionGenerator.getInstance().generateInstruction("SWAP");  //para que el this quede en el tope
            }
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
            if (!expressionType.isCompatibleWithType(parameterType))
                throw new SemanticExceptionSimple(this.token, "tipos incompatibles en el pasaje de parametros");
        }
    }

    private boolean classContainsThisMethod(ConcreteClass concreteClass) {
        return concreteClass.getMethods().containsKey(this.token.getLexeme());
    }

    @Override
    public boolean isAssignable() {
        return this.encadenado != null;
    }

    @Override
    public boolean isCallable() {
        return true;
    }
}
