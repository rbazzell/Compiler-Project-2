package src.parser;
import java.util.ArrayList;

public class Program implements Node{
    
    public ArrayList<Declaration> decls;

    public Program(ArrayList<Declaration> in_decls){
        decls = in_decls;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
