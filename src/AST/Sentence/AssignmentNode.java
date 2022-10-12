package AST.Sentence;

import AST.Access.AccessNode;
import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public class AssignmentNode extends SentenceNode {

    private AccessNode leftSide;
    private ExpressionNode rightSide;

    public AssignmentNode(Token token, AccessNode leftSide, ExpressionNode rightSide) {
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
        if (this.leftSide.getEncadenado() == null) {
            if (leftSide.isAssignable()) {
                Type leftSideAssignmentType = this.leftSide.check();
                Type rightSideAssignmentType = this.rightSide.check();
                if (!leftSideAssignmentType.isCompatibleWithType(rightSideAssignmentType))
                    throw new SemanticExceptionSimple(this.token, leftSide.getToken().getLexeme() + " no conforma con el tipo " + rightSideAssignmentType.getClassName());
                if (!bothSidesAreCompatibleWithOperand(leftSideAssignmentType, rightSideAssignmentType))
                    throw new SemanticExceptionSimple(this.token, "el lado izquierdo y derecho de la asignación no son compatibles con el operador " + this.token.getLexeme());
            } else
                throw new SemanticExceptionSimple(this.token, "El lado izquierdo de la asignación no es asignable");
        }
        else
            this.leftSide.check();
    }

    private boolean bothSidesAreCompatibleWithOperand(Type leftSideAssignmentType, Type rightSideAssignmentType) {
        String operator = this.token.getLexeme();
        return leftSideAssignmentType.isCompatibleWithOperator(operator) && rightSideAssignmentType.isCompatibleWithOperator(operator);
    }

}
