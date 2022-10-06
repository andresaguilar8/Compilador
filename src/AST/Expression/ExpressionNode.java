package AST.Expression;

import SemanticAnalyzer.Type;

public abstract class ExpressionNode {

    public abstract Type check();

    public abstract void printExpression();
    
}
