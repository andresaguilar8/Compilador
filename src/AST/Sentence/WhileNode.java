package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;
import Traductor.Traductor;

import java.io.IOException;

public class WhileNode extends SentenceNode {

    private ExpressionNode condition;
    private SentenceNode sentence;

    //las etiquetas tienen que ser unicas por eso hacemos estatica la variable
    private static int whileEndLabelNumber = 0;
    private static int whileBeginLabelNumber = 0;

    public WhileNode(Token token, ExpressionNode condition, SentenceNode sentence) {
        super(token);
        this.condition = condition;
        this.sentence = sentence;
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

    @Override
    protected void generateCode() throws IOException {
        String whileEndLabel = this.newWhileEndLabel();
        String whileBeginLabel = this.newWhileBeginLabel();

        Traductor.getInstance().gen(whileBeginLabel + ":");        //etiqueta de comienzo del while
        this.condition.generateCode();                                      //se genera el codigo de la condicion
                                                                            //si la condicion es falsa, se produce un salto a la etiqueta de fin del while
        Traductor.getInstance().gen("BF " + whileEndLabel + "               ; Si el tope de la fila es falso, salto a " + whileEndLabel);
        this.sentence.generateCode();                                       //se genera el codigo de la sentencia del while
        Traductor.getInstance().gen("JUMP " + whileBeginLabel);    //se vuelve a saltar al comienzo del while, y se evaluará otra vez la condicion
        Traductor.getInstance().gen(whileEndLabel + ":");
    }

    private String newWhileEndLabel() {
        String labelName = "while_end_label_" + this.whileEndLabelNumber;
        this.whileEndLabelNumber += 1;
        return labelName;
    }

    private String newWhileBeginLabel() {
        String labelName = "while_begin_label_" + this.whileBeginLabelNumber;
        this.whileBeginLabelNumber += 1;
        return labelName;
    }
}
