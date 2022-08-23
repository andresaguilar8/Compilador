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
