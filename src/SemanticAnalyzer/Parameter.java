package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class Parameter {

    private Token parameterToken;
    private Type parameterType;

    public Parameter(Token parameterToken, Type parameterType) {
        this.parameterToken = parameterToken;
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return this.parameterToken.getLexeme();
    }

    public Type getParameterType() {
        return this.parameterType;
    }

    public Token getParameterToken() {
        return this.parameterToken;
    }

}
