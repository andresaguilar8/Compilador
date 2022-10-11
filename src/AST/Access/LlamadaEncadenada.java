package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Parameter;

import java.util.ArrayList;

public class LlamadaEncadenada extends Encadenado {

    private ArrayList<Parameter> parametersList;

    public LlamadaEncadenada(Token token) {
        super(token);
    }

    @Override
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public void printExpression() {
        if (this.encadenado == null)
            System.out.println("llamada encadenada con nombre: " + this.token.getLexeme());
        else {
            System.out.println("llamada encadenada con nombre: " + this.token.getLexeme());
            this.encadenado.printExpression();
        }

    }
}
