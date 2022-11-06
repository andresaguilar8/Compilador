package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import Traductor.Traductor;

import java.io.IOException;

public class Constructor {

    Token constructorToken;

    public Constructor(Token constructorToken) {
        this.constructorToken = constructorToken;
    }

    public Token getConstructorToken() {
        return this.constructorToken;
    }

    public void generateCode() throws IOException {
        Traductor.getInstance().gen("Constructor_" + this.constructorToken.getLexeme() + ":");
        Traductor.getInstance().gen("LOADFP");
        Traductor.getInstance().gen("LOADSP");
        Traductor.getInstance().gen("STOREFP");
        Traductor.getInstance().gen("STOREFP");
        Traductor.getInstance().gen("RET 0");
//        + this.getReturnOffset());
        //TODO CHEQUEAR
    }

    private int getReturnOffset() {
        ConcreteClass concreteClass = SymbolTable.getInstance().getConcreteClass(this.constructorToken.getLexeme());
        return concreteClass.getCirSize();
    }
}
