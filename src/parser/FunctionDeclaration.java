package src.parser;

import java.util.ArrayList;

public class FunctionDeclaration extends Declaration {

    public typeSpecifier typeSpec;
    public String id;
    public ArrayList<Param> params = null;
    public CompoundStatement stmt;

    public FunctionDeclaration(typeSpecifier in_type, String in_id, ArrayList<Param> in_params, CompoundStatement in_stmt) {
        typeSpec = in_type;
        id = in_id;
        params = in_params;
        stmt = in_stmt;
    }

    public String print(int indent) {
        String printStr = "\t".repeat(indent);
        printStr += typeSpec.name() + " " + id + "()\n";
        for (Param param : params) {
            printStr += param.print(indent + 1);
        }
        printStr += stmt.print(indent + 1);
        return printStr;
    }
}
