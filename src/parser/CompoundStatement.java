package src.parser;

import java.util.ArrayList;

public class CompoundStatement extends Statement {

    public ArrayList<VariableDeclaration> localDecls = null;
    public ArrayList<Statement> stmtList = null;

    public CompoundStatement(ArrayList<VariableDeclaration> in_localDecls, ArrayList<Statement> in_stmtList) {
        localDecls = in_localDecls;
        stmtList = in_stmtList;
    }

    public String printNode(int indent) {
        // Implement Print
        return null;
    }
}
