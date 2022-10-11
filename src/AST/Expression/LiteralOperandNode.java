package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public abstract class LiteralOperandNode extends OperandNode {

    public LiteralOperandNode(Token token) {
        super(token);
    }

    public abstract Type check();

    public abstract void printExpression();

    @Override
    public void setType() {

    }

}
