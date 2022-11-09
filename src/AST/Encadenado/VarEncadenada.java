package AST.Encadenado;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import Traductor.Traductor;

import java.io.IOException;

public class VarEncadenada extends Encadenado {

    protected Attribute attribute;

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
        //si no es una clase es una interfaz (va a estar chequeado que esté declarada)
        if (concreteClass == null)
            throw new SemanticExceptionSimple(this.token, "una interfaz no tiene atributos");
        if (!SymbolTable.getInstance().isAttribute(this.token.getLexeme(), concreteClass))
            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable de instancia de la clase " + concreteClass.getClassName());
        else {
            this.attribute = concreteClass.getAttributes().get(this.token.getLexeme());
            if (this.attribute.isInherited())
                if (!SymbolTable.getInstance().isPublicAttribute(this.token.getLexeme(), concreteClass))
                    throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " tiene visibilidad privada y es un atributo heredado");
        }
        if (!SymbolTable.getInstance().getCurrentClass().getClassName().equals(concreteClass.getClassName()))
            if (concreteClass.getAttributes().get(this.token.getLexeme()).getVisibility().equals("private"))
                throw new SemanticExceptionSimple(this.token,  "la variable de instancia " + this.token.getLexeme() + " tiene visibilidad privada");
        cadVarType = concreteClass.getAttributes().get(this.token.getLexeme()).getAttributeType();
        if (this.encadenado != null)
            if (!cadVarType.isPrimitive())
                return this.encadenado.check(cadVarType);
            else
                throw new SemanticExceptionSimple(this.token, "la variable encadenada " +this.token.getLexeme() + " es de tipo primitivo y tiene un encadenado");
        return cadVarType;
    }

    @Override
    public boolean isAssignable() {
        return true;
    }

    @Override
    public boolean isCallable() {
        return false;
    }

    @Override
    public void generateCode() throws IOException {
        if (!this.isLeftSide || this.encadenado != null) {
            Traductor.getInstance().gen("LOADREF " + this.attribute.getOffset() + "       ; Se apila el valor del atributo de instancia " + this.attribute.getAttributeName());
        }
        else {
            Traductor.getInstance().gen("SWAP");
            Traductor.getInstance().gen("STOREREF " + this.attribute.getOffset() + "      ; Se guarda el valor en el atributo");
        }

        if (this.encadenado != null) {
            this.encadenado.generateCode();
        }
    }


}
