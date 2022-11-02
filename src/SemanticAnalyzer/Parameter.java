package SemanticAnalyzer;

import LexicalAnalyzer.Token;

public class Parameter {

    private Token parameterToken;
    private Type parameterType;
    private int offset;

    public Parameter(Token parameterToken, Type parameterType) {
        this.parameterToken = parameterToken;
        this.parameterType = parameterType;
    }

    public int getOffset() {
        return this.offset;
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
