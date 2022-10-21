package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;

public class WhileNode extends SentenceNode {

    private ExpressionNode condition;
    private SentenceNode sentence;

    public WhileNode(Token token, ExpressionNode condition, SentenceNode sentence) {
        super(token);
        this.condition = condition;
        this.sentence = sentence;
    }

    @Override
    public void printSentence() {
        System.out.println("while con condicion: " + this.condition + " y la/las sentencias es/son: ");
        this.sentence.printSentence();
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        Type conditionType = this.condition.check();
        if (conditionType != null)
            if (conditionType.isPrimitive() && conditionType.getClassName().equals("boolean"))
                this.sentence.check();
            else
                throw new SemanticExceptionSimple(this.token, "La condicion del while debe ser de tipo primitivo boolean");
    }
}
