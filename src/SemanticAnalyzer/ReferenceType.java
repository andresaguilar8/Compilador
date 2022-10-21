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
        if (typeToCompareWith.isPrimitive())
            return false;
        if (this.tokenType.getLexeme().equals("null") || typeToCompareWith.getClassName().equals("null"))
            return true;
        if (this.tokenType.getLexeme().equals(typeToCompareWith.getClassName()))
            return true;
        ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.getClassName());
        Interface comparingInterface = SymbolTable.getInstance().getInterface(typeToCompareWith.getClassName());
        //comparo clase con interface
        if (concreteClass != null && comparingInterface != null) {
            System.out.println("ref: pregunto si " + concreteClass.getClassName() + " tiene un ancestro " + comparingInterface.getClassName());
            if (concreteClass.hasAncestorInterface(comparingInterface.getClassName()))
                return true;
            while (concreteClass.getAncestorClass() != null) {
                if (concreteClass.getAncestorClass() != null) {
                    System.out.println("ref: pregunto si " + concreteClass.getAncestorClass().getClassName() + " tiene un ancestro " + comparingInterface.getClassName());
                    if (concreteClass.getAncestorClass().hasAncestorInterface(comparingInterface.getClassName()))
                        return true;
                    concreteClass = concreteClass.getAncestorClass();
                }
            }
        }
        else {
            if (concreteClass == null) {
                Interface thisClassInterface = SymbolTable.getInstance().getInterface(this.getClassName());
                if (comparingInterface != null) {
                    //son dos interfaces las que comparo
                    if (thisClassInterface.hasAncestorInterface(comparingInterface.getClassName()))
                        return true;
                }
                else {
                    //comparo interfaz con clase
                    ConcreteClass comparingConcreteClass = SymbolTable.getInstance().getConcreteClass(typeToCompareWith.getClassName());
                    if (comparingConcreteClass.hasAncestorInterface(thisClassInterface.getClassName()))
                        return true;
                }
            }
            //comparo dos clases
            else {
                System.out.println("comparo clases");
                System.out.println("quiero ver si " + concreteClass.getClassName() + " tiene ancestro " + typeToCompareWith.getClassName());
                while (concreteClass.getAncestorClass() != null) {
                        if (concreteClass.getAncestorClass().getClassName().equals(typeToCompareWith.getClassName()))
                            return true;
                    concreteClass = concreteClass.getAncestorClass();
                }
            }
        }
//        else
//            if ()

//        if (SymbolTable.getInstance().interfaceIsDeclared(typeToCompareWith.getClassName())) {
//            if (SymbolTable.getInstance().concreteClassIsDeclared(this.getClassName())) {
//                ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.getClassName());
//                if (concreteClass.hasAncestorInterface(typeToCompareWith.getClassName()))
//                    return true;
//                else {
//                    while (concreteClass.getAncestorClass() != null) {
//                        concreteClass = concreteClass.getAncestorClass();
//                        if (concreteClass.hasAncestorInterface(typeToCompareWith.getClassName()))
//                            return true;
//                    }
//                }
//            }
//            //todo.. en caso de que el tipo de retorno sea una interface que onda
//        }
//        else {
//            if (SymbolTable.getInstance().concreteClassIsDeclared(typeToCompareWith.getClassName())) {
//                if (SymbolTable.getInstance().concreteClassIsDeclared(this.getClassName())) {
//                    ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.getClassName());
//                    while (concreteClass.getAncestorClass() != null) {
//                        if (concreteClass.getAncestorClass().getClassName().equals(typeToCompareWith.getClassName()))
//                            return true;
//                        concreteClass = concreteClass.getAncestorClass();
//                    }
//                }
//            }
//        }
        return false;
    }

}
