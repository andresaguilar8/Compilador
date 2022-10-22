package AST.Sentence;

import AST.Access.AccessNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;

public class CallNode extends SentenceNode {

    private AccessNode accessNode;

    public CallNode(Token token, AccessNode accessNode) {
        super(token);
        this.accessNode = accessNode;
    }

    @Override
    public void printSentence() {
        System.out.println("la expresion del call node es: ");
        this.accessNode.printExpression();
    }

    @Override
    public void check() throws SemanticExceptionSimple {
        System.out.println("caalll");
        System.out.println(this.accessNode);
        this.accessNode.check();
    }
}
