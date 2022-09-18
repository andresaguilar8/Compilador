package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.Hashtable;

public class ConcreteClass extends Class {

    private String ancestorName;
    private Hashtable<String, Atribute> atributes;

    public ConcreteClass(Token classToken, String ancestorName) {
        super(classToken);
        this.ancestorName = ancestorName;
        this.atributes = new Hashtable<>();
    }

    public void setAncestor(String ancestorName) {
        this.ancestorName = ancestorName;
    }

    public void insertAtribute(Atribute atributeToInsert) {
        this.atributes.put(atributeToInsert.getAtributeName(), atributeToInsert);
    }

//    public Method getMethod(String methodName) {
//        return this.classMethods.get(methodName);
//    }

    //    public void insertMethod(Method methodToInsert) {
//        this.classMethods.put(methodToInsert.getMethodName(), methodToInsert);
//    }

}

