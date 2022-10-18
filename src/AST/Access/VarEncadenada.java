package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

public class VarEncadenada extends Encadenado {

    public VarEncadenada(Token token) {
        super(token);
        this.isAssignable = true;
    }

    @Override
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public Type check(Type leftSideType) throws SemanticExceptionSimple {
        Type cadVarType;
        ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(leftSideType.getClassName());
        if (!SymbolTable.getInstance().isAttribute(this.token.getLexeme(), concreteClass))
            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable de instancia de la clase " + concreteClass.getClassName());
        cadVarType = concreteClass.getAttributes().get(this.token.getLexeme()).getAttributeType();
        if (this.encadenado != null)
            return this.encadenado.check(cadVarType);
        return cadVarType;
    }

    @Override
    public void printExpression() {
        System.out.println(" var encadenada");
    }

    @Override
    public boolean isAssignable() {
        return true;
    }

}
