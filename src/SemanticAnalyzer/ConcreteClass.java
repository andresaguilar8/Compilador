package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import InstructionGenerator.InstructionGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ConcreteClass extends Class {

    private Token ancestorClassToken;
    private Hashtable<String, Attribute> attributes;
    private Constructor classConstructor;
    private boolean hasRepeatedInterfaces;
    private boolean methodOffsetsGenerated;
    private boolean attributeOffsetsGenerated;
    private int cirSize;
    private int vtSize;
    private Hashtable<Integer, Method> dynamicMethodsOffsetsMap;
    private boolean vtSizeIsSet;

    public ConcreteClass(Token classToken, Token ancestorToken) {
        super(classToken);
        this.attributes = new Hashtable<>();
        this.ancestorClassToken = ancestorToken;
        this.hasRepeatedInterfaces = false;
        this.attributeOffsetsGenerated = false;
        this.methodOffsetsGenerated = false;
        this.cirSize = 1; //el 0 es para la VT
        this.dynamicMethodsOffsetsMap = new Hashtable<>();
        this.vtSizeIsSet = false;
        this.vtSize = 0;
    }

    public int getTotalOfDynamicMethods() {
        int totalOfDynamicMethods = 0;
        for (Method method: this.methods.values()) {
            if (!method.isStatic())
                totalOfDynamicMethods++;
        }
        return totalOfDynamicMethods;
    }

    public Hashtable<String, Attribute> getAttributes() {
        return this.attributes;
    }

    public String getAncestorClassName() {
        if (this.ancestorClassToken != null)
            return this.ancestorClassToken.getLexeme();
        return null;
    }

    public boolean hasAncestorInterface(String interfaceNameToCheckFor) {
        boolean toReturn = false;
        for (Interface i: this.ancestorsInterfaces) {
            if (i.getClassName().equals(interfaceNameToCheckFor))
                return true;
            if (i.hasAncestorInterface(interfaceNameToCheckFor))
                toReturn =  true;
        }
        return toReturn;
    }

    public void addAncestorInterface(Interface interfaceToAdd) {
        String interfaceToAddName = interfaceToAdd.getClassName();
        String interfaceNameToCompare;
        boolean nameExists = false;
        for (Interface ancestorInterface: this.ancestorsInterfaces) {
            interfaceNameToCompare = ancestorInterface.getClassName();
            if (interfaceToAddName.equals(interfaceNameToCompare)) {
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(interfaceToAdd.getToken(), "La clase " + "\"" + this.getClassName() + "\"" + " ya implementa a la interface " + interfaceToAdd.getClassName()));
                nameExists = true;
                break;
            }
        }
        if (!nameExists)
            this.ancestorsInterfaces.add(interfaceToAdd);
    }

    public void insertMethod(Method methodToInsert) {
        if (!methodAlreadyExist(methodToInsert))
            this.methods.put(methodToInsert.getMethodName(), methodToInsert);
        else
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(methodToInsert.getMethodToken(), "Ya existe un metodo con nombre " + "\"" + methodToInsert.getMethodName() + "\"" + " en la clase " + this.getClassName()));
    }

    public void insertAttribute(Attribute attribute) {
        if (!this.attributes.containsKey(attribute.getAttributeName())) {
            Attribute attributeToInsert = new Attribute(attribute.getAttributeToken(), attribute.getAttributeType(), attribute.getVisibility());
            this.attributes.put(attributeToInsert.getAttributeName(), attributeToInsert);
        }
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

    public Constructor getClassConstructor() {
        return this.classConstructor;
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
        for (Interface interfaceToCheck : this.ancestorsInterfaces) {
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
            if (!this.getAttributes().containsKey(ancestorAttributeName)) {
                this.insertAttribute(ancestorAttribute);
                this.setAttributeAsInherited(ancestorAttribute.getAttributeName());
            }
            else {
                Attribute thisClassAttribute = this.getAttributes().get(ancestorAttributeName);
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(thisClassAttribute.getAttributeToken(), "El atributo " + "\"" + thisClassAttribute.getAttributeName() + "\"" + " ya fue declarado en una clase ancestra"));
            }
        }
    }

    private void setAttributeAsInherited(String attributeName) {
        for (Attribute attribute: this.attributes.values())
            if (attribute.getAttributeName().equals(attributeName)) {
                attribute.setInherited();
                break;
            }
    }

    public void consolidateMethods(Class classToConsolidateWith) {
        for (Method ancestorMethod: classToConsolidateWith.getMethods().values()) {
            String methodName = ancestorMethod.getMethodName();
            if (!this.getMethods().containsKey(methodName)) {
                this.insertMethod(ancestorMethod);
                this.setMethodAsInherited(ancestorMethod.getMethodName());
            }
            else {
                Method thisClassMethod = this.getMethod(methodName);
                if (!thisClassMethod.correctRedefinedMethodHeader(ancestorMethod))
                    SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(thisClassMethod.getMethodToken(), "El metodo " + "\"" + thisClassMethod.getMethodName() + "\"" + " esta incorrectamente redefinido"));
            }
        }
    }

    private void setMethodAsInherited(String methodName) {
        for (Method method: this.getMethods().values())
            if (method.getMethodName().equals(methodName)) {
                method.setInherited();
                break;
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
        for (Interface interfaceThatImplements: this.ancestorsInterfaces) {
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

    public boolean hasAttributeOffsetsGenerated() {
        return this.attributeOffsetsGenerated;
    }

    public boolean hasMethodOffsetsGenerated() {
        return this.methodOffsetsGenerated;
    }

    public int getCirSize() {
        return this.cirSize;
    }

    public int getVtSize() {
        return this.vtSize;
    }

    public String getVTLabel() {
        return "VT_Clase" + this.getClassName();
    }

    public void generateOffsets() {
        this.generateAttributesOffsets();
        this.generateClassMethodsOffsets();
    }

    public void generateAttributesOffsets() {
        if (this.getAncestorClass() != null)
            if (!this.getAncestorClass().getClassName().equals("Object"))
                if (!this.getAncestorClass().hasAttributeOffsetsGenerated())
                    this.getAncestorClass().generateOffsets();

        if (this.getAncestorClass() != null) {
            for (Attribute ancestorAttribute: this.getAncestorClass().getAttributes().values()) {
                for (Attribute attribute: this.attributes.values())
                    if (ancestorAttribute.getAttributeName().equals(attribute.getAttributeName())) {
                        attribute.setOffset(ancestorAttribute.getOffset());
                    }
            }
            this.cirSize = this.getAncestorClass().getCirSize();
        }
        for (Attribute attribute: this.attributes.values()) {
            if (!attribute.isInherited()) {
                attribute.setOffset(this.getCirSize());
                this.cirSize += 1;
            }
        }
        this.attributeOffsetsGenerated = true;
    }

    public void generateClassMethodsOffsets() {
        if (this.getAncestorClass() != null)
            if (!this.getAncestorClass().hasMethodOffsetsGenerated())
                this.getAncestorClass().generateClassMethodsOffsets();

        if (this.getAncestorClass() != null)
            if (!this.getAncestorClass().getClassName().equals("Object")) {
                for (Method ancestorMethod: this.getAncestorClass().getMethods().values())
                    for (Method method: this.methods.values())
                        if (!method.isInterfaceMethod())
                            if (ancestorMethod.getMethodName().equals(method.getMethodName())) {
                                method.setOffset(ancestorMethod.getOffset());
                                method.setOffsetIsSet();
                            }
            this.vtSize = this.getAncestorClass().getVtSize();
        }

        for (Method method: this.methods.values()) {
            if (!method.getStaticHeader().equals("static"))
                if (!method.isInterfaceMethod()) {
                    if (!method.hasOffset()) {
                        method.setOffset(this.vtSize);
                        method.setOffsetIsSet();
                        this.vtSize += 1;
                    }
                    this.dynamicMethodsOffsetsMap.put(method.getOffset(), method);
                }
        }

        this.methodOffsetsGenerated = true;
    }

    public void generateInterfaceMethodsOffsets() {
        for (Method interfaceMethodToSetOffset : this.methods.values()) {
            if (interfaceMethodToSetOffset.isInterfaceMethod()) {
                Interface interfaceMethod = interfaceMethodToSetOffset.getInterfaceMethod();
                Method methodInInterface = interfaceMethod.getMethod(interfaceMethodToSetOffset.getMethodName());
                interfaceMethodToSetOffset.setOffset(methodInInterface.getOffset());
                this.dynamicMethodsOffsetsMap.put(interfaceMethodToSetOffset.getOffset(), interfaceMethodToSetOffset);
                interfaceMethodToSetOffset.setOffsetIsSet();
            }
        }
        this.vtSize = this.getGreatestOffset();
    }

    //genera offsets de aquellos metodos heredados de una clase
    //con igual nombre que uno de una interface
    //para poder usar en la VT
    public void generateInheritedMethodsOffsetsForVt() {
        if (this.getAncestorClass() != null && !this.getAncestorClass().getClassName().equals("Object"))
            for (Method method: this.getAncestorClass().getMethods().values()) {
                if (!method.isStatic()) {
                    int ancestorMethodOffset = method.getOffset();
                    if (!this.dynamicMethodsOffsetsMap.containsKey(ancestorMethodOffset)) {
                        Method thisClassMethod = SymbolTable.getInstance().getConcreteClass(this.getClassName()).getMethod(method.getMethodName());
                        this.dynamicMethodsOffsetsMap.put(ancestorMethodOffset, thisClassMethod);
                    }
                }
            }
    }

    private int getGreatestOffset() {
        int greatestMethodOffset = 0;
        for (Method method: this.methods.values()) {
            if (method.getOffset() > greatestMethodOffset)
                greatestMethodOffset = method.getOffset();
        }
        return greatestMethodOffset;
    }

    public void generateVT() throws IOException {
        InstructionGenerator.getInstance().setDataMode();
        InstructionGenerator.getInstance().generateInstruction("VT_Clase" + this.getClassName() + ":");
        String VTInstruction = "DW";

        if (this.dynamicMethodsOffsetsMap.size() > 0) {
            //la clase tiene metodos dinamicos

            for (int offset = 0; offset <= this.vtSize; offset++) {
                Method method = this.dynamicMethodsOffsetsMap.get(offset);
                if (method != null)
                    VTInstruction += " " + method.getMethodLabel() + ",";
                else
                    VTInstruction += " 0,";
            }
            VTInstruction = VTInstruction.substring(0, VTInstruction.length() - 1);  //elimino la , dps del ultimo metodo
            InstructionGenerator.getInstance().generateInstruction(VTInstruction);
        }
        else
            //la clase no tiene metodos dinamicos
            InstructionGenerator.getInstance().generateInstruction("NOP");
    }

    public void generateCode() throws IOException {
        InstructionGenerator.getInstance().setCodeMode();
        for (Method method: this.methods.values())
            if (!method.codeIsGenerated()) {
                method.generateCode();
                method.setCodeGenerated();
            }
        InstructionGenerator.getInstance().setCodeMode();
        this.classConstructor.generateCode();
    }

}

