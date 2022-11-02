import LexicalAnalyzer.LexicalAnalyzer;
import FileHandler.FileHandler;
import LexicalAnalyzer.LexicalException;
import SemanticAnalyzer.SemanticException;
import SemanticAnalyzer.SemanticExceptionSimple;
import SemanticAnalyzer.SymbolTable;
import SyntacticAnalyzer.SyntacticAnalyzer;
import SyntacticAnalyzer.SyntacticException;
import Traductor.Traductor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainModule {


    public static void main (String [] args) {

        String outputName = args[1];

        File file = new File(args[0]);
        FileHandler fileHandler = null;

        try {
            fileHandler = new FileHandler(file);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Map<String, String> keywordDictionary = new HashMap<>();
        keywordDictionary.put("else", "pr_else");
        keywordDictionary.put("if", "pr_if");
        keywordDictionary.put("class", "pr_class");
        keywordDictionary.put("interface", "pr_interface");
        keywordDictionary.put("extends", "pr_extends");
        keywordDictionary.put("implements", "pr_implements");
        keywordDictionary.put("public", "pr_public");
        keywordDictionary.put("private", "pr_private");
        keywordDictionary.put("static", "pr_static");
        keywordDictionary.put("boolean", "pr_boolean");
        keywordDictionary.put("void", "pr_void");
        keywordDictionary.put("char", "pr_char");
        keywordDictionary.put("int", "pr_int");
        keywordDictionary.put("while", "pr_while");
        keywordDictionary.put("return", "pr_return");
        keywordDictionary.put("var", "pr_var");
        keywordDictionary.put("this", "pr_this");
        keywordDictionary.put("new", "pr_new");
        keywordDictionary.put("null", "pr_null");
        keywordDictionary.put("true", "pr_true");
        keywordDictionary.put("false", "pr_false");

        LexicalAnalyzer lexicalAnalyzer = null;
        SyntacticAnalyzer syntaxAnalyzer = null;

        try {

            SymbolTable.getInstance().emptySymbolTable();

            lexicalAnalyzer = new LexicalAnalyzer(fileHandler, keywordDictionary);
            syntaxAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);

            SymbolTable.getInstance().checkDeclarations();
            SymbolTable.getInstance().consolidate();

            if (SymbolTable.getInstance().getSemanticErrorsList().size() > 0)
                throw new SemanticException(SymbolTable.getInstance().getSemanticErrorsList());




        } catch (IOException | LexicalException | SyntacticException | SemanticException | SemanticExceptionSimple exception) {
            System.out.println(exception.getMessage());
        }

        try {
            SymbolTable.getInstance().checkSentences();
            Traductor.getInstance().setOutputFileName(outputName);
            Traductor.getInstance().traducir();
            System.out.println("Compilación Exitosa\n\n");
            System.out.println("[SinErrores]");
        } catch (SemanticExceptionSimple exceptionSimple) {
            System.out.println(exceptionSimple.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}