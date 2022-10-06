package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public class UnaryExpressionNode extends ExpressionNode {

    private Token unaryExpressionNode;
    private OperandNode operandNode;

    public UnaryExpressionNode(Token unaryExpressionNode, OperandNode operandNode) {
        super();
        this.unaryExpressionNode = unaryExpressionNode;
        this.operandNode = operandNode;
    }

    @Override
    public Type check() {
        return null;
    }

    @Override
    public void printExpression() {
        System.out.println("expresion unaria: " + unaryExpressionNode.getLexeme() + " operando:");// "+ operandNode.printExpression());
    }

}
