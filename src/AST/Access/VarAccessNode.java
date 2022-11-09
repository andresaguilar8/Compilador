package AST.Access;

import AST.Sentence.LocalVarDeclarationNode;
import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;
import Traductor.Traductor;

import java.io.IOException;

public class VarAccessNode extends AccessNode {

    private LocalVarDeclarationNode localVar;
    private Attribute attribute;
    private Parameter parameter;

    public VarAccessNode(Token token) {
        super(token);
        this.isAssignable = true;
    }

    @Override
    public boolean isAssignable() {
        return true;
    }

    @Override
    public boolean isCallable() {
        return false;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        Type varType;
        String varName = this.token.getLexeme();
        Method currentMethod = SymbolTable.getInstance().getCurrentMethod();
        if (SymbolTable.getInstance().isMethodParameter(varName, currentMethod)) {
            varType = SymbolTable.getInstance().retrieveParameterType(varName, currentMethod);
            this.parameter = SymbolTable.getInstance().retrieveParameter(varName, currentMethod);
        }
        else
            if (SymbolTable.getInstance().isCurrentBlockLocalVar(varName)) {
                this.localVar = SymbolTable.getInstance().retrieveLocalVar(varName);
                varType = this.localVar.getLocalVarType();
            }
            else {
                ConcreteClass methodClass = currentMethod.getMethodClass();
                    if (SymbolTable.getInstance().isAttribute(varName, methodClass)) {
                        this.attribute = methodClass.getAttributes().get(this.token.getLexeme());
                        if (this.attribute.isInherited())
                            if (!SymbolTable.getInstance().isPublicAttribute(this.token.getLexeme(), methodClass))
                                throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " tiene visibilidad privada y es un atributo heredado");
                        if (!SymbolTable.getInstance().getCurrentMethod().getStaticHeader().equals("static"))
                            varType = SymbolTable.getInstance().retrieveAttribute(varName, methodClass);
                        else
                            throw new SemanticExceptionSimple(this.token, "un metodo estatico no puede acceder a un atributo");
                    }
                    else
                        if (!SymbolTable.getInstance().getCurrentMethod().getStaticHeader().equals("static"))
                            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable local ni un parametro del metodo " + "\"" + currentMethod.getMethodName() + "\"" + " ni un atributo de la clase " + methodClass.getClassName());
                        else
                            throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable local ni un parametro del metodo " + "\"" + currentMethod.getMethodName() + "\"" );
            }
        if (this.encadenado != null) {
            if (!varType.isPrimitive())
                return this.encadenado.check(varType);
            else
                throw new SemanticExceptionSimple(this.token, "el lado izquierdo del encadenado es un tipo primitivo");
        }
        return varType;
    }

    @Override
    public void generateCode() throws IOException {

        //genero codigo para una variable local
        if (this.localVar != null) {
            if (!this.isLeftSide() || this.encadenado != null) //si el acceso a var es lado derecho..
                Traductor.getInstance().gen("LOAD " + this.localVar.getOffset() + " ; Se apila el valor de la variable local o parametro " + this.localVar.getVarName());
            else
                Traductor.getInstance().gen("STORE " + this.localVar.getOffset());
        }

        //genero codigo para un parametro
        if (this.parameter != null) {
            if (!this.isLeftSide() || this.encadenado != null)
                Traductor.getInstance().gen("LOAD " + this.parameter.getOffset() + " ; Se apila el valor del parametro " + this.parameter.getParameterName());
            else
                Traductor.getInstance().gen("STORE " + this.parameter.getOffset());
        }

        //genero codigo para un atributo
        if (this.attribute != null) {
            Traductor.getInstance().gen("LOAD 3");
            if (!this.isLeftSide() || this.encadenado != null) {
                Traductor.getInstance().gen("LOADREF " + this.attribute.getOffset() + "              ; Se apila el valor del atributo " + this.attribute.getAttributeName());
            }
            else {
                Traductor.getInstance().gen("SWAP");
                Traductor.getInstance().gen("STOREREF " + this.attribute.getOffset());
            }
        }

        if (this.encadenado != null)
            encadenado.generateCode();
    }

}
