package AST.Expression;

import LexicalAnalyzer.Token;

public class IntNode extends LiteralOperandNode {

    public IntNode(Token currentToken) {
        super(currentToken);
    }
}
