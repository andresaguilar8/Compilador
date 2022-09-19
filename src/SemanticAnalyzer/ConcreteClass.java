package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.Hashtable;

public class ConcreteClass extends Class {

    private Token ancestorToken;
    private Hashtable<String, Atribute> atributes;


    public ConcreteClass(Token classToken, Token ancestorToken) {
        super(classToken);
        this.atributes = new Hashtable<>();
        this.ancestorToken = ancestorToken;
    }

    public boolean hasExplicitInheritance() {
        return true;
    }

    public Hashtable<String, Atribute> getAtributes() {
        return this.atributes;
    }

    public void insertAtribute(Atribute atributeToInsert) throws SemanticException {
        if (!this.atributes.containsKey(atributeToInsert.getAtributeName()))
            this.atributes.put(atributeToInsert.getAtributeName(), atributeToInsert);
        else
            throw new SemanticException(atributeToInsert.getAtributeToken(), "El atributo " + atributeToInsert.getAtributeToken().getLexeme() + " ya esta declarado en la clase " + this.classToken.getLexeme());
    }

    public String getAncestorClassName() {
        return this.ancestorToken.getLexeme();
    }

    public void checkDeclaration() throws SemanticException {
        //if (hayHerenciaCIrcular)
        //throw excep

        if (!classIsDeclared(this.getAncestorClassName()))
            throw new SemanticException(this.ancestorToken, "La clase " + this.getAncestorClassName() + " no esta declarada");
        for (Atribute atributeToCheck: this.atributes.values())
            atributeToCheck.checkDeclaration(this.getAncestorClassName());
        for (Method methodToCheck: this.classMethods.values())
            methodToCheck.checkDeclaration(this.getAncestorClassName());
    }

    private boolean classIsDeclared(String className) {
        return SymbolTable.getInstance().classIsDeclared(className);
    }

    public boolean methodIsDeclared(String methodName) {
        return this.classMethods.containsKey(methodName);
    }
}

