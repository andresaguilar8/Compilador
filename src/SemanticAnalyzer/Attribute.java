package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class Attribute {

    private String visibility;
    private Token attributeToken;
    private Type attributeType;
    private boolean isInherited;
    private int offset;

    public Attribute(Token attributeToken, Type attributeType, String visibility) {
        this.attributeToken = attributeToken;
        this.attributeType = attributeType;
        this.visibility = visibility;
        this.isInherited = false;
    }

    public String getAttributeName() {
        return this.attributeToken.getLexeme();
    }

    public Token getAttributeToken() {
        return this.attributeToken;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void checkDeclaration() {
        if (!this.attributeType.isPrimitive() && !referenceTypeExist(this.attributeType.getClassName()))
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(this.attributeType.getToken(), "El tipo " + this.attributeType.getClassName() + " no esta declarado"));
    }

    private boolean referenceTypeExist(String className) {
        return SymbolTable.getInstance().concreteClassIsDeclared(className) || SymbolTable.getInstance().interfaceIsDeclared(className);
    }

    public Type getAttributeType() {
        return this.attributeType;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public void setInherited() {
        this.isInherited = true;
    }

    public boolean isInherited() {
        return this.isInherited;
    }

    public int getOffset() {
        return this.offset;
    }
}
