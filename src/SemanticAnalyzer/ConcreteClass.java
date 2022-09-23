package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.ArrayList;
import java.util.Hashtable;

public class ConcreteClass extends Class {

    private Token ancestorToken;
    private Hashtable<String, Attribute> attributes;

    public ConcreteClass(Token classToken, Token ancestorToken) {
        super(classToken);
        this.attributes = new Hashtable<>();
        this.ancestorToken = ancestorToken;
    }

    public Hashtable<String, Attribute> getAttributes() {
        return this.attributes;
    }

    public String getAncestorClassName() {
        if (this.ancestorToken != null)
            return this.ancestorToken.getLexeme();
        return null;
    }

    public void insertMethod(Method methodToInsert) throws SemanticException {
        if (!methodAlreadyExist(methodToInsert)) {
            this.methods.put(methodToInsert.getMethodName(), methodToInsert);
//            this.checkIfItsMainMethod(methodToInsert);
        }
        else
            throw new SemanticException(methodToInsert.getMethodToken(), "El metodo " + "\"" + methodToInsert.getMethodName() + "\"" + " ya esta declarado" + " en la clase " + this.getClassName());
    }

    //todo acomodar, quedaria mejor si esta en la symboltable
//    private void checkIfItsMainMethod(Method methodToCheck) {
//        if (methodToCheck.getStaticHeader().equals("static") && methodToCheck.getMethodName().equals("main") && !methodToCheck.hasParameters())
//            SymbolTable.getInstance().setMainMethodDeclared();
//    }

    public void insertAttribute(Attribute attribute) throws SemanticException {
        if (!this.attributes.containsKey(attribute.getAttributeName()))
            this.attributes.put(attribute.getAttributeName(), attribute);
        else
            throw new SemanticException(attribute.getAttributeToken(), "El atributo " + attribute.getAttributeToken().getLexeme() + " ya esta declarado en la clase " + this.classToken.getLexeme());
    }

    public void checkDeclaration() throws SemanticException {
        this.checkCyclicInheritance();
        this.checkAncestorClass();
        //todo los metodos de la interfaz los tengo que chequear despues de consolidar las inter
//        this.checkInterfacesDeclaration();
        this.checkAttributesDeclaration();
        this.checkMethodsDeclaration();
    }

    public void consolidate() throws SemanticException {
        if (!this.consolidated) {
            if (this.getAncestorClass() != null) {
                ConcreteClass ancestorClass = this.getAncestorClass();
                if (!ancestorClass.isConsolidated())
                    ancestorClass.consolidate();
                this.consolidateAttributes(ancestorClass);
                this.consolidateMethods(ancestorClass);
                this.checkInterfacesDeclaration(); //todo preguntar
                this.checkCyclicInheritance();
                this.consolidated = true;
            }
        }
    }

    private void consolidateAttributes(ConcreteClass ancestorClass) throws SemanticException {
        for (Attribute ancestorAttribute: ancestorClass.getAttributes().values()) {
            String ancestorAttributeName = ancestorAttribute.getAttributeName();
            if (!this.getAttributes().containsKey(ancestorAttributeName))
                this.insertAttribute(ancestorAttribute);
            else {
                Attribute thisClassAttribute = this.getAttributes().get(ancestorAttributeName);
                throw new SemanticException(thisClassAttribute.getAttributeToken(), "El atributo " + "\"" + thisClassAttribute.getAttributeName() + "\"" + " ya fue declarado en la clase ancestra");
            }
        }
    }

    public void consolidateMethods(Class classToConsolidateWith) throws SemanticException {
        for (Method ancestorMethod: classToConsolidateWith.getMethods().values()) {
            String methodName = ancestorMethod.getMethodName();
            if (!this.getMethods().containsKey(methodName)) {
                this.insertMethod(ancestorMethod);
            }
            else {
                Method thisClassMethod = this.getMethod(methodName);
                if (!thisClassMethod.correctRedefinedMethodHeader(ancestorMethod))
                    throw new SemanticException(thisClassMethod.getMethodToken(), "El metodo " + "\"" + thisClassMethod.getMethodName() + "\"" + " esta incorrectamente redefinido");
            }
        }
    }

    public ConcreteClass getAncestorClass() {
        if (this.ancestorToken != null)
            return SymbolTable.getInstance().getConcreteClass(this.ancestorToken.getLexeme());
        return null;
    }

    private boolean isConsolidated() {
        return this.consolidated;
    }

    private void checkAncestorClass() throws SemanticException {
        if (this.getAncestorClassName() != null)
            if (!classIsDeclared(this.getAncestorClassName()))
                throw new SemanticException(this.ancestorToken, "La clase " + this.getAncestorClassName() + " no esta declarada");
    }

    private void checkCyclicInheritance() throws SemanticException {
        ArrayList<String> ancestorsList = new ArrayList<>();
        this.getAncestorsList(ancestorsList);
    }

    public void getAncestorsList(ArrayList<String> ancestorsList) throws SemanticException {
        if (this.getAncestorClass() != null) {
            if (!ancestorsList.contains(this.getAncestorClass().getClassName())) {
                ancestorsList.add(this.ancestorToken.getLexeme());
                this.getAncestorClass().getAncestorsList(ancestorsList);
            } else
                throw new SemanticException(this.classToken, "Herencia circular: la clase " + "\"" + this.getClassName() + "\"" + " posee un ancestro que extiende a la clase " + "\"" + this.getClassName() + "\"");
        }
    }

    private void checkInterfacesDeclaration() throws SemanticException {
        for (Interface interfaceToCheckIfItsDeclared: this.interfaces) {
            Token interfaceToken = interfaceToCheckIfItsDeclared.getToken();
            String interfaceName = interfaceToken.getLexeme();
            if (!this.interfaceIsDeclared(interfaceName))
                throw new SemanticException(interfaceToken, "La interface " + interfaceName + " no esta declarada");
            else {
                Interface interfaceToCheck = SymbolTable.getInstance().getInterface(interfaceToken.getLexeme());
                interfaceToCheck.checkIfClassImplementsAllInterfaceMethods(this);
            }
        }
    }

    private void checkAttributesDeclaration() throws SemanticException {
        for (Attribute attributeToCheck: this.attributes.values())
            attributeToCheck.checkDeclaration();
    }

    private  void checkMethodsDeclaration() throws SemanticException {
        for (Method methodToCheck: this.methods.values())
            methodToCheck.checkDeclaration();
    }

    private boolean classIsDeclared(String concreteClassName) {
        return SymbolTable.getInstance().classIsDeclared(concreteClassName);
    }

    private boolean interfaceIsDeclared(String interfaceName) {
        return SymbolTable.getInstance().interfaceIsDeclared(interfaceName);
    }

}

