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
        return operator.equals("=");
    }

    @Override
    public void setClassName(Token tokenType) {

    }

    public boolean isCompatibleWithType(Type rightSideAssignmentType) {
        System.out.println(this.getClassName());
        System.out.println(rightSideAssignmentType.getClassName());
        if (this.tokenType.getLexeme().equals("null") && rightSideAssignmentType.isPrimitive())
            return false;
        if (this.tokenType.getLexeme().equals("null") || rightSideAssignmentType.getClassName().equals("null"))
            return true;
        if (this.tokenType.getLexeme().equals(rightSideAssignmentType.getClassName()))
            return true;
        ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.getClassName());
        if (concreteClass.getAncestorClass().equals(rightSideAssignmentType.getClassName()))
            return true;
        else
            return concreteClass.getAncestorsInterfaces().contains(rightSideAssignmentType.getClassName());
    }
}
