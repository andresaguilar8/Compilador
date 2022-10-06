package AST.Expression;

import LexicalAnalyzer.Token;

public class CharNode extends LiteralOperandNode {
    public CharNode(Token currentToken) {
        super(currentToken);
    }
}
