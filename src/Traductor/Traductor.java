package Traductor;

import SemanticAnalyzer.ConcreteClass;
import SemanticAnalyzer.Method;
import SemanticAnalyzer.SymbolTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Traductor {

    private BufferedWriter bufferedWriter;
    private String outputFileName;
    private File outputFile;
    private String codeMode;

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
        //bufferedWriter.write(".CODE");
        this.generateMainMethodCall();
        this.generateClassCode();
        //...
        bufferedWriter.close();
    }

    private void generateMainMethodCall() throws IOException {
        String mainMethodLabel = SymbolTable.getInstance().getMainMethod().toString();
        bufferedWriter.write(".CODE");
        bufferedWriter.newLine();
        bufferedWriter.write("PUSH " + mainMethodLabel);
        bufferedWriter.newLine();
        bufferedWriter.write("CALL");
        bufferedWriter.newLine();
        bufferedWriter.write("HALT");
        bufferedWriter.newLine();
    }

    private void generateClassCode() throws IOException {
        for (ConcreteClass concreteClass: SymbolTable.getInstance().getConcreteClassesTable().values()) {
            for (Method method: concreteClass.getMethods().values()) {
                method.generateCode();
            }
        }
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void gen(String instruction) throws IOException {
        bufferedWriter.write(instruction);
        bufferedWriter.newLine();
    }


    //todo va a haber un metodo para setar modo .code .data .stack y
    public void setCodeMode() throws IOException {
//        this.codeMode = ".CODE";
        this.bufferedWriter.write(".CODE");
    }
}
