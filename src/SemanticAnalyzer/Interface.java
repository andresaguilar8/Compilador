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

    public void addAncestorInterface(Interface interfaceToAdd) {
        String interfaceToAddName = interfaceToAdd.getClassName();
        String interfaceNameToCompare;
        boolean nameExists = false;
        for (Interface ancestorInterface: this.ancestorsInterfaces) {
            interfaceNameToCompare = ancestorInterface.getClassName();
            if (interfaceToAddName.equals(interfaceNameToCompare)) {
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToAdd.getToken(), "La interface " + "\"" + this.getClassName() + "\"" + " ya extiende a la interface " + interfaceToAdd.getClassName()));
                nameExists = true;
                break;
            }
        }
        if (!nameExists)
            this.ancestorsInterfaces.add(interfaceToAdd);
    }

    public void checkDeclarations() {
        for (Interface interfaceToCheck : this.ancestorsInterfaces) {
            Token interfaceToken = interfaceToCheck.getToken();
            String interfaceToCheckName = interfaceToken.getLexeme();
            if (!this.interfaceIsDeclared(interfaceToCheckName))
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToken, "La interface " + interfaceToCheckName + " no esta declarada"));
        }
        this.checkMethodsDeclaration();
        this.checkCyclicInheritance();
    }

    private void checkMethodsDeclaration() {
        for (Method methodToCheck: this.methods.values())
            methodToCheck.checkDeclaration();
    }

    public void consolidate() {
        if (!this.consolidated) {
            if (!this.hasCyclicInheritance)
                for (Interface interfaceToCheck: this.ancestorsInterfaces) {
                    Interface interfaceInSymbolTable = SymbolTable.getInstance().getInterface(interfaceToCheck.getClassName());
                    if (interfaceInSymbolTable != null) {
                        if (!interfaceInSymbolTable.isConsolidated())
                            interfaceInSymbolTable.consolidate();
                        this.consolidateMethods(interfaceInSymbolTable);
                        interfaceInSymbolTable.setConsolidated();
                    }
                }
        }
    }

    private void checkCyclicInheritance() {
        ArrayList<String> ancestorsList = new ArrayList<>();
        ancestorsList.add(this.getClassName());
        for (Interface ancestorInterface: this.ancestorsInterfaces) {
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
            for (Interface ancestorInterface: this.ancestorsInterfaces) {
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

    private void consolidateMethods(Interface interfaceToConsolidateWith) {
        for (Method ancestorMethod: interfaceToConsolidateWith.getMethods().values()) {
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
            else {
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToken, "La clase " + concreteClassToCheck.getClassName() + " no implementa todos los metodos de la interface " + this.getClassName()));
                break;
            }
        }

    }

    private boolean isConsolidated() {
        return this.consolidated;
    }

}
