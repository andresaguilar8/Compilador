package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.PrimitiveType;
import SemanticAnalyzer.Type;
import Traductor.Traductor;

import java.io.IOException;

public class IntNode extends LiteralOperandNode {

    public IntNode(Token token) {
        super(token);
    }

    public Type check() {
        return new PrimitiveType(new Token("pr_int","int",0));
    }

    public void generateCode() throws IOException {
        Traductor.getInstance().gen("PUSH " + this.token.getLexeme());
    }
}
