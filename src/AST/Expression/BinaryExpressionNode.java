package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public class BinaryExpressionNode extends ExpressionNode {

    private ExpressionNode leftSide;
    private ExpressionNode rightSide;
    private Token expressionOperator;

    public BinaryExpressionNode() {

    }

    @Override
    public Type check() {
        return null;
    }

    @Override
    public void printExpression() {

    }

}
