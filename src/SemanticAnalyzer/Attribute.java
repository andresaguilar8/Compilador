package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class Attribute {

    private String visibility;
    private Token attributeToken;
    private Type attributeType;

    public Attribute(Token attributeToken, Type attributeType, String visibility) {
        this.attributeToken = attributeToken;
        this.attributeType = attributeType;
        this.visibility = visibility;
    }

    public String getAttributeName() {
        return this.attributeToken.getLexeme();
    }

    public Token getAttributeToken() {
        return this.attributeToken;
    }

    public void checkDeclaration() {
        if (!this.attributeType.isPrimitive() && !referenceTypeExist(this.attributeType.getClassName()))
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(this.attributeType.getToken(), "El tipo " + this.attributeType.getClassName() + " no esta declarado"));
    }

    private boolean referenceTypeExist(String className) {
        return SymbolTable.getInstance().concreteClassIsDeclared(className) || SymbolTable.getInstance().interfaceIsDeclared(className);
    }

}
