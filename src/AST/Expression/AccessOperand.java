package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public abstract class AccessOperand extends OperandNode {

    public AccessOperand(Token token) {
        super(token);
    }

    @Override
    public abstract Type check();

    @Override
    public void printExpression() {

    }

    @Override
    public void setType() {

    }
}
