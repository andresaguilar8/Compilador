package SemanticAnalyzer;

import LexicalAnalyzer.Token;

import java.util.Arrays;

public class PrimitiveType extends Type {

    public PrimitiveType(Token tokenType) {
        super(tokenType);
//        this.setClassName(tokenType);
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
        if (Arrays.asList("-", "+", "*", "/", "<", ">", "<=", ">=", "==", "!=", "+=", "-=", "=", "%").contains(operator) && this.getClassName().equals("int"))
            return true;
        else
            if (Arrays.asList("&&", "!", "||", "!=", "==", "=").contains(operator) && this.getClassName().equals("boolean"))
                return true;
            else
                if (Arrays.asList("=", "==", "!=").contains(operator) && this.getClassName().equals("char"))
                    return true;
                //todo puede que falten cosas
        return false;
    }

    @Override
    public boolean isCompatibleWithType(Type typeToCompareWith) {
        if (!typeToCompareWith.isPrimitive())
            return false;
        if (this.className.equals("int"))
            return typeToCompareWith.getClassName().equals("int");
        if (this.className.equals("boolean"))
            return typeToCompareWith.getClassName().equals("boolean");
        if (this.className.equals("char"))
            return typeToCompareWith.getClassName().equals("char");
        if (this.className.equals("void"))
            return typeToCompareWith.getClassName().equals("void");
        return false;
            //todo terminar
    }
}
