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

    public Type getAtributeType() {
        return this.atributeType;
    }

    public void checkDeclaration(String ancestorClassName) throws SemanticException {
        //si el atributo no es primitivo hay que verificar que la clase del atributo  este declarada sino --> error
        if (!this.atributeType.isPrimitive() && !referenceTypeExist(this.atributeType.getTypeName()))
            throw new SemanticException(this.atributeToken, "El tipo " + this.atributeType.getTypeName() + " no esta declarado");
    }

    private boolean referenceTypeExist(String className) {
        return SymbolTable.getInstance().classIsDeclared(className);
    }

}
