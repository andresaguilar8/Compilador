package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;
import Traductor.Traductor;

import java.io.IOException;

public class IfNode extends SentenceNode {

    private ExpressionNode condition;
    private SentenceNode sentence;
    private SentenceNode elseSentence;

    //las etiquetas tienen que ser unicas por eso hacemos estatica la variable
    private static int ifLabelNumber = 0;
    private static int elseLabelNumber = 0;

    public IfNode(Token token, ExpressionNode condition, SentenceNode sentence) {
        super(token);
        this.condition = condition;
        this.sentence = sentence;
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

    @Override
    protected void generateCode() throws IOException {
        String ifEndLabel = this.newIfEndLabel();
        String elseLabel = this.newElseLabel();

        this.condition.generateCode();

        //no hay else
        if (this.elseSentence != null) {
            Traductor.getInstance().gen("BF " + elseLabel + "      ; Si el tope de la pila es falso, salto a " + elseLabel);
            this.sentence.generateCode();
            Traductor.getInstance().gen("JUMP " + ifEndLabel);
            Traductor.getInstance().gen(elseLabel + ":");
            this.elseSentence.generateCode();
        }
        else {
            Traductor.getInstance().gen("BF " + ifEndLabel + "       ; Si el tope de la fila es falso, salto a " + ifEndLabel);
            this.sentence.generateCode();
        }
        Traductor.getInstance().gen(ifEndLabel + ":");
        Traductor.getInstance().gen("NOP");
    }

    private String newIfEndLabel() {
        String labelName = "if_end_label_" + this.ifLabelNumber;
        this.ifLabelNumber += 1;
        return labelName;
    }

    private String newElseLabel() {
        String labelName = "else_label_" + this.elseLabelNumber;
        this.elseLabelNumber += 1;
        return labelName;
    }
}
