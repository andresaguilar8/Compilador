package AST.Sentence;

import AST.Access.AccessNode;
import AST.Encadenado.Encadenado;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.Type;
import Traductor.Traductor;

import java.io.IOException;

public class CallNode extends SentenceNode {

    private AccessNode accessNode;
    private Type callType;

    public CallNode(Token token, AccessNode accessNode) {
        super(token);
        this.accessNode = accessNode;
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        this.callType = this.accessNode.check();
        if (this.accessNode.getEncadenado() != null) {
            Encadenado accessNodeEncadenado = this.accessNode.getEncadenado();
            while (accessNodeEncadenado.getEncadenado() != null)
                accessNodeEncadenado = accessNodeEncadenado.getEncadenado();
            if (!accessNodeEncadenado.isCallable())
                throw new SemanticExceptionSimple(accessNodeEncadenado.getToken(), "llamada incorrecta");
        }
        else {
            if (!accessNode.isCallable())
                throw new SemanticExceptionSimple(accessNode.getToken(), "llamada incorrecta");
        }
    }

    public void generateCode() throws IOException {
        this.accessNode.generateCode();
        if (!this.callType.getClassName().equals("void"))
            Traductor.getInstance().gen("POP       ; El retorno del metodo invocado no es void por lo que el valor retornado no es asignado a ninguna variable entonces se descarta");
    }

}
