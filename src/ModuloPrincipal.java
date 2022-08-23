import AnalizadorLexico.AnalizadorLexico;
import ManejadorDeArchivo.ManejadorDeArchivo;
import AnalizadorLexico.ExcepcionLexica;
import AnalizadorLexico.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModuloPrincipal {

    public static void main (String [] args) {
//        File file = new File(args[0]);
        File file;
        ManejadorDeArchivo manejadorDeArchivo = null;

//        if (args.length > 0) {
//            file = new File(args[0]);
//            ManejadorDeArchivo manejadorDeArchivo = null;
//            try {
//                manejadorDeArchivo = new ManejadorDeArchivo(file);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            AnalizadorLexico analizadorLexico = new AnalizadorLexico(manejadorDeArchivo);
//        }

        file = new File("src/ArchivoPrueba");
            try {
                manejadorDeArchivo = new ManejadorDeArchivo(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //mapeo de palabras clave
            Map<String, String> mapeoDePalabrasClave = new HashMap<>();
            mapeoDePalabrasClave.put("else", "pr_else");
            mapeoDePalabrasClave.put("if", "pr_if");
            mapeoDePalabrasClave.put("class", "pr_class");
            mapeoDePalabrasClave.put("interface", "pr_interface");
            mapeoDePalabrasClave.put("extends", "pr_extends");
            mapeoDePalabrasClave.put("implements", "pr_implements");
            mapeoDePalabrasClave.put("public", "pr_public");
            mapeoDePalabrasClave.put("private", "pr_private");
            mapeoDePalabrasClave.put("static", "pr_static");
            mapeoDePalabrasClave.put("boolean", "pr_boolean");
            mapeoDePalabrasClave.put("void", "pr_void");
            mapeoDePalabrasClave.put("char", "pr_char");
            mapeoDePalabrasClave.put("int", "pr_int");
            mapeoDePalabrasClave.put("while", "pr_while");
            mapeoDePalabrasClave.put("return", "pr_return");
            mapeoDePalabrasClave.put("var", "pr_var");
            mapeoDePalabrasClave.put("this", "pr_this");
            mapeoDePalabrasClave.put("new", "pr_new");
            mapeoDePalabrasClave.put("null", "pr_null");
            mapeoDePalabrasClave.put("true", "pr_true");
            mapeoDePalabrasClave.put("false", "pr_false");
            mapeoDePalabrasClave.put("dynamic", "pr_dynamic");

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(manejadorDeArchivo, mapeoDePalabrasClave);
            ArrayList<Token> listaTokens = new ArrayList<>();

            try {
                boolean recorrer = true;
                while (recorrer) {
                    Token token = analizadorLexico.proximoToken();
                    listaTokens.add(token);
                    System.out.println(token.toString());
                    if (token.getTokenId() == "EOF") {
                        System.out.println();
                        System.out.println("[SinErrores]");
                        recorrer = false;
                    }
                }

            } catch (IOException | ExcepcionLexica e) {
                System.out.println(e.getMessage());
            }
        }
//    }
}
