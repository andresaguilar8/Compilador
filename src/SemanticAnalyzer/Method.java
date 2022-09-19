package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.ArrayList;
import java.util.Hashtable;

public class Method {

    private String baseClassName;
    private String staticMethod;
    private Token methodToken;
    private String methodName;
    private Type methodReturnType;
    private ArrayList<Parameter> parametersList;
    private Hashtable<String, Parameter> parametersTable;

    public Method(Token methodToken, String staticMethod, Type methodReturnType, String baseClassName) {
        this.staticMethod = staticMethod;
        this.methodToken = methodToken;
        this.methodName = this.methodToken.getLexeme();
        this.methodReturnType = methodReturnType;
        this.baseClassName = baseClassName;
        this.parametersList = new ArrayList<>();
        this.parametersTable = new Hashtable<>();
    }

    public void insertParameter(Parameter parameterToInsert) throws SemanticException {
        if (!this.parametersTable.containsKey(parameterToInsert.getParameterName())) {
            this.parametersTable.put(parameterToInsert.getParameterName(), parameterToInsert);
            this.parametersList.add(parameterToInsert);
        }
        else
            throw new SemanticException(this.methodToken, "El parametro " + parameterToInsert.getParameterName() + " ya esta declarado en el metodo " + this.methodName);
    }

    public String getMethodName() {
        return this.methodToken.getLexeme();
    }

    public String getStaticHeader() {
        return this.staticMethod;
    }

    public String getReturnType() {
        return this.methodReturnType.getTypeName();
    }

    public ArrayList<Parameter> getParametersList() {
        return this.parametersList;
    }

    public void checkDeclaration(String ancestorClassName) throws SemanticException {

        //si clase padre tiene mismo nombre, verificar que toda la declaracion sea igual sino --> ERROR

        if (!this.methodReturnType.isPrimitive())
            if (!this.classIsDeclared())
                throw new SemanticException(this.methodToken, "El tipo de retorno del metodo " + this.methodName + " no es una clase declarada");
        if (methodAlreadyExist())
            if (!this.correctRedefinedMethodHeader())
                throw new SemanticException(this.methodToken, "El metodo " + this.methodName + " esta incorrectamente redefinido");
    }

    private boolean correctRedefinedMethodHeader() {
        Class ancestorClass = this.getAncestorClass();
        System.out.println(ancestorClass.getClassName());
        Method ancestorMethod = ancestorClass.getMethod(this.methodName);
        System.out.println("metodo ancestro: "+ancestorMethod.getMethodName());
        boolean correctHeader = this.compareMethodsHeaders(ancestorMethod);
        return correctHeader;
    }

    private boolean compareMethodsHeaders(Method ancestorMethod) {
        boolean correctHeader = true;
        if (!ancestorMethod.getStaticHeader().equals(this.staticMethod) || !ancestorMethod.getReturnType().equals(this.methodReturnType.getTypeName()) || !this.parametersListAreEquals(ancestorMethod))
            correctHeader = false;
        return correctHeader;
    }

    private boolean parametersListAreEquals(Method ancestorMethod) {
        boolean parametersAreEquals;
        if (ancestorMethod.getParametersList().size() == this.parametersList.size()) {
            parametersAreEquals = true;
            int parameterIndex = 0;
            while (parametersAreEquals && (parameterIndex < this.parametersList.size())) {
                Parameter ancestorParameter = ancestorMethod.getParametersList().get(parameterIndex);
                if (!parametersAreEquals(ancestorParameter, parameterIndex))
                    parametersAreEquals = false;
                parameterIndex = parameterIndex + 1;
            }
        }
        else
            parametersAreEquals = false;
        return parametersAreEquals;
    }

    private boolean parametersAreEquals(Parameter parameterToCompareWith, int parameterIndex) {
        Parameter parameterOfThisMethod = this.parametersList.get(parameterIndex);
        if (!parameterToCompareWith.getParameterType().getTypeName().equals(parameterOfThisMethod.getParameterType().getTypeName()))
            return false;
        return true;
    }

    private boolean classIsDeclared() {
        return SymbolTable.getInstance().classIsDeclared(this.methodReturnType.getTypeName());
    }

    private boolean methodAlreadyExist() {
        Class ancestorClass = this.getAncestorClass();
        return ancestorClass.methodIsDeclared(this.methodName);
    }

    private Class getAncestorClass() {
        String ancestorClassName = SymbolTable.getInstance().getClass(this.baseClassName).getAncestorClassName();
        return SymbolTable.getInstance().getClass(ancestorClassName);
    }

    public Token getMethodToken() {
        return this.methodToken;
    }
}
