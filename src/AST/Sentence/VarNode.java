package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;

public class VarNode extends SentenceNode {

    Token nodeToken;
    ExpressionNode expressionNode;

    public VarNode(Token nodeToken, ExpressionNode expressionNode) {
        super();
        this.nodeToken = nodeToken;
        this.expressionNode = expressionNode;
    }

    @Override
    public void printSentence() {
        System.out.println("nombre var: " + this.nodeToken.getLexeme() + ", expresion: " + this.expressionNode);
    }
}
