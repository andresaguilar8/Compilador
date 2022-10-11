package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

public class BinaryExpressionNode extends ExpressionNode {

    private ExpressionNode leftSide;
    private ExpressionNode rightSide;

    public BinaryExpressionNode(Token expressionOperator, ExpressionNode leftSide, ExpressionNode rightSide) {
        super(expressionOperator);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        Type leftSideTypeExpression = leftSide.check();
        Type rightSideTypeExpression = rightSide.check();
        String operator = this.token.getLexeme();
        boolean leftSideTypeExpressionIsCompatible = leftSideTypeExpression.isCompatibleWithOperator(operator);
        boolean rightSideTypeExpressionIsCompatible = rightSideTypeExpression.isCompatibleWithOperator(operator);
        if (leftSideTypeExpression.isCompatibleWithOperator(operator) && rightSideTypeExpression.isCompatibleWithOperator(operator)) {
            Type primitiveType = new PrimitiveType(this.token);
            primitiveType.setClassName(this.token);
            return primitiveType;
        }
        else
            if (leftSideTypeExpressionIsCompatible)
                throw new SemanticExceptionSimple(this.token, "El operador " + this.token.getLexeme() + " no es compatible con el tipo " + rightSideTypeExpression.getClassName());
            else
                throw new SemanticExceptionSimple(this.token, "El operador " + this.token.getLexeme() + " no es compatible con el tipo " + leftSideTypeExpression.getClassName());
    }

    @Override
    public void printExpression() {
        this.leftSide.printExpression();
        System.out.print(this.token.getLexeme());
        this.rightSide.printExpression();
    }

    @Override
    public void setType() {

    }

}
