package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public class IfNode extends SentenceNode {

    private ExpressionNode condition;
    private SentenceNode sentence;
    private SentenceNode elseSentence;

    public IfNode(Token token, ExpressionNode condition, SentenceNode sentence) {
        super(token);
        this.condition = condition;
        this.sentence = sentence;
    }

    @Override
    public void printSentence() {
        System.out.print(this.token.getLexeme() + " condicion: ");
        this.condition.printExpression();
        System.out.println(" sentencia: ");
        this.sentence.printSentence();
    }

    public void setElseSentence(SentenceNode elseSentence) {
        this.elseSentence = elseSentence;
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        Type conditionType = this.condition.check();
        if (conditionType != null)
            if (conditionType.isPrimitive() && conditionType.getClassName().equals("boolean"))
                this.sentence.check();
            else
               throw new SemanticExceptionSimple(this.token, "La condicion del if debe ser de tipo primitivo boolean");
        if (this.elseSentence != null)
            elseSentence.check();
    }

}
