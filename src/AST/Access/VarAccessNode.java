package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

public class VarAccessNode extends AccessNode {

    protected Method varMethod;
    protected ConcreteClass concreteClass;
    protected AccessNode encadenado;

    public VarAccessNode(Token token, ConcreteClass concreteClass, Method varMethod) {
        //todo no es necesario tener la clase actual aca, se la puedo pedir a la tabla de simbolos
        super(token);
        this.concreteClass = concreteClass;
        this.varMethod = varMethod;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        Type varType;
        String varName = this.token.getLexeme();
        if (SymbolTable.getInstance().isMethodParameter(varName, this.varMethod))
            varType = SymbolTable.getInstance().retrieveParameterType(varName, varMethod);
        else
            if (SymbolTable.getInstance().isLocalVar(varName, varMethod)) {
                System.out.println("aca");
                varType = SymbolTable.getInstance().retrieveLocalVarType(varName, varMethod);
            }
            else
                if (SymbolTable.getInstance().isAttribute(varName, concreteClass))
                    varType = SymbolTable.getInstance().retrieveAttribute(varName, concreteClass);
                else
                    throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable local ni un parametro del metodo " + this.varMethod.getMethodName() + " ni un atributo de la clase " + this.concreteClass.getClassName());
        return varType;
    }

    @Override
    public void printExpression() {
        System.out.print(this.token.getLexeme());
    }

    @Override
    public void setType() {

    }

    @Override
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }
}
