package AST.Expression;

import LexicalAnalyzer.Token;

public class StringNode extends LiteralOperandNode {
    public StringNode(Token currentToken) {
        super(currentToken);
    }
}
