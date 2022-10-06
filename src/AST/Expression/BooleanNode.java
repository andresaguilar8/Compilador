package AST.Expression;

import LexicalAnalyzer.Token;

public class BooleanNode extends LiteralOperandNode {

    public BooleanNode(Token currentToken) {
        super(currentToken);
    }
}
