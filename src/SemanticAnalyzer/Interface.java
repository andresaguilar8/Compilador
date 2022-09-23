package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.ArrayList;

public class Interface extends Class {

    public Interface(Token interfaceToken) {
        super(interfaceToken);
    }

    public void insertMethod(Method methodToInsert) throws SemanticException {
        if (methodToInsert.getStaticHeader().equals("static"))
            throw new SemanticException(methodToInsert.getMethodToken(), "Una interface no puede tener metodos estaticos");
        if (!methodAlreadyExist(methodToInsert))
            this.methods.put(methodToInsert.getMethodName(), methodToInsert);
        else
            throw new SemanticException(methodToInsert.getMethodToken(), "El metodo " + "\"" + methodToInsert.getMethodName() + "\"" + " ya esta declarado" + " en la clase " + this.getClassName());
    }

    public void checkDeclaration() throws SemanticException {
        for (Interface interfaceToCheck : this.interfaces) {
            Token interfaceToken = interfaceToCheck.getToken();
            String interfaceToCheckName = interfaceToken.getLexeme();
            if (!this.interfaceIsDeclared(interfaceToCheckName))
                throw new SemanticException(interfaceToken, "La interface " + interfaceToCheckName + " no esta declarada");
        }
    }

    public void consolidate() throws SemanticException {
        if (!this.consolidated) {
            for (Interface interfaceToCheck: this.interfaces) {
                if (!interfaceToCheck.isConsolidated())
                    interfaceToCheck.consolidate();
                Interface interfaceInTable = SymbolTable.getInstance().getInterface(interfaceToCheck.getClassName());
                this.consolidateMethods(interfaceInTable);
                this.checkCyclicInheritance(); //todo preg
                this.consolidated = true;
            }
        }
    }

    private void checkCyclicInheritance() throws SemanticException {
        ArrayList<String> ancestorsList = new ArrayList<>();
        this.getAncestorsList(ancestorsList, this.getClassName());
    }

    public void getAncestorsList(ArrayList<String> ancestorsList, String thisClassName) throws SemanticException {
        for (Interface inter: this.interfaces) {
            if (!ancestorsList.contains(thisClassName)) {
                if (!ancestorsList.contains(inter.getToken().getLexeme()))
                    ancestorsList.add(inter.getToken().getLexeme());
                SymbolTable.getInstance().getInterface(inter.getClassName()).getAncestorsList(ancestorsList, thisClassName);
            } else
                  throw new SemanticException(this.classToken, "Herencia circular: la interface " + "\"" + this.getClassName() + "\"" + " posee un ancestro que extiende a la clase " + "\"" + this.getClassName() + "\"");
        }
    }

    private void consolidateMethods(Interface classToConsolidateWith) throws SemanticException {
        for (Method ancestorMethod: classToConsolidateWith.getMethods().values()) {
            String methodName = ancestorMethod.getMethodName();
            if (!this.getMethods().containsKey(methodName))
                this.insertMethod(ancestorMethod);
            else {
                Method thisClassMethod = this.getMethod(methodName);
                if (!thisClassMethod.correctRedefinedMethodHeader(ancestorMethod))
                    throw new SemanticException(thisClassMethod.getMethodToken(), "El metodo " + "\"" + thisClassMethod.getMethodName() + "\"" + " esta incorrectamente redefinido");
            }
        }
    }

    private boolean interfaceIsDeclared(String concreteClassName) {
        return SymbolTable.getInstance().interfaceIsDeclared(concreteClassName);
    }

    public void checkIfClassImplementsAllInterfaceMethods(ConcreteClass concreteClassToCheck) throws SemanticException {
        for (Method method : this.methods.values()) {
            if (concreteClassToCheck.getMethods().containsKey(method.getMethodName())) {
                String methodName = method.getMethodName();
                if (!method.methodsHeadersAreEquals(concreteClassToCheck.getMethod(methodName)))
                    throw new SemanticException(concreteClassToCheck.getMethod(method.getMethodName()).getMethodToken(), "El metodo " + "\"" + method.getMethodName() + "\"" + " se encuentra implementado con distintos parametros que el encabezado del metodo en su interface.");
            }
            else
                throw new SemanticException(concreteClassToCheck.getToken(), "La clase " + concreteClassToCheck.getClassName() + " no implementa todos los metodos de la interface " + this.getClassName());
        }
    }

    private boolean isConsolidated() {
        return this.consolidated;
    }

}
