package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class ReferenceType extends Type {

    public ReferenceType(Token tokenType) {
        super(tokenType);
    }

    @Override
    public String getClassName() {
        return this.tokenType.getLexeme();
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isCompatibleWithOperator(String operator) {
        return false;
    }

    @Override
    public void setClassName(Token tokenType) {

    }

    @Override
    public boolean isCompatibleWithType(Type rightSideAssignmentType) {
        return false;
    }

}
