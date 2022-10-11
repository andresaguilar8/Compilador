package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

public class UnaryExpressionNode extends ExpressionNode {

    private ExpressionNode operandNode;

    //aca el token es el operador
    public UnaryExpressionNode(Token token, ExpressionNode operandNode) {
        super(token);
        this.operandNode = operandNode;
        this.setType();
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        Type operandType = operandNode.check();
        String operator =  this.token.getLexeme();
        if (operandType.isCompatibleWithOperator(operator))
            return operandType;
        else
            throw new SemanticExceptionSimple(this.token, "El operador " + this.token.getLexeme() + " no es compatible con el tipo " + operandType.getClassName());
    }

    @Override
    public void printExpression() {
        System.out.print(this.token.getLexeme());
        operandNode.printExpression();
    }

    @Override
    public void setType() {
//        if (this.operandNode.token.getLexeme().equals("+"))
            this.expressionType = new PrimitiveType(operandNode.token);
//        else
//            if (this.operandNode.token.getLexeme().equals("!"))
//                this.expressionType = new PrimitiveType(operandNode.token);
    }

}
