package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public class CharNode extends LiteralOperandNode {

    public CharNode(Token currentToken) {
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
