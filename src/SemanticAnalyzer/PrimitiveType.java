package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.Arrays;

public class PrimitiveType extends Type {

    public PrimitiveType(Token tokenType) {
        super(tokenType);
        this.setClassName(tokenType);
    }

    public boolean isPrimitive() {
        return true;
    }

    @Override
    public void setClassName(Token tokenType) {
        if (Arrays.asList("&&", "!", "||", "<", ">", "<=", ">=", "==", "!=").contains(tokenType.getLexeme()))
            this.className = "boolean";
        else
        if (Arrays.asList("-", "+", "*", "/", "%").contains(tokenType.getLexeme()))
            this.className = "int";
    }

    public String getClassName() {
        return this.className;
    }

    @Override
    public boolean isCompatibleWithOperator(String operator) {
        if (Arrays.asList("-", "+", "*", "/", "<", ">", "<=", ">=", "==", "!=", "+=", "-=", "=").contains(operator) && this.getClassName().equals("int"))
            return true;
        else
            if (Arrays.asList("&&", "!", "||", "!=", "==", "=").contains(operator) && this.getClassName().equals("boolean"))
                return true;
        return false;
    }

    @Override
    public boolean isCompatibleWithType(Type rightSideAssignmentType) {
        if (this.className.equals("int"))
            return rightSideAssignmentType.getClassName().equals("int");
        else
            if (this.className.equals("boolean"))
                return rightSideAssignmentType.getClassName().equals("boolean");
            else
                return false;
            //todo terminar
    }
}
