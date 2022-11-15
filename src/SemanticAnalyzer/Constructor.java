package SemanticAnalyzer;

import LexicalAnalyzer.Token;
import InstructionGenerator.InstructionGenerator;

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
        InstructionGenerator.getInstance().generateInstruction("Constructor_" + this.constructorToken.getLexeme() + ":");
        InstructionGenerator.getInstance().generateInstruction("LOADFP");
        InstructionGenerator.getInstance().generateInstruction("LOADSP");
        InstructionGenerator.getInstance().generateInstruction("STOREFP");
        InstructionGenerator.getInstance().generateInstruction("STOREFP");
        InstructionGenerator.getInstance().generateInstruction("RET 0");
    }

}
