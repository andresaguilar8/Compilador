package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import java.util.ArrayList;
import java.util.Hashtable;

public class Method {

    private String staticMethod;
    private Token methodToken;
    private String methodName;
    private Type methodReturnType;
    private ArrayList<Parameter> parametersList;
    private Hashtable<String, Parameter> parametersTable;

    public Method(Token methodToken, String staticMethod, Type methodReturnType) {
        this.staticMethod = staticMethod;
        this.methodToken = methodToken;
        this.methodName = this.methodToken.getLexeme();
        this.methodReturnType = methodReturnType;
        this.parametersList = new ArrayList<>();
        this.parametersTable = new Hashtable<>();
    }

    public void insertParameter(Parameter parameterToInsert) throws SemanticException {
        if (!this.parametersTable.containsKey(parameterToInsert.getParameterName())) {
            this.parametersTable.put(parameterToInsert.getParameterName(), parameterToInsert);
            this.parametersList.add(parameterToInsert);
        }
        else
            throw new SemanticException(parameterToInsert.getParameterToken(), "El parametro " + parameterToInsert.getParameterName() + " ya esta declarado en el metodo " + "\"" + this.methodName + "\"");
    }

    public String getMethodName() {
        return this.methodToken.getLexeme();
    }

    public String getStaticHeader() {
        return this.staticMethod;
    }

    public String getReturnType() {
        return this.methodReturnType.getClassName();
    }

    public ArrayList<Parameter> getParametersList() {
        return this.parametersList;
    }

    public void checkDeclaration() throws SemanticException {
        this.checkNoPrimitiveParameters();
        if (!this.methodReturnType.isPrimitive())
            if (!this.classIsDeclared())
                throw new SemanticException(this.methodReturnType.getToken(), "El tipo de retorno del metodo " + this.methodName + " no es una clase declarada");
    }

    private void checkNoPrimitiveParameters() throws SemanticException {
        for (Parameter parameter: this.parametersTable.values()) {
            if (!parameter.getParameterType().isPrimitive())
                if (!parameterTypeIsDeclared(parameter)) {
                    Token parameterTypeToken = parameter.getParameterType().getToken();
                    throw new SemanticException(parameterTypeToken, "El tipo del parametro " + "\"" + parameter.getParameterName() + "\"" + " del metodo " + "\"" + this.methodName + "\"" + " no esta declarado");
                }
        }
    }

    private boolean parameterTypeIsDeclared(Parameter parameter) {
        Type parameterType = parameter.getParameterType();
        String parameterClass = parameterType.getClassName();
        return SymbolTable.getInstance().classIsDeclared(parameterClass);
    }

    public boolean correctRedefinedMethodHeader(Method ancestorMethod) {
        return this.methodsHeadersAreEquals(ancestorMethod);
    }

    public boolean methodsHeadersAreEquals(Method ancestorMethod) {
        if (!ancestorMethod.getStaticHeader().equals(this.staticMethod) || !ancestorMethod.getReturnType().equals(this.methodReturnType.getClassName()) || !this.hasEqualsParameters(ancestorMethod))
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

    private boolean classIsDeclared() {
        return SymbolTable.getInstance().classIsDeclared(this.methodReturnType.getClassName());
    }

    public Token getMethodToken() {
        return this.methodToken;
    }

    public boolean hasParameters() {
        return this.parametersList.size() != 0;
    }
}
