package src;

public class VariableDeclaration extends Declaration {
    
    public IDExpression id;
    public NUMExpression num;

    public VariableDeclaration(IDExpression in_id, NUMExpression in_num){
        id = in_id;
        num = in_num;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
