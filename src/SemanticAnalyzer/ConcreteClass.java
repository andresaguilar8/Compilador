package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.ArrayList;
import java.util.Hashtable;

public class ConcreteClass extends Class {

    private Token ancestorClassToken;
    private Hashtable<String, Attribute> attributes;
    private Constructor classConstructor;
    private boolean hasRepeatedInterfaces;

    public ConcreteClass(Token classToken, Token ancestorToken) {
        super(classToken);
        this.attributes = new Hashtable<>();
        this.ancestorClassToken = ancestorToken;
        this.hasRepeatedInterfaces = false;
    }

    public Hashtable<String, Attribute> getAttributes() {
        return this.attributes;
    }

    public String getAncestorClassName() {
        if (this.ancestorClassToken != null)
            return this.ancestorClassToken.getLexeme();
        return null;
    }

    public void addAncestorInterface(Interface interfaceToAdd) {
        String interfaceToAddName = interfaceToAdd.getClassName();
        String interfaceNameToCompare;
        for (Interface interfacess: this.interfaces) {
            interfaceNameToCompare = interfacess.getClassName();
            if (interfaceToAddName.equals(interfaceNameToCompare)) {
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToAdd.getToken(), "La clase " + "\"" + this.getClassName() + "\"" + " ya implementa a la interface " + interfaceToAdd.getClassName()));
                break;
            }
        }
        this.interfaces.add(interfaceToAdd);
    }

    public void insertMethod(Method methodToInsert) {
        if (!methodAlreadyExist(methodToInsert))
            this.methods.put(methodToInsert.getMethodName(), methodToInsert);
        else
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(methodToInsert.getMethodToken(), "Ya existe un metodo con nombre " + "\"" + methodToInsert.getMethodName() + "\"" + " en la clase " + this.getClassName()));
    }

    public void insertAttribute(Attribute attribute) {
        if (!this.attributes.containsKey(attribute.getAttributeName()))
            this.attributes.put(attribute.getAttributeName(), attribute);
        else
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(attribute.getAttributeToken(), "El atributo " + attribute.getAttributeToken().getLexeme() + " ya esta declarado en la clase " + this.classToken.getLexeme()));
    }

    public void checkDeclarations() {
        this.checkCyclicInheritance();
        this.insertConstructor();
        this.checkAncestorClass();
        this.checkInterfacesDeclaration();
        this.checkAttributesDeclaration();
        this.checkMethodsDeclaration();
    }

    private void insertConstructor() {
        this.classConstructor = new Constructor(new Token("idClase", this.getClassName(), 0));
    }

    private void checkAncestorClass() {
        if (this.getAncestorClassName() != null)
            if (!this.concreteClassIsDeclared(this.getAncestorClassName()))
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(this.ancestorClassToken, "La clase " + this.getAncestorClassName() + " no esta declarada"));
    }

    private void checkCyclicInheritance() {
        ArrayList<String> ancestorsList = new ArrayList<>();
        if (this.hasCyclicInheritance(ancestorsList)) {
            this.hasCyclicInheritance = true;
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(this.ancestorClassToken, "Herencia circular: la clase " + "\"" + this.getClassName() + "\"" + " se extiende a si misma"));
        }
    }

    public boolean hasCyclicInheritance(ArrayList<String> ancestorsList) {
        if (this.getAncestorClass() != null) {
            if (!ancestorsList.contains(this.getAncestorClass().getClassName())) {
                ancestorsList.add(this.ancestorClassToken.getLexeme());
                return this.getAncestorClass().hasCyclicInheritance(ancestorsList);
            }
            else
                return true;
        }
        return false;
    }

    private void checkInterfacesDeclaration() {
        for (Interface interfaceToCheck : this.interfaces) {
            Token interfaceToken = interfaceToCheck.getToken();
            String interfaceName = interfaceToken.getLexeme();
            if (!this.interfaceIsDeclared(interfaceName))
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToken, "La interface " + interfaceName + " no esta declarada"));
        }
    }

    private void checkAttributesDeclaration() {
        for (Attribute attributeToCheck: this.attributes.values())
            attributeToCheck.checkDeclaration();
    }

    private void checkMethodsDeclaration() {
        for (Method methodToCheck: this.methods.values())
            methodToCheck.checkDeclaration();
    }

    public void consolidate() {
        if (!this.consolidated)
            if (!this.hasCyclicInheritance)
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

    private void consolidateAttributes(ConcreteClass ancestorClass) {
        for (Attribute ancestorAttribute: ancestorClass.getAttributes().values()) {
            String ancestorAttributeName = ancestorAttribute.getAttributeName();
            if (!this.getAttributes().containsKey(ancestorAttributeName))
                this.insertAttribute(ancestorAttribute);
            else {
                Attribute thisClassAttribute = this.getAttributes().get(ancestorAttributeName);
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(thisClassAttribute.getAttributeToken(), "El atributo " + "\"" + thisClassAttribute.getAttributeName() + "\"" + " ya fue declarado en una clase ancestra"));
            }
        }
    }

    public void consolidateMethods(Class classToConsolidateWith) {
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

    public ConcreteClass getAncestorClass() {
        if (this.ancestorClassToken != null)
            return SymbolTable.getInstance().getConcreteClass(this.ancestorClassToken.getLexeme());
        return null;
    }

    private boolean isConsolidated() {
        return this.consolidated;
    }

    private void verifyInterfacesMethods() {
        for (Interface interfaceThatImplements: this.interfaces) {
            Token interfaceToken = interfaceThatImplements.getToken();
            String interfaceName = interfaceToken.getLexeme();
            Interface interfaceToVerifyMethodsImplementations = SymbolTable.getInstance().getInterface(interfaceName);
            if (interfaceToVerifyMethodsImplementations != null)
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

