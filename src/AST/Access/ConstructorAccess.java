package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

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

}
