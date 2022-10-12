package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

public class VarEncadenada extends Encadenado {

    public VarEncadenada(Token token) {
        super(token);
        this.isAssignable = true;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        return null;
    }

    @Override
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public Type check(Type leftSideType) throws SemanticExceptionSimple {
        Type accessVarType;
        ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(leftSideType.getClassName());
        if (this.isAClassAttribute(concreteClass))
            accessVarType = concreteClass.getAttributes().get(this.token.getLexeme()).getAttributeType();
        else
            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable de instancia de la clase " + concreteClass.getClassName());
        if (this.encadenado == null)
            accessVarType = concreteClass.getAttributes().get(this.token.getLexeme()).getAttributeType();
        else {
            this.expressionType = accessVarType;
            accessVarType = this.encadenado.check(accessVarType);
            System.out.println("access var " + accessVarType.getClassName());
        }
        return accessVarType;
    }

    private boolean isAClassAttribute(ConcreteClass concreteClass) {
        return concreteClass.getAttributes().containsKey(this.token.getLexeme()) && concreteClass.getAttributes().get(this.token.getLexeme()).getVisibility().equals("public");
    }

    @Override
    public void printExpression() {
        if (this.encadenado == null)
            System.out.println("variable encadenada de nombre: " + this.token.getLexeme());
        else {
            System.out.println("variable encadenada de nombre: " + this.token.getLexeme());
            encadenado.printExpression();
        }
    }

    @Override
    public void setType() {

    }
}
