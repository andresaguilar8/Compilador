package AST.Sentence;

import AST.Expression.ExpressionNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import InstructionGenerator.InstructionGenerator;

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
        //libero memoria de las variables locales
        InstructionGenerator.getInstance().generateInstruction("FMEM " + this.blockOfReturn.getTotalVars() + "         ; Se libera memoria de variables locales despues de un return");

        if (this.method.getReturnType().getClassName().equals("void")) {
            InstructionGenerator.getInstance().generateInstruction("STOREFP            ; Nodo return, se actualiza el FP para que ahora apunte al RA llamador");
            InstructionGenerator.getInstance().generateInstruction("RET " + this.method.getReturnOffset() + "       ; Se liberan " + this.method.getReturnOffset() + " lugares de la pila");
        }
        else {
            this.expressionNode.generateCode();
            InstructionGenerator.getInstance().generateInstruction("STORE " + this.method.getStoringValueInReturnOffset() + "       ; Se coloca el valor de la expresion del return en la locacion que fue reservada para el retorno del metodo");
            InstructionGenerator.getInstance().generateInstruction("STOREFP           ; Nodo return, se actualiza el FP para que ahora apunte al RA llamador");
            InstructionGenerator.getInstance().generateInstruction("RET " + this.method.getReturnOffset() + "       ; Se liberan " + this.method.getReturnOffset() + " lugares de la pila");
        }
    }

}
