package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.PrimitiveType;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public class AssignmentNode extends SentenceNode {

    private ExpressionNode leftSide;
    private ExpressionNode rightSide;

    public AssignmentNode(Token token, ExpressionNode leftSide, ExpressionNode rightSide) {
        super(token);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public void printSentence() {
        this.leftSide.printExpression();
        System.out.print(token.getLexeme());
        this.rightSide.printExpression();
        System.out.println();
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        //que el lado izquierdo este declarado
        //que lado izq y der sean compatibles con token
        Type leftSideAssignmentType = this.leftSide.check();
        Type rightSideAssignmentType = this.rightSide.check();
        //todo preg error aca, si tengo var x = 10 y dps hago x = true cual es el error?
        if (!leftSideAssignmentType.isCompatibleWithType(rightSideAssignmentType))
            throw new SemanticExceptionSimple(rightSide.getToken(),    leftSide.getToken().getLexeme() + " no es compatible con el tipo " + rightSideAssignmentType.getClassName());
        else
            if (!rightSideAssignmentType.isCompatibleWithType(leftSideAssignmentType))
                throw new SemanticExceptionSimple(leftSideAssignmentType.getToken(),    " el lado derecho de la asignación no es compatible con " + this.token.getLexeme());
            else
                if (!checkAssignmentWithToken(leftSideAssignmentType, rightSideAssignmentType))
                    throw new SemanticExceptionSimple(this.token,    " el lado izquierdo y derecho de la asignación no son compatibles con el operador " + this.token.getLexeme());
    }

    private boolean checkAssignmentWithToken(Type leftSideAssignmentType, Type rightSideAssignmentType) {
        String operator = this.token.getLexeme();
        return leftSideAssignmentType.isCompatibleWithOperator(operator) && rightSideAssignmentType.isCompatibleWithOperator(operator);
    }

}
