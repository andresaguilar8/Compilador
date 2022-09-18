package SemanticAnalyzer;

import java.beans.Visibility;

public class Atribute {

    private String visibility;
    private String atributeName;
    private Type atributeType;

    public Atribute(String atributeName, Type atributeType, String visibility) {
        this.atributeName = atributeName;
        this.atributeType = atributeType;
        this.visibility = visibility;
    }

    public String getAtributeName() {
        return this.atributeName;
    }

}
