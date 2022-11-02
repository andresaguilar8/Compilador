package SemanticAnalyzer;

import AST.Sentence.BlockNode;
import AST.Sentence.LocalVarDeclarationNode;
import LexicalAnalyzer.Token;
import Traductor.Traductor;

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

    public Method(Token methodToken, String staticScope, Type methodReturnType, String className) {
        this.staticScope = staticScope;
        this.methodToken = methodToken;
        this.methodReturnType = methodReturnType;
        this.parametersList = new ArrayList<>();
        this.parametersTable = new Hashtable<>();
        this.isInherited = false;
        this.principalBlockIsChecked = false;
        this.className = className;
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
        Traductor.getInstance().gen("LOADFP");
        Traductor.getInstance().gen("LOADSP");
        Traductor.getInstance().gen("STOREFP");

        if (this.principalBlock != null)
            this.principalBlock.generateCode();

        //todo chequear si el metodo tiene retorno
    }
}
