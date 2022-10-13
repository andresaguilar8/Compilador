package AST.Sentence;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.SemanticExceptionSimple;

public class EmptySentence extends SentenceNode {

    public EmptySentence(Token token) {
        super(token);
    }

    @Override
    public void printSentence() {

    }

    @Override
    public void check() throws SemanticExceptionSimple {
        System.out.println("emptysentence");
    }
}
