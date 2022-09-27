package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.ArrayList;
import java.util.Hashtable;

public class ConcreteClass extends Class {

    private Token ancestorClassToken;
    private Hashtable<String, Attribute> attributes;

    public ConcreteClass(Token classToken, Token ancestorToken) {
        super(classToken);
        this.attributes = new Hashtable<>();
        this.ancestorClassToken = ancestorToken;
    }

    public Hashtable<String, Attribute> getAttributes() {
        return this.attributes;
    }

    public String getAncestorClassName() {
        if (this.ancestorClassToken != null)
            return this.ancestorClassToken.getLexeme();
        return null;
    }

    public void insertMethod(Method methodToInsert) throws SemanticException {
        if (!methodAlreadyExist(methodToInsert))
            this.methods.put(methodToInsert.getMethodName(), methodToInsert);
        else
            throw new SemanticException(methodToInsert.getMethodToken(), "Ya existe un metodo con nombre " + "\"" + methodToInsert.getMethodName() + "\"" + " en la clase " + this.getClassName());
    }

    public void insertAttribute(Attribute attribute) throws SemanticException {
        if (!this.attributes.containsKey(attribute.getAttributeName()))
            this.attributes.put(attribute.getAttributeName(), attribute);
        else
            throw new SemanticException(attribute.getAttributeToken(), "El atributo " + attribute.getAttributeToken().getLexeme() + " ya esta declarado en la clase " + this.classToken.getLexeme());
    }

    public void checkDeclarations() throws SemanticException {
        this.checkCyclicInheritance();
        this.checkAncestorClass();
        this.checkInterfacesDeclaration();
        this.checkAttributesDeclaration();
        this.checkMethodsDeclaration();
    }

    private void checkAncestorClass() throws SemanticException {
        if (this.getAncestorClassName() != null)
            if (!this.concreteClassIsDeclared(this.getAncestorClassName()))
                throw new SemanticException(this.ancestorClassToken, "La clase " + this.getAncestorClassName() + " no esta declarada");
    }

    private void checkCyclicInheritance() throws SemanticException {
        ArrayList<String> ancestorsList = new ArrayList<>();
        this.getAncestorsList(ancestorsList);
    }

    public void getAncestorsList(ArrayList<String> ancestorsList) throws SemanticException {
        if (this.getAncestorClass() != null) {
            if (!ancestorsList.contains(this.getAncestorClass().getClassName())) {
                ancestorsList.add(this.ancestorClassToken.getLexeme());
                this.getAncestorClass().getAncestorsList(ancestorsList);
            } else
                throw new SemanticException(this.ancestorClassToken, "Herencia circular: la clase " + "\"" + this.getClassName() + "\"" + " se extiende a si misma");
        }
    }

    private void checkInterfacesDeclaration() throws SemanticException {
        for (Interface interfaceToCheckIfItsDeclared : this.interfaces) {
            Token interfaceToken = interfaceToCheckIfItsDeclared.getToken();
            String interfaceName = interfaceToken.getLexeme();
            if (!this.interfaceIsDeclared(interfaceName))
                throw new SemanticException(interfaceToken, "La interface " + interfaceName + " no esta declarada");
        }
    }

    private void checkAttributesDeclaration() throws SemanticException {
        for (Attribute attributeToCheck: this.attributes.values())
            attributeToCheck.checkDeclaration();
    }

    private void checkMethodsDeclaration() throws SemanticException {
        for (Method methodToCheck: this.methods.values())
            methodToCheck.checkDeclaration();
    }

    public void consolidate() throws SemanticException {
        if (!this.consolidated) {
            if (this.getAncestorClass() != null) {
                ConcreteClass ancestorClass = this.getAncestorClass();
                if (!ancestorClass.isConsolidated())
                    ancestorClass.consolidate();
                this.consolidateAttributes(ancestorClass);
                this.consolidateMethods(ancestorClass);
                this.verifyInterfacesMethods();
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
                throw new SemanticException(thisClassAttribute.getAttributeToken(), "El atributo " + "\"" + thisClassAttribute.getAttributeName() + "\"" + " ya fue declarado en una clase ancestra");
            }
        }
    }

    public void consolidateMethods(Class classToConsolidateWith) throws SemanticException {
        for (Method ancestorMethod: classToConsolidateWith.getMethods().values()) {
            String methodName = ancestorMethod.getMethodName();
            if (!this.getMethods().containsKey(methodName)) {
                if (!isMainMethod(ancestorMethod))
                    this.insertMethod(ancestorMethod);
            }
            else {
                Method thisClassMethod = this.getMethod(methodName);
                if (!thisClassMethod.correctRedefinedMethodHeader(ancestorMethod))
                    throw new SemanticException(thisClassMethod.getMethodToken(), "El metodo " + "\"" + thisClassMethod.getMethodName() + "\"" + " esta incorrectamente redefinido");
            }
        }
    }

    private boolean isMainMethod(Method method) {
        if (method.getStaticHeader().equals("static") && method.getReturnType().equals("void") && method.getMethodName().equals("main") && !method.hasParameters())
            return true;
        return false;
    }

    public ConcreteClass getAncestorClass() {
        if (this.ancestorClassToken != null)
            return SymbolTable.getInstance().getConcreteClass(this.ancestorClassToken.getLexeme());
        return null;
    }

    private boolean isConsolidated() {
        return this.consolidated;
    }

    private void verifyInterfacesMethods() throws SemanticException {
        for (Interface interfaceThatImplements: this.interfaces) {
            Token interfaceToken = interfaceThatImplements.getToken();
            String interfaceName = interfaceToken.getLexeme();
            Interface interfaceToVerifyMethodsImplementations = SymbolTable.getInstance().getInterface(interfaceName);
            interfaceToVerifyMethodsImplementations.verifyMethodsImplementation(interfaceToken, this);
        }
    }

    private boolean concreteClassIsDeclared(String concreteClassName) {
        return SymbolTable.getInstance().concreteClassIsDeclared(concreteClassName);
    }

    private boolean interfaceIsDeclared(String interfaceName) {
        return SymbolTable.getInstance().interfaceIsDeclared(interfaceName);
    }

}

