package AST.Expression;

import LexicalAnalyzer.Token;

public class NullNode extends LiteralOperandNode {
    public NullNode(Token currentToken) {
        super(currentToken);
    }
}
