package AST.Sentence;

import java.util.ArrayList;

public class BlockNode extends SentenceNode {

    private ArrayList<SentenceNode> sentencesList;

    public BlockNode() {
        super();
        this.sentencesList = new ArrayList<>();
    }

    @Override
    public void printSentence() {

    }

    public void addSentence(SentenceNode sentenceNode) {
        this.sentencesList.add(sentenceNode);
    }

    public ArrayList<SentenceNode> getSentencesList() {
        return this.sentencesList;
    }

}
