package src.parser;
import java.util.ArrayList;

public class FunctionDeclaration extends Declaration {
    
    public typeSpecifier typeSpec;
    public ArrayList<Param> params = null;
    public CompoundStatement stmt;

    public FunctionDeclaration(typeSpecifier in_type, ArrayList<Param> in_params, CompoundStatement in_stmt){
        typeSpec = in_type;
        params = in_params;
        stmt = in_stmt;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
