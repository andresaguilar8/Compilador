package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.Arrays;

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
        return Arrays.asList("=", "==", "!=").contains(operator);
    }

    @Override
    public void setClassName(Token tokenType) {

    }

    public boolean isCompatibleWithType(Type typeToCompareWith) {
        System.out.println("comparo " + this.getClassName() + " con " + typeToCompareWith.getClassName());
        if (typeToCompareWith.isPrimitive())
            return false;
        if (this.tokenType.getLexeme().equals("null") || typeToCompareWith.getClassName().equals("null"))
            return true;
        if (this.tokenType.getLexeme().equals(typeToCompareWith.getClassName()))
            return true;
        if (SymbolTable.getInstance().interfaceIsDeclared(typeToCompareWith.getClassName())) {
            if (SymbolTable.getInstance().concreteClassIsDeclared(this.getClassName())) {
                ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.getClassName());
                if (concreteClass.hasAncestorInterface(typeToCompareWith.getClassName()))
                    return true;
                else {
                    while (concreteClass.getAncestorClass() != null) {
                        concreteClass = concreteClass.getAncestorClass();
                        if (concreteClass.hasAncestorInterface(typeToCompareWith.getClassName()))
                            return true;
                    }
                }
            }
            //todo.. en caso de que el tipo de retorno sea una interface que onda
        }
        else {
            if (SymbolTable.getInstance().concreteClassIsDeclared(typeToCompareWith.getClassName())) {
                if (SymbolTable.getInstance().concreteClassIsDeclared(this.getClassName())) {
                    ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.getClassName());
                    while (concreteClass.getAncestorClass() != null) {
                        if (concreteClass.getAncestorClass().getClassName().equals(typeToCompareWith.getClassName()))
                            return true;
                        concreteClass = concreteClass.getAncestorClass();
                    }
                }
            }
        }
        return false;
    }

}
