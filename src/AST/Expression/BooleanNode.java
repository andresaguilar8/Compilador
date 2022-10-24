package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.PrimitiveType;
import SemanticAnalyzer.Type;

public class BooleanNode extends LiteralOperandNode {

    public BooleanNode(Token token) {
        super(token);
    }

    @Override
    public Type check() {
        return new PrimitiveType(new Token("pr_boolean","boolean",0));
    }

    @Override
    public void printExpression() {
        System.out.print(this.token.getLexeme());
    }

}
