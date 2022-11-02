package AST.Sentence;

import AST.Access.AccessNode;
import AST.Encadenado.Encadenado;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;

import java.io.IOException;

public class CallNode extends SentenceNode {

    private AccessNode accessNode;

    public CallNode(Token token, AccessNode accessNode) {
        super(token);
        this.accessNode = accessNode;
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        this.accessNode.check();
//        Encadenado accessNodeEncadenado = this.accessNode.getEncadenado();
        if (this.accessNode.getEncadenado() != null) {
            Encadenado accessNodeEncadenado = this.accessNode.getEncadenado();
            while (accessNodeEncadenado.getEncadenado() != null)
                accessNodeEncadenado = accessNodeEncadenado.getEncadenado();
            System.out.println(accessNodeEncadenado.isCallable());
            if (!accessNodeEncadenado.isCallable())
                throw new SemanticExceptionSimple(accessNodeEncadenado.getToken(), "llamada incorrecta");
        }
        System.out.println(accessNode.getToken());
//        if (!accessNode.isCallable())
//            throw new SemanticExceptionSimple(accessNode.getToken(), "llamada incorrecta");
    }

    public void generateCode() throws IOException {
        System.out.println(this.token);
        this.accessNode.generateCode();
    }

}
