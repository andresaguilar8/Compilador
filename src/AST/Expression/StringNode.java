package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public class StringNode extends LiteralOperandNode {

    public StringNode(Token currentToken) {
        super(currentToken);
    }

    @Override
    public Type check() {
        return null;
    }

    @Override
    public void printExpression() {
        System.out.print(this.token.getLexeme());
    }
}
