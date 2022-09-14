import LexicalAnalyzer.LexicalAnalyzer;
import FileHandler.FileHandler;
import LexicalAnalyzer.LexicalException;
import LexicalAnalyzer.Token;
import SyntacticAnalyzer.SyntacticAnalyzer;
import SyntacticAnalyzer.SyntacticException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainModule {

    public static void main (String [] args) {
//        File file = new File(args[0]);
        File file = null;
        FileHandler fileHandler = null;

//        try {
//            file = new File(args[0]);
//        }catch (ArrayIndexOutOfBoundsException exception) {
//            exception.printStackTrace();
//        }
//
        file = new File("src/ArchivoPrueba.txt");

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
            lexicalAnalyzer = new LexicalAnalyzer(fileHandler, keywordDictionary);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

//        try {
//            lexicalAnalyzer = new LexicalAnalyzer(fileHandler, keywordDictionary);
//            syntaxAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);
//
//            System.out.println("Compilación Exitosa\n\n");
//            System.out.println("[SinErrores]");
//
//        } catch (IOException | LexicalException | SyntacticException exception) {
//            System.out.println(exception.getMessage());
//        }

            getTokens(lexicalAnalyzer);

    }




    private static void getTokens(LexicalAnalyzer lexicalAnalyzer)  {
        ArrayList<Token> tokensList = new ArrayList<>();
        boolean tokensLeft = true;

        while (tokensLeft) {
            Token token = null;
            try {
                token = lexicalAnalyzer.nextToken();
            } catch (IOException | LexicalException exception) {
                System.out.println(exception.getMessage());
                token = null;
            }
            tokensList.add(token);
            if (token != null)
                if (token.getTokenId().equals("EOF") && !lexicalAnalyzer.hasLexicalErrors()) {
                    for (Token tokenToPrint : tokensList)
                        System.out.println(tokenToPrint.toString());
                    System.out.println("\n[SinErrores]");
                    tokensLeft = false;
                }
                else
                    if (token.getTokenId().equals("EOF"))
                        tokensLeft = false;
        }
    }

}