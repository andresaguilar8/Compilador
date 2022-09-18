package SemanticAnalyzer;

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
    }

    public void insertClass(Class classToInsert) throws SemanticException {
        if (!this.classTable.contains(classToInsert.getClassName()))
            this.classTable.put(classToInsert.getClassName(), classToInsert);
        else
            throw new SemanticException();
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

    public void imprimir() {
        System.out.println(this.classTable.keys());
    }

    //metodo actual
    //clase actual
    //para saber en que entrada tenemos que insertar la entrada de la declaracion actual
}
