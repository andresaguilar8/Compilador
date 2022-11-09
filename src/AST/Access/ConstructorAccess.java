package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import Traductor.Traductor;

import java.io.IOException;

public class ConstructorAccess extends AccessNode {

    public ConstructorAccess(Token token) {
        super(token);
    }

    @Override
    public boolean isAssignable() {
        return false;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        Type constructorType;
        if (this.encadenado == null) {
            ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.token.getLexeme());
            if (concreteClass != null) {
                if (!concreteClass.getClassConstructor().getConstructorToken().getLexeme().equals(this.token.getLexeme()))
                    throw new SemanticExceptionSimple(this.token, " no es un constructor de la clase " + this.token.getLexeme());
                else
                    constructorType = new ReferenceType(this.token);
            }
            else
                throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una clase concreta declarada");
        }
        else
            if (SymbolTable.getInstance().concreteClassIsDeclared(this.token.getLexeme()))
                return this.encadenado.check(new ReferenceType(this.token));
            else
                throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una clase concreta declarada");
        return constructorType;
    }

    @Override
    public void generateCode() throws IOException {
        ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.token.getLexeme());
        int CIR_Size = concreteClass.getCirSize();

        Traductor.getInstance().gen("RMEM 1");
        Traductor.getInstance().gen("PUSH " + CIR_Size + "       ; Tamaño del CIR (cant atributos + 1)");
        Traductor.getInstance().gen("PUSH simple_malloc");
        Traductor.getInstance().gen("CALL");
        Traductor.getInstance().gen("DUP");
        Traductor.getInstance().gen("PUSH " + concreteClass.getVTLabel() + "       ; Se apila la dirección del comienzo de la virtual table");
        Traductor.getInstance().gen("STOREREF 0        ; Se guarda la referencia a la virtual table en el CIR creado (el offset es 0)" );
        //Traductor.getInstance().gen("DUP");
        //todo generar codigo argumentos e ir haciendo swap
        Traductor.getInstance().gen("PUSH Constructor_" + this.token.getLexeme());
        Traductor.getInstance().gen("CALL");

        if (this.encadenado != null)
            encadenado.generateCode();
    }

}
