package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.Hashtable;

public class SymbolTable {

    private Class currentClass;
    private Method currentMethod;
    private static SymbolTable instance = null;
    private Hashtable<String, ConcreteClass> concreteClassTable;
    private Hashtable<String, Interface> interfaceTable;
    private boolean mainMethodIsDeclared;

    public static SymbolTable getInstance() {
        if (instance == null)
            instance = new SymbolTable();
        return instance;
    }

    private SymbolTable() {
        this.concreteClassTable = new Hashtable<String, ConcreteClass>();
        this.interfaceTable = new Hashtable<String, Interface>();
        this.mainMethodIsDeclared = false;
        this.initPredefinedClasses();
    }

    public void insertConcreteClass(ConcreteClass classToInsert) throws SemanticException {
        if (!this.concreteClassTable.containsKey(classToInsert.getClassName()) && !this.interfaceTable.containsKey(classToInsert.getClassName())) {
            this.concreteClassTable.put(classToInsert.getClassName(), classToInsert);
        }
        else
            throw new SemanticException(classToInsert.getToken(), "El nombre " + classToInsert.getClassName() + " ya esta declarado");
    }

    public void insertInterface(Interface interfaceToInsert) throws SemanticException {
        if (!this.concreteClassTable.containsKey(interfaceToInsert.getClassName()) && !this.interfaceTable.containsKey(interfaceToInsert.getClassName())) {
            this.interfaceTable.put(interfaceToInsert.getClassName(), interfaceToInsert);
        }
        else
            throw new SemanticException(interfaceToInsert.getToken(), "El nombre " + interfaceToInsert.getClassName() + " ya esta declarado");
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

    public Hashtable<String, ConcreteClass> getConcreteClassTable() {
        return this.concreteClassTable;
    }

    public Hashtable<String, Interface> getInterfaceTable() {
        return this.interfaceTable;
    }

    public boolean classIsDeclared(String className) {
        return SymbolTable.getInstance().getConcreteClassTable().containsKey(className);
    }

    public boolean interfaceIsDeclared(String className) {
        return SymbolTable.getInstance().getInterfaceTable().containsKey(className);
    }

    public ConcreteClass getConcreteClass(String concreteClassName) {
        return SymbolTable.getInstance().getConcreteClassTable().get(concreteClassName);
    }

    public Interface getInterface(String interfaceName) {
        return SymbolTable.getInstance().getInterfaceTable().get(interfaceName);
    }

    public void checkDeclaration() throws SemanticException {
        for (Interface interfaceToCheck: this.interfaceTable.values())
            interfaceToCheck.checkDeclaration();

        for (ConcreteClass classToCheck: this.concreteClassTable.values()) {
                classToCheck.checkDeclaration();
                if (this.mainMethodIsDeclared == false)
                    this.checkForMainMethod(classToCheck);
        }

        //todo  cual seria la linea de token de error?
        if (!this.mainMethodIsDeclared)
            throw new SemanticException(new Token("idMV","main", 0), "No se encontro el metodo estatico main sin parametros declarado");
    }

    private void checkForMainMethod(ConcreteClass classToCheck) {
        for (Method methodToCheck: classToCheck.getMethods().values())
            if (methodToCheck.getStaticHeader().equals("static") && methodToCheck.getMethodName().equals("main") && !methodToCheck.hasParameters())
                this.mainMethodIsDeclared = true;
    }

    public void consolidate() throws SemanticException {
        for (Interface interfaceToConsolidate: this.interfaceTable.values())
            interfaceToConsolidate.consolidate();
        for (ConcreteClass classToConsolidate: this.concreteClassTable.values())
            classToConsolidate.consolidate();
    }

    private void initPredefinedClasses() {
        this.initObjectClass();
        this.initStringClass();
        this.initSystemClass();
        //todo iniciar clase system
    }

    private void initObjectClass() {
        Token objectClassToken = new Token("idClase", "Object", 0);
        Token debugPrintMethodToken = new Token("idMV", "debugPrint", 0);
        Token voidToken = new Token("pr_void", "void", 0);
        Token intToken = new Token("pr_int", "int", 0);
        Token parameterToken = new Token("idMV", "i", 0);

        Type debugPrintMethodType = new PrimitiveType(voidToken);
        Type debugPrintMethodParameterType = new PrimitiveType(intToken);

        Method debugPrintMethod = new Method(debugPrintMethodToken, "static", debugPrintMethodType);
        Parameter methodParameter = new Parameter(parameterToken, debugPrintMethodParameterType);
        ConcreteClass objectClass = new ConcreteClass(objectClassToken, null);
        objectClass.setConsolidated();

        try {
            debugPrintMethod.insertParameter(methodParameter);
            objectClass.insertMethod(debugPrintMethod);
            //todo preg el try catch
            this.concreteClassTable.put(objectClass.getClassName(), objectClass);
        } catch (SemanticException exception) {
            exception.getMessage();
        }

    }

    private void initStringClass() {
        Token stringClassToken = new Token("idClase", "String", 0);
        ConcreteClass ancestorClass = this.concreteClassTable.get("Object");
        Token ancestorClassToken = ancestorClass.getToken();

        ConcreteClass stringConcreteClass = new ConcreteClass(stringClassToken, ancestorClassToken);
        this.concreteClassTable.put(stringConcreteClass.getClassName(), stringConcreteClass);
    }

    private void initSystemClass() {
        Token systemClassToken = new Token("idClase", "System", 0);
        ConcreteClass ancestorClass = this.concreteClassTable.get("Object");
        Token ancestorClassToken = ancestorClass.getToken();

        ConcreteClass systemConcreteClass = new ConcreteClass(systemClassToken, ancestorClassToken);

    }

    public void emptySymbolTable() {
        this.concreteClassTable = new Hashtable<String, ConcreteClass>();
        this.interfaceTable = new Hashtable<String, Interface>();
        this.mainMethodIsDeclared = false;
        this.initPredefinedClasses();
    }

    public void imprimirTablaDeSimbolos() {
        for (Interface interfaceToPrint: this.interfaceTable.values()) {
            System.out.println();
            System.out.println("Interface: "+interfaceToPrint.getClassName());
            System.out.print("Metodos: ");
            for (Method method: interfaceToPrint.getMethods().values())
                System.out.print(method.getMethodName() + " ");
            System.out.println();
        }

        for (ConcreteClass concreteClass: this.concreteClassTable.values()) {
            System.out.println();
            System.out.println("Clase: "+concreteClass.getClassName());
            System.out.print("Atributos: ");
            for (Attribute attribute: concreteClass.getAttributes().values())
                System.out.print(attribute.getAttributeName() + ", ");
            System.out.println();
            System.out.print("Metodos: ");
            for (Method method: concreteClass.getMethods().values())
                System.out.print(method.getMethodName() + " ");
            System.out.println();
        }
    }

}
