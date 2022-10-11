package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;

public class ReturnNode extends SentenceNode {

    private ExpressionNode expressionNode;

    public ReturnNode(Token token, ExpressionNode expressionNode) {
        super(token);
        this.expressionNode = expressionNode;
    }

    @Override
    public void printSentence() {
    if (this.expressionNode != null) {
        System.out.print(this.token.getLexeme() + " con expresion: ");
        this.expressionNode.printExpression();
        System.out.println();
    }
    else
        System.out.println(this.token.getLexeme() + " sin expresion");
    }

    @Override
    public void check() {

    }
}
