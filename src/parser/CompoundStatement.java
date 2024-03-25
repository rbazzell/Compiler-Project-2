package src.parser;

import java.util.ArrayList;

public class CompoundStatement extends Statement {

    public ArrayList<VariableDeclaration> localDecls = null;
    public ArrayList<Statement> stmtList = null;

    public CompoundStatement(ArrayList<VariableDeclaration> in_localDecls, ArrayList<Statement> in_stmtList) {
        localDecls = in_localDecls;
        stmtList = in_stmtList;
    }

    public String print(int indent) {
        String printStr = "\t".repeat(indent) + "{ }\n";
        if(localDecls != null){
            for(int i = 0; i < localDecls.size(); i++){
                printStr += localDecls.get(i).print(indent + 1);
            }
        } 
        if(stmtList != null){
            for(int i = 0; i < stmtList.size(); i++){
                printStr += stmtList.get(i).print(indent + 1);
            }
        } 
        return printStr;
    }
}
