package AST.Access;

import LexicalAnalyzer.Token;

public class VarEncadenada extends Encadenado {

    public VarEncadenada(Token token) {
        super(token);
    }

    @Override
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public void printExpression() {
        if (this.encadenado == null)
            System.out.println("variable encadenada de nombre: " + this.token.getLexeme());
        else {
            System.out.println("variable encadenada de nombre: " + this.token.getLexeme());
            encadenado.printExpression();
        }
    }
}
