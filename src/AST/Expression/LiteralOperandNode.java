package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public class LiteralOperandNode extends OperandNode {

    public LiteralOperandNode(Token currentToken) {
        super();
    }

    @Override
    public Type check() {
        return null;
    }

    @Override
    public void printExpression() {

    }

}
