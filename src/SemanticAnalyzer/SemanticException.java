package SemanticAnalyzer;
import java.util.ArrayList;

public class SemanticException extends Exception {

    private ArrayList<SemanticError> semanticErrorList;

    public SemanticException(ArrayList<SemanticError> semanticErrorList) {
        this.semanticErrorList = semanticErrorList;
    }

    public String getMessage() {
        String toReturn =  "";
        for (SemanticError semanticError: this.semanticErrorList)
            toReturn += "Error Semantico en linea "
                    + semanticError.getErrorToken().getLineNumber()
                    + ": "
                    + semanticError.getErrorMessage()
                    + this.generateStringError(semanticError);
        return toReturn;
    }

    private String generateStringError(SemanticError semanticError) {
        return "\n\n[Error:" +
                semanticError.getErrorToken().getLexeme()
                + "|"
                + semanticError.getErrorToken().getLineNumber()
                + "]\n\n";
    }

}
