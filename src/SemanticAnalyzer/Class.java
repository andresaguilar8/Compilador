package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.HashSet;
import java.util.Hashtable;

public abstract class Class {

    private Token classToken;
    private Hashtable<String, Method> classMethods;
    private HashSet<String> interfacesOClases;


    public Class(Token classToken) {
        this.classToken = classToken;
        this.classMethods = new Hashtable<>();
        interfacesOClases = new HashSet<>();
    }

    public void insertMethod(Method methodToInsert) {
        this.classMethods.put(methodToInsert.getMethodName(), methodToInsert);
    }

    public Method getMethod(String methodName) {
        return this.classMethods.get(methodName);
    }

    public HashSet<String> getLista() {
        return this.interfacesOClases;
    }

    public void setAncestor(String ancestorName) {

    }

    public String getClassName() {
        return this.classToken.getLexeme();
    }

    public void insertAtribute(Atribute atribute) {

    }
}
