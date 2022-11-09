package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import Traductor.Traductor;

import java.io.IOException;

public class ReturnNode extends SentenceNode {

    private ExpressionNode expressionNode;
    private Method method;
    private BlockNode blockOfReturn;

    public ReturnNode(Token token, ExpressionNode expressionNode) {
        super(token);
        this.expressionNode = expressionNode;
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        Type expressionType = this.expressionNode.check();
        this.method = SymbolTable.getInstance().getCurrentMethod();
        Type returnMethodType = this.method.getReturnType();
        if (expressionType == null && !returnMethodType.getClassName().equals("void"))
                throw new SemanticExceptionSimple(this.token, "El metodo debe retornar una expresion de tipo " + returnMethodType.getClassName());
        if (expressionType != null) {
            if (!expressionType.isCompatibleWithType(returnMethodType))
                if (!returnMethodType.getClassName().equals("void"))
                    throw new SemanticExceptionSimple(this.token, "El metodo debe retornar una expresion de tipo " + returnMethodType.getClassName());
                else
                    throw new SemanticExceptionSimple(this.token, "El metodo no tiene retorno");
        }
        this.setBlockOfReturn(SymbolTable.getInstance().getCurrentBlock());
    }

    private void setBlockOfReturn(BlockNode blockOfReturn) {
        this.blockOfReturn = blockOfReturn;
    }

    @Override
    protected void generateCode() throws IOException {
        //libero memoria de las variables
        //todo acomodar, tiene uqe ser el total de variables del bloque
        Traductor.getInstance().gen("FMEM " + this.blockOfReturn.getTotalVars());
//        + this.method.getReturnOffset());
        if (this.method.getReturnType().equals("void")) {
            Traductor.getInstance().gen("STOREFP");
            Traductor.getInstance().gen("RET " + this.method.getReturnOffset());
        }
        else {
            this.expressionNode.generateCode();
            Traductor.getInstance().gen("STORE " + this.method.getStoringValueInReturnOffset() + "       ; Se coloca el valor de la expresion del return en la locacion que fue reservada para el retorno del metodo");
            Traductor.getInstance().gen("STOREFP");
            Traductor.getInstance().gen("RET " + this.method.getReturnOffset() + "       ; Se liberan " + this.method.getReturnOffset() + " lugares de la pila");
        }
    }

}
