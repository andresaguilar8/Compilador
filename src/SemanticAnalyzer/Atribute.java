package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class Atribute {

    private String visibility;
    private Token atributeToken;
    private Type atributeType;

    public Atribute(Token atributeToken, Type atributeType, String visibility) {
        this.atributeToken = atributeToken;
        this.atributeType = atributeType;
        this.visibility = visibility;
    }

    public String getAtributeName() {
        return this.atributeToken.getLexeme();
    }

    public Token getAtributeToken() {
        return this.atributeToken;
    }

    public void checkDeclaration(String ancestorClassName) throws SemanticException {
        if (!this.atributeType.isPrimitive() && !referenceTypeExist(this.atributeType.getTypeName()))
            throw new SemanticException(this.atributeToken, "El tipo " + this.atributeType.getTypeName() + " no esta declarado");
        if (ancestorClassHasSameAtribute(ancestorClassName))
            throw new SemanticException(this.atributeToken, "");
    }

    private boolean referenceTypeExist(String className) {
        return SymbolTable.getInstance().classIsDeclared(className);
    }

    private boolean ancestorClassHasSameAtribute(String ancestorClassName) {
        //todo chequear
        ConcreteClass ancestorClass = (ConcreteClass) SymbolTable.getInstance().getClass(ancestorClassName);
        boolean hasSameAtribute = false;
        //creo que una clase peude heredar atributos de mucho mas arriba.. capaz que una clase tendria un hash de ancestros
    }

}
