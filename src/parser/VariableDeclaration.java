package src.parser;

public class VariableDeclaration extends Declaration {
    
    public String id;
    public int num;

    public VariableDeclaration(String in_id, int in_num){
        id = in_id;
        num = in_num;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
