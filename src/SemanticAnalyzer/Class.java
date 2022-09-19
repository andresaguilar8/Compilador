package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.HashSet;
import java.util.Hashtable;

public abstract class Class {

    protected Token classToken;
    protected Hashtable<String, Method> classMethods;
    private HashSet<String> interfacesOClases;


    public Class(Token classToken) {
        this.classToken = classToken;
        this.classMethods = new Hashtable<>();
        interfacesOClases = new HashSet<>();
    }

    public void insertMethod(Method methodToInsert) throws SemanticException {
        //todo entre interface y class va a cambiar este metodo, pq interface no puede tener metodos estaticos
        if (!methodAlreadyExist(methodToInsert))
            this.classMethods.put(methodToInsert.getMethodName(), methodToInsert);
        else
            throw new SemanticException(methodToInsert.getMethodToken(), "El metodo " + "\"" + methodToInsert.getMethodName() + "\"" + " ya esta declarado" + " en la clase " + this.getClassName());
    }

    private boolean methodAlreadyExist(Method method) {
        return this.classMethods.containsKey(method.getMethodName());
    }

    public Method getMethod(String methodName) {
        return this.classMethods.get(methodName);
    }

    public Hashtable<String, Method> getClassMethods() {
        return this.classMethods;
    }

    public HashSet<String> getLista() {
        return this.interfacesOClases;
    }

    public boolean hasExplicitInheritance() {
        return false;
    }

    public String getAncestorClassName() {
        return "";
    }

    public String getClassName() {
        return this.classToken.getLexeme();
    }

    public void insertAtribute(Atribute atribute) throws SemanticException {

    }

    public boolean methodIsDeclared(String methodName) {
        System.out.println("a");
        return this.classMethods.containsKey(methodName);
    }

    public Token getClassToken() {
        return this.classToken;
    }

    public void checkDeclaration() throws SemanticException {

    }
}
