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

    @Override
    public boolean isCompatibleWithType(Type rightSideAssignmentType) {
        if (this.tokenType.getLexeme().equals(rightSideAssignmentType.getClassName()))
            return true;
        else {
            ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.getClassName());
            if (concreteClass.getAncestorClass().equals(rightSideAssignmentType.getClassName()))
                return true;
            else
                return concreteClass.getAncestorsInterfaces().contains(rightSideAssignmentType.getClassName());
        }
    }
}
