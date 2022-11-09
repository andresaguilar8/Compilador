package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.ReferenceType;
import SemanticAnalyzer.Type;
import Traductor.Traductor;

import java.io.IOException;

public class StringNode extends LiteralOperandNode {

    private static int stringLabelNumber = 0;

    public StringNode(Token currentToken) {
        super(currentToken);
    }

    @Override
    public Type check() {
        return new ReferenceType(new Token("idClase", "String", 0));
    }

    @Override
    public void generateCode() throws IOException {
        //todo no se recomienda alojar los Strings de MiniJava en .data
        //todo ya que se pueden manejar creando objetos en el .heap
        Traductor.getInstance().setDataMode();
        String label = this.generateStringLabel();
        String instruction = label + ":";
        Traductor.getInstance().gen(instruction);
        Traductor.getInstance().gen("DW " + this.token.getLexeme() + ", 0");

        Traductor.getInstance().setCodeMode();
        Traductor.getInstance().gen("PUSH " + label);
    }

    private String generateStringLabel() {
        String label =  "str_label_" + this.stringLabelNumber;
        this.stringLabelNumber += 1;
        return label;
    }
}
