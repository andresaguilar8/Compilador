package SemanticAnalyzer;

import AST.Sentence.BlockNode;
import LexicalAnalyzer.Token;
import InstructionGenerator.InstructionGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Method {

    private Token methodToken;
    private String staticScope;
    private Type methodReturnType;
    private ArrayList<Parameter> parametersList;
    private Hashtable<String, Parameter> parametersTable;
    private BlockNode currentBlock;
    private BlockNode principalBlock;
    private boolean isInherited;
    private boolean principalBlockIsChecked;
    private String className;
    private boolean codeIsGenerated;
    private int offset;
    private boolean hasOffset;
    private boolean isInterfaceMethod;

    public Method(Token methodToken, String staticScope, Type methodReturnType, String className) {
        this.staticScope = staticScope;
        this.methodToken = methodToken;
        this.methodReturnType = methodReturnType;
        this.parametersList = new ArrayList<>();
        this.parametersTable = new Hashtable<>();
        this.isInherited = false;
        this.principalBlockIsChecked = false;
        this.className = className;
        this.codeIsGenerated = false;
        this.hasOffset = false;
        this.isInterfaceMethod = false;
    }

    public void insertParameter(Parameter parameterToInsert) {
        if (!this.parametersTable.containsKey(parameterToInsert.getParameterName())) {
            this.parametersTable.put(parameterToInsert.getParameterName(), parameterToInsert);
            this.parametersList.add(parameterToInsert);
        }
        else
            SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(parameterToInsert.getParameterToken(), "El parametro " + parameterToInsert.getParameterName() + " ya esta declarado en el metodo " + "\"" + this.methodToken.getLexeme() + "\""));
    }

    public String getMethodName() {
        return this.methodToken.getLexeme();
    }

    public String getStaticHeader() {
        return this.staticScope;
    }

    public String getReturnTypeString() {
        return this.methodReturnType.getClassName();
    }

    public Type getReturnType() {
        return this.methodReturnType;
    }

    public ArrayList<Parameter> getParametersList() {
        return this.parametersList;
    }

    public void checkDeclaration() {
        this.checkNoPrimitiveParameters();
        this.checkNoPrimitiveReturnType();
    }

    private void checkNoPrimitiveParameters() {
        for (Parameter parameter: this.parametersTable.values()) {
            if (!parameter.getParameterType().isPrimitive())
                if (!parameterTypeIsDeclared(parameter)) {
                    Token parameterTypeToken = parameter.getParameterType().getToken();
                    SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(parameterTypeToken, "El tipo del parametro " + "\"" + parameter.getParameterName() + "\"" + " del metodo " + "\"" + this.methodToken.getLexeme() + "\"" + " no esta declarado"));
                }
        }
    }

    private void checkNoPrimitiveReturnType() {
        if (!this.methodReturnType.isPrimitive())
            if (!this.returnTypeClassIsDeclared())
                SymbolTable.getInstance().getSemanticErrorsList().add(new SemanticError(this.methodReturnType.getToken(), "El tipo de retorno del metodo " + "\"" + this.methodToken.getLexeme() + "\"" + " no esta declarado"));
    }

    private boolean parameterTypeIsDeclared(Parameter parameter) {
        Type parameterType = parameter.getParameterType();
        String parameterClass = parameterType.getClassName();
        return SymbolTable.getInstance().concreteClassIsDeclared(parameterClass) || SymbolTable.getInstance().interfaceIsDeclared(parameterClass);
    }

    public boolean correctRedefinedMethodHeader(Method ancestorMethod) {
        return this.methodsHeadersAreEquals(ancestorMethod);
    }

    public boolean methodsHeadersAreEquals(Method ancestorMethod) {
        if (!ancestorMethod.getStaticHeader().equals(this.staticScope) || !ancestorMethod.getReturnTypeString().equals(this.methodReturnType.getClassName()) || !this.hasEqualsParameters(ancestorMethod))
            return false;
        return true;
    }

    private boolean hasEqualsParameters(Method ancestorMethod) {
        boolean parametersAreEquals;
        if (ancestorMethod.getParametersList().size() == this.parametersList.size()) {
            parametersAreEquals = true;
            int parameterIndex = 0;
            while (parametersAreEquals && (parameterIndex < this.parametersList.size())) {
                Parameter ancestorParameter = ancestorMethod.getParametersList().get(parameterIndex);
                if (!hasEqualsParameters(ancestorParameter, parameterIndex))
                    parametersAreEquals = false;
                parameterIndex = parameterIndex + 1;
            }
        }
        else
            parametersAreEquals = false;
        return parametersAreEquals;
    }

    private boolean hasEqualsParameters(Parameter parameterToCompareWith, int parameterIndex) {
        Parameter parameterOfThisMethod = this.parametersList.get(parameterIndex);
        if (!parameterToCompareWith.getParameterType().getClassName().equals(parameterOfThisMethod.getParameterType().getClassName()))
            return false;
        return true;
    }

    private boolean returnTypeClassIsDeclared() {
        return SymbolTable.getInstance().concreteClassIsDeclared(this.methodReturnType.getClassName()) || SymbolTable.getInstance().interfaceIsDeclared(this.methodReturnType.getClassName());
    }

    public Token getMethodToken() {
        return this.methodToken;
    }

    public boolean hasParameters() {
        return this.parametersList.size() != 0;
    }

    public void setPrincipalBlock(BlockNode blockNode) {
        this.principalBlock = blockNode;
    }

    public BlockNode getPrincipalBlock() {
        return this.principalBlock;
    }

    public void setCurrentBlock(BlockNode blockNode) {
        this.currentBlock = blockNode;
    }

    public BlockNode getCurrentBlock() {
        return this.currentBlock;
    }

    public void setChecked() {
        this.principalBlockIsChecked = true;
    }

    public boolean isChecked() {
        return this.principalBlockIsChecked;
    }

    public boolean isInherited() {
        return this.isInherited;
    }

    public void setInherited() {
        this.isInherited = true;
    }

    public ConcreteClass getMethodClass() {
        return SymbolTable.getInstance().getConcreteClass(this.className);
    }

    public void generateCode() throws IOException {
        InstructionGenerator.getInstance().generateInstruction(this.getMethodLabel() + ":");
        InstructionGenerator.getInstance().generateInstruction("LOADFP ; Se guarda el enlace dinámico del registro de activación del llamador");
        InstructionGenerator.getInstance().generateInstruction("LOADSP ; Se apila el comienzo del registro de activación de la unidad llamada");
        InstructionGenerator.getInstance().generateInstruction("STOREFP ; Se actualiza el frame pointer para que indicar que el RA que estamos armando es el actual (llamado)");

        if (this.parametersList.size() > 0) {
            int parameterOffset;
            if (!this.staticScope.equals("static")) //es dinamico
                parameterOffset = 3;
            else
                parameterOffset = 2;
            for (Parameter parameter : this.parametersList) {
                parameterOffset += 1;
                parameter.setOffset(parameterOffset);
            }
        }

        //Si no tiene un bloque principal entonces se trata de un método predefinido
        if (this.principalBlock != null) {
            this.principalBlock.generateCode();
            this.codeIsGenerated = true;
        }
        else
            generateCodeForPredefinedMethod();

        InstructionGenerator.getInstance().generateInstruction("STOREFP ; Se actualiza el frame pointer");
        InstructionGenerator.getInstance().generateInstruction("RET "+ this.getReturnOffset() + " ; Retorna el retorno de la unidad y libera " + this.getReturnOffset() + " lugares de la pila");
    }

    private void generateCodeForPredefinedMethod() throws IOException {
        if (this.getMethodName().equals("debugPrint")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");   //LOAD 3 porque tiene un solo parametro
            InstructionGenerator.getInstance().generateInstruction("IPRINT");
            InstructionGenerator.getInstance().generateInstruction("PRNLN");
        }
        if (this.getMethodName().equals("read")) {
            //lee el próximo byte del stream de entrada estándar
            InstructionGenerator.getInstance().generateInstruction("READ");
            InstructionGenerator.getInstance().generateInstruction("STORE 3");
        }
        if (this.getMethodName().equals("printB")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");   //LOAD 3 porque tiene un solo parametro
            InstructionGenerator.getInstance().generateInstruction("BPRINT");
        }
        if (this.getMethodName().equals("printC")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");
            InstructionGenerator.getInstance().generateInstruction("CPRINT");
        }
        if (this.getMethodName().equals("printI")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");
            InstructionGenerator.getInstance().generateInstruction("IPRINT");
        }
        if (this.getMethodName().equals("printS")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");
            InstructionGenerator.getInstance().generateInstruction("SPRINT");
        }
        if (this.getMethodName().equals("println")) {
            InstructionGenerator.getInstance().generateInstruction("PRNLN");
        }
        if (this.getMethodName().equals("printBln")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");   //LOAD 3 porque tiene un solo parametro
            InstructionGenerator.getInstance().generateInstruction("BPRINT");
            InstructionGenerator.getInstance().generateInstruction("PRNLN");
        }
        if (this.getMethodName().equals("printCln")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");   //LOAD 3 porque tiene un solo parametro
            InstructionGenerator.getInstance().generateInstruction("CPRINT");
            InstructionGenerator.getInstance().generateInstruction("PRNLN");
        }
        if (this.getMethodName().equals("printIln")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");   //LOAD 3 porque tiene un solo parametro
            InstructionGenerator.getInstance().generateInstruction("IPRINT");
            InstructionGenerator.getInstance().generateInstruction("PRNLN");
        }
        if (this.getMethodName().equals("printSln")) {
            InstructionGenerator.getInstance().generateInstruction("LOAD 3");   //LOAD 3 porque tiene un solo parametro
            InstructionGenerator.getInstance().generateInstruction("SPRINT");
            InstructionGenerator.getInstance().generateInstruction("PRNLN");
        }
    }

    public int getStoringValueInReturnOffset() {
        if (this.getStaticHeader().equals("static"))
            return 3 + this.parametersList.size();
        else
            return 3 + this.parametersList.size() + 1;
    }

    public int getReturnOffset() {
        //si el metodo es dinamico, retornar lista + 1 por el this
        if (this.staticScope.equals("static")) {
            if (parametersList != null)
                return this.parametersList.size();
            else
                return 0;
        }
        else
            if (parametersList != null)
                return this.parametersList.size() + 1;
            else
                return 1;
    }

    public boolean codeIsGenerated() {
        return this.codeIsGenerated;
    }

    public String getMethodLabel() {
        return this.getMethodName() + "_Clase" + this.className;
    }

    public void setCodeGenerated() {
        this.codeIsGenerated = true;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffsetIsSet() {
        this.hasOffset = true;
    }

    public boolean hasOffset() {
        return this.hasOffset;
    }

    public boolean isStatic() {
        return this.staticScope.equals("static");
    }

    public boolean isInterfaceMethod() {
        return this.isInterfaceMethod;
    }

    public void setAsInterfaceMethod() {
        this.isInterfaceMethod = true;
    }

    private Interface interfaceMethod;

    public void setInterface(Interface interfaceMethod) {
        this.interfaceMethod = interfaceMethod;
    }

    public Interface getInterfaceMethod() {
        return this.interfaceMethod;
    }
}
