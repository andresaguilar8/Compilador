package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.ArrayList;

public class Interface extends Class {

    public Interface(Token interfaceToken) {
        super(interfaceToken);
    }

    public void insertMethod(Method methodToInsert) {
        if (methodToInsert.getStaticHeader().equals("static"))
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(methodToInsert.getMethodToken(), "Una interface no puede tener metodos estaticos"));
        if (!methodAlreadyExist(methodToInsert))
            this.methods.put(methodToInsert.getMethodName(), methodToInsert);
        else
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(methodToInsert.getMethodToken(), "El metodo " + "\"" + methodToInsert.getMethodName() + "\"" + " ya esta declarado" + " en la clase " + this.getClassName()));
    }

    public void checkRepeatedInterfaceImplementation(Interface otherInterface) {
        int total = 0;
        for (Interface interfaceToCheck : this.interfaces)
            if (interfaceToCheck.getClassName().equals(otherInterface.getClassName())) {
                total += 1;
                if (total > 1)
                    SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(otherInterface.getToken(), "La interface " + "\"" + this.getClassName() + "\"" + " extiende mas de una vez a una misma interface"));
            }
    }

    public void checkDeclarations() throws SemanticException {
        for (Interface interfaceToCheck : this.interfaces) {
            Token interfaceToken = interfaceToCheck.getToken();
            String interfaceToCheckName = interfaceToken.getLexeme();
            if (!this.interfaceIsDeclared(interfaceToCheckName))
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToken, "La interface " + interfaceToCheckName + " no esta declarada"));
            this.checkRepeatedInterfaceImplementation(interfaceToCheck);
        }
        this.checkMethodsDeclaration();
    }

    private void checkMethodsDeclaration() throws SemanticException {
        for (Method methodToCheck: this.methods.values())
            methodToCheck.checkDeclaration();
    }

    public void consolidate() throws SemanticException {
        if (!this.consolidated) {
            if (!this.hasCyclicInheritance)
                for (Interface interfaceToCheck: this.interfaces) {
                    if (!interfaceToCheck.isConsolidated())
                        interfaceToCheck.consolidate();
                    Interface interfaceInSymbolTable = SymbolTable.getInstance().getInterface(interfaceToCheck.getClassName());
                    if (interfaceInSymbolTable != null) {
                        this.consolidateMethods(interfaceInSymbolTable);
                        this.checkCyclicInheritance();
                    }
                    this.consolidated = true;
                }
        }
    }

    private void checkCyclicInheritance() {
        ArrayList<String> ancestorsList = new ArrayList<>();
        ancestorsList.add(this.getClassName());
        for (Interface ancestorInterface: this.interfaces) {
            Token ancestorToken = ancestorInterface.getToken();
            Interface interfaceInSymbolTable = SymbolTable.getInstance().getInterface(ancestorInterface.getClassName());
            if (interfaceInSymbolTable != null)
                if (interfaceInSymbolTable.hasCyclicInheritance(ancestorsList, ancestorToken)) {
                    SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(ancestorInterface.classToken, "Herencia circular: la interface " + "\"" + this.getClassName() + "\"" + " se extiende a si misma"));
                    this.hasCyclicInheritance = true;
                }
        }
    }

    public boolean hasCyclicInheritance(ArrayList<String> ancestorsList, Token interfaceToken){
        if (!ancestorsList.contains(this.getClassName())) {
            ancestorsList.add(interfaceToken.getLexeme());
            for (Interface ancestorInterface: this.interfaces) {
                Token ancestorToken = ancestorInterface.getToken();
                Interface interfaceInSymbolTable = SymbolTable.getInstance().getInterface(ancestorInterface.getClassName());
                if (interfaceInSymbolTable != null)
                    if (interfaceInSymbolTable.hasCyclicInheritance(ancestorsList, ancestorToken))
                        return true;
            }
            ancestorsList.remove(interfaceToken.getLexeme());
        }
        else
            return true;
        return false;
    }

    private void consolidateMethods(Interface classToConsolidateWith) throws SemanticException {
        for (Method ancestorMethod: classToConsolidateWith.getMethods().values()) {
            String methodName = ancestorMethod.getMethodName();
            if (!this.getMethods().containsKey(methodName))
                this.insertMethod(ancestorMethod);
            else {
                Method thisClassMethod = this.getMethod(methodName);
                if (!thisClassMethod.correctRedefinedMethodHeader(ancestorMethod))
                    SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(thisClassMethod.getMethodToken(), "El metodo " + "\"" + thisClassMethod.getMethodName() + "\"" + " esta incorrectamente redefinido"));
            }
        }
    }

    private boolean interfaceIsDeclared(String interfaceName) {
        return SymbolTable.getInstance().interfaceIsDeclared(interfaceName);
    }

    public void verifyMethodsImplementation(Token interfaceToken, ConcreteClass concreteClassToCheck) {
        for (Method method : this.methods.values()) {
            if (concreteClassToCheck.getMethods().containsKey(method.getMethodName())) {
                String methodName = method.getMethodName();
                if (!method.methodsHeadersAreEquals(concreteClassToCheck.getMethod(methodName)))
                    SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(concreteClassToCheck.getMethod(method.getMethodName()).getMethodToken(), "El metodo " + "\"" + method.getMethodName() + "\"" + " no respeta el encabezado del metodo definido en la interface " +this.getClassName()));
            }
            else
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToken, "La clase " + concreteClassToCheck.getClassName() + " no implementa todos los metodos de la interface " + this.getClassName()));
        }

    }

    private boolean isConsolidated() {
        return this.consolidated;
    }

}
