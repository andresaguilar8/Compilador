package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.Type;

public class ThisAccessNode extends AccessNode {

    private String className;

    public ThisAccessNode(Token token, String className) {
        super(token);
        this.className = className;
    }

    @Override
    public Type check() {
        return null;
    }

    @Override
    public void printExpression() {
        System.out.print(this.token.getLexeme() + " de tipo clase " + this.className);
        if (this.encadenado != null) {
            System.out.print(" y tiene encadenado: ");
            encadenado.printExpression();
        }
    }

    @Override
    public void setType() {

    }

    @Override
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }
}
