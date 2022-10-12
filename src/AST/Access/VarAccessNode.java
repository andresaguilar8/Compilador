package AST.Access;

import LexicalAnalyzer.Token;
import SemanticAnalyzer.*;

public class VarAccessNode extends AccessNode {

    public VarAccessNode(Token token) {
        super(token);
        this.isAssignable = true;
    }

    @Override
    public Type check() throws SemanticExceptionSimple {
        Type varType;
        String varName = this.token.getLexeme();
        Method currentMethod = SymbolTable.getInstance().getCurrentMethod();
        if (SymbolTable.getInstance().isMethodParameter(varName, currentMethod))
            varType = SymbolTable.getInstance().retrieveParameterType(varName, currentMethod);
        else
            if (SymbolTable.getInstance().isCurrentBlockLocalVar(varName))
                varType = SymbolTable.getInstance().retrieveLocalVarType(varName);
            else {
                ConcreteClass currentClass = (ConcreteClass) SymbolTable.getInstance().getCurrentClass();
                if (SymbolTable.getInstance().isAttribute(varName, currentClass) && SymbolTable.getInstance().getCurrentMethod().getStaticHeader().equals(""))
                    varType = SymbolTable.getInstance().retrieveAttribute(varName, currentClass);
                else
                    throw new SemanticExceptionSimple(this.token, this.token.getLexeme() + " no es una variable local ni un parametro del metodo " + currentMethod.getMethodName() + " ni un atributo de la clase " + currentClass.getClassName());
            }
        if (this.encadenado != null)
            return this.encadenado.check(varType);
        return varType;
    }


    @Override
    public void printExpression() {
        System.out.print(this.token.getLexeme());
    }

    @Override
    public void setType() {

    }

}
