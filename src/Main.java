import LexicalAnalyzer.LexicalAnalyzer;
import FileHandler.FileHandler;
import LexicalAnalyzer.LexicalException;
import LexicalAnalyzer.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main (String [] args) {
//        File file = new File(args[0]);
        File file = null;
        FileHandler fileHandler = null;
//
//        if (args.length > 0) {
//            file = new File(args[0]);
//        }
//
        file = new File("src/ArchivoPrueba");
        try {
            fileHandler = new FileHandler(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        try {
            lexicalAnalyzer = new LexicalAnalyzer(fileHandler, keywordDictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Token> tokensList = new ArrayList<>();

        try {
            boolean quedanTokens = true;
            while (quedanTokens) {
                Token token = lexicalAnalyzer.nextToken();
                tokensList.add(token);
                if (token.getTokenId() == "EOF") {
                    for (Token tokenAImprimir: tokensList)
                        System.out.println(tokenAImprimir.toString());
                    System.out.println("\n[SinErrores]");
                    quedanTokens = false;
                }
            }

        } catch (IOException | LexicalException e) {
            System.out.println(e.getMessage());
        }
    }
}
