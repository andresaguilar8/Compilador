package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.ArrayList;

public class Method {

    private String staticMethod;
    private Token methodToken;
    private Type methodType;
    private ArrayList<Parameter> parametersList;

    public Method(Token methodToken, String staticMethod, Type methodType) {
        this.staticMethod = staticMethod;
        this.methodToken = methodToken;
        this.methodType = methodType;
        this.parametersList = new ArrayList<>();
    }

    public void insertParameter(Parameter parameterToInsert) {
        this.parametersList.add(parameterToInsert);
    }

    public String getMethodName() {
        return this.methodToken.getLexeme();
    }

}
