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
        //todo si tengo a1.m2(); y a1 es de tipo interfaz, tengo que tirar error..
        if (!leftSideType.isPrimitive()) {
            ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(leftSideType.getClassName());
            //si no es una clase es una interfaz (va a estar chequeado que esté declarada)
            if (concreteClass == null)
                throw new SemanticExceptionSimple(this.token, "un atributo de tipo interfaz no puede acceder a un atributo");
            if (!SymbolTable.getInstance().isAttribute(this.token.getLexeme(), concreteClass))
                throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable de instancia de la clase " + concreteClass.getClassName());
            cadVarType = concreteClass.getAttributes().get(this.token.getLexeme()).getAttributeType();
            if (this.encadenado != null)
                if (!cadVarType.isPrimitive())
                    return this.encadenado.check(cadVarType);
                else
                    throw new SemanticExceptionSimple(this.token, "el lado izquierdo del encadenado retorna un tipo primitivo");
        }

//        if (this.encadenado != null && !leftSideType.isPrimitive())
//            return this.encadenado.check(cadVarType);
        else
            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " el lado izquierdo del encadenado retorna un tipo primitivo");
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
