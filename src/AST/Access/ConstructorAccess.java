package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import InstructionGenerator.InstructionGenerator;

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

        InstructionGenerator.getInstance().generateInstruction("RMEM 1         ; Retorno acceso constructor");
        InstructionGenerator.getInstance().generateInstruction("PUSH " + CIR_Size + "       ; Tamaño del CIR (cant atributos + 1)");
        InstructionGenerator.getInstance().generateInstruction("PUSH simple_malloc");
        InstructionGenerator.getInstance().generateInstruction("CALL            ; Se realiza la llamada a la rutina malloc");
        InstructionGenerator.getInstance().generateInstruction("DUP");
        InstructionGenerator.getInstance().generateInstruction("PUSH " + concreteClass.getVTLabel() + "       ; Se apila la dirección del comienzo de la virtual table");
        InstructionGenerator.getInstance().generateInstruction("STOREREF 0        ; Se guarda la referencia a la virtual table en el CIR creado (el offset es 0)" );
        InstructionGenerator.getInstance().generateInstruction("PUSH Constructor_" + this.token.getLexeme());
        InstructionGenerator.getInstance().generateInstruction("CALL");

        if (this.encadenado != null)
            encadenado.generateCode();
    }

}
