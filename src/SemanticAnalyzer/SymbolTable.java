package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.ArrayList;
import java.util.Hashtable;

public class SymbolTable {

    private Class currentClass;
    private Method currentMethod;
    private static SymbolTable instance = null;
    private Hashtable<String, Class> classTable;

    public static SymbolTable getInstance() {
        if (instance == null)
            instance = new SymbolTable();
        return instance;
    }

    private SymbolTable() {
        this.classTable = new Hashtable<String, Class>();
        this.initObjectClass();
    }

    private void initObjectClass() {
        Token objectClassToken = new Token("idClase", "Object", 0);
        Token debugPrintMethodToken = new Token("idMV", "debugPrint", 0);
        Token voidToken = new Token("pr_void", "void", 0);
        Token intToken = new Token("pr_int", "int", 0);
        Token parameterToken = new Token("idMV", "i", 0);

        Type debugPrintMethodType = new PrimitiveType(voidToken);
        Type debugPrintMethodParameterType = new PrimitiveType(intToken);

        Method debugPrintMethod = new Method(debugPrintMethodToken, "", debugPrintMethodType, "Object");
        Parameter methodParameter = new Parameter(parameterToken, debugPrintMethodParameterType);
        Class objectClass = new ConcreteClass(objectClassToken, null);

        try {
            debugPrintMethod.insertParameter(methodParameter);
            objectClass.insertMethod(debugPrintMethod);
            this.classTable.put(objectClass.getClassName(), objectClass);
        } catch (SemanticException exception) {
            exception.getMessage();
        }

    }

    public void insertClass(Class classToInsert) throws SemanticException {
        if (!this.classTable.containsKey(classToInsert.getClassName())) {
            this.classTable.put(classToInsert.getClassName(), classToInsert);
        }
        else
            throw new SemanticException(classToInsert.getClassToken(), "La clase " + classToInsert.getClassName() + " ya esta declarada");
    }

    public Class getCurrentClass() {
        return this.currentClass;
    }

    public void setActualClass(Class currentClass) {
        this.currentClass = currentClass;
    }

    public void setCurrentMethod(Method currentMethod) {
        this.currentMethod = currentMethod;
    }

    public Method getCurrentMethod() {
        return this.currentMethod;
    }

    public Hashtable<String, Class> getClassTable() {
        return this.classTable;
    }

    public void emptySymbolTable() {
        this.classTable = new Hashtable<String, Class>();
    }

    public boolean classIsDeclared(String className) {
        return SymbolTable.getInstance().getClassTable().containsKey(className);
    }

    public Class getClass(String className) {
        return SymbolTable.getInstance().getClassTable().get(className);
    }

    public void checkDeclaration() throws SemanticException {
        for (Class classToCheck: this.classTable.values()) {
            if (!classToCheck.getClassName().equals("Object"))
                classToCheck.checkDeclaration();
        }
    }

    public void consolidate() {

    }

    public void imprimir() {
        for (Class clase : this.classTable.values()) {
            System.out.println();
            System.out.println("class name: " + clase.getClassName());
            if (clase.hasExplicitInheritance())
                System.out.println("ancestor: "+clase.getAncestorClassName());
            else {
                System.out.print("clases que la interfaz extiende: ");
                for (String extendsClasses : clase.getLista()) {
                    System.out.print(extendsClasses + " ");
                }
                System.out.println();
            }
            System.out.println("methods:");
            Hashtable<String, Method> metodos = clase.getClassMethods();
            for (Method metodo : metodos.values()) {
                System.out.println("method name: " + metodo.getMethodName());
                System.out.println("method return type: " + metodo.getReturnType());
                ArrayList<Parameter> parametersList = metodo.getParametersList();
                for (Parameter parameter: parametersList) {
                    System.out.print("parameter name: "+parameter.getParameterName());
                    System.out.print(", parameter type: "+parameter.getParameterType().toString());
                    System.out.println();
                }
            }
        }
    }

}
