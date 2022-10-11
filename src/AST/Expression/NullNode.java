package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public class NullNode extends LiteralOperandNode {

    public NullNode(Token currentToken) {
        super(currentToken);
    }

    @Override
    public Type check() {
        return null;
    }

    public void printExpression() {
        System.out.print(this.token.getLexeme());
    }

}
