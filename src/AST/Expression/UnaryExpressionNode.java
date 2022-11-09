package AST.Expression;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import Traductor.Traductor;

import java.io.IOException;

public class UnaryExpressionNode extends ExpressionNode {

    private ExpressionNode operandNode;

    //aca el token es el operador
    public UnaryExpressionNode(Token token, OperandNode operandNode) {
        super(token);
        this.operandNode = operandNode;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        Type operandType = operandNode.check();
        String operator =  this.token.getLexeme();
        if (operandType.isCompatibleWithOperator(operator))
            return operandType;
        else
            throw new SemanticExceptionSimple(this.token, "El operador " + this.token.getLexeme() + " no es compatible con el tipo " + operandType.getClassName());
    }

    @Override
    public void generateCode() throws IOException {
        System.out.println("q");
        this.operandNode.generateCode();
        if (this.token.getLexeme().equals("!"))
            Traductor.getInstance().gen("NOT");
        if (this.token.getLexeme().equals("-"))
            Traductor.getInstance().gen("NEG");
    }

}
