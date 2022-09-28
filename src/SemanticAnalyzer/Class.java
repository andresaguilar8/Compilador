package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.HashSet;
import java.util.Hashtable;

public abstract class Class {

    protected Token classToken;
    protected Hashtable<String, Method> methods;
    protected HashSet<Interface> interfaces;
    protected boolean consolidated;
    protected boolean hasCyclicInheritance;

    public Class(Token classToken) {
        this.classToken = classToken;
        this.methods = new Hashtable<>();
        this.interfaces = new HashSet<>();
        this.consolidated = false;
        this.hasCyclicInheritance = false;
    }

    public abstract void addAncestorInterface(Interface interfaceToAdd);

    protected boolean methodAlreadyExist(Method method) {
        return this.methods.containsKey(method.getMethodName());
    }

    public Method getMethod(String methodName) {
        return this.methods.get(methodName);
    }

    public Hashtable<String, Method> getMethods() {
        return this.methods;
    }

    public HashSet<Interface> getInterfaces() {
        return this.interfaces;
    }

    public String getClassName() {
        return this.classToken.getLexeme();
    }

    public Token getToken() {
        return this.classToken;
    }

    public void setConsolidated() {
        this.consolidated = true;
    }

    public abstract void insertMethod(Method methodToInsert) throws SemanticException;

    public abstract void consolidate() throws SemanticException;

    public abstract void checkDeclarations() throws SemanticException;

}
