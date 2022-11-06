package Traductor;

import SemanticAnalyzer.ConcreteClass;
import SemanticAnalyzer.SymbolTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Traductor {

    private BufferedWriter bufferedWriter;
    private String outputFileName;
    private File outputFile;
    private String currentCodeMode;
    private static Traductor instance = null;

    private Traductor() {

    }

    public static Traductor getInstance() {
        if (instance == null)
            instance = new Traductor();
        return instance;
    }

    public void traducir() throws IOException {
        outputFile = new File(outputFileName);
        FileWriter fileWriter = new FileWriter(outputFile);
        bufferedWriter = new BufferedWriter(fileWriter);
        this.currentCodeMode = ".";
        this.setCodeMode();
        this.generateMainMethodCall();
        this.initSimpleMallocRoutine();
        this.generateClassCode();
        //...
        bufferedWriter.close();
    }

    private void generateMainMethodCall() throws IOException {
        String mainMethodLabel = SymbolTable.getInstance().getMainMethod().getMethodLabel();
        this.gen("PUSH " + mainMethodLabel);
        this.gen("CALL");
        this.gen("HALT");
    }

    private void initSimpleMallocRoutine() throws IOException {
        this.gen("simple_malloc:");
        this.gen("LOADFP");
        this.gen("LOADSP");
        this.gen("STOREFP");
        this.gen("LOADHL");
        this.gen("DUP");
        this.gen("PUSH 1");
        this.gen("ADD");
        this.gen("STORE 4");
        this.gen("LOAD 3");
        this.gen("ADD");
        this.gen("STOREHL");
        this.gen("STOREFP");
        this.gen("RET 1");
    }

    private void generateClassCode() throws IOException {
        for (ConcreteClass concreteClass: SymbolTable.getInstance().getConcreteClassesTable().values())
            concreteClass.generateVT();
        for (ConcreteClass concreteClass: SymbolTable.getInstance().getConcreteClassesTable().values())
            concreteClass.generateCode();

    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void gen(String instruction) throws IOException {
        if (instruction.contains(":")) {
            bufferedWriter.write(instruction);
            bufferedWriter.newLine();
        }
        else {
            bufferedWriter.write("                              " + instruction);
            bufferedWriter.newLine();
        }
    }


    //todo va a haber un metodo para setar modo .code .data .stack y
    public void setDataMode() throws IOException {
        if (!this.currentCodeMode.equals(".DATA")) {
            this.bufferedWriter.write(".DATA");
            this.bufferedWriter.newLine();
            this.currentCodeMode = ".DATA";
        }
    }

    public void setCodeMode() throws IOException {
        if (!this.currentCodeMode.equals(".CODE")) {
            this.bufferedWriter.write(".CODE");
            this.bufferedWriter.newLine();
            this.currentCodeMode = ".CODE";
        }
    }
}
