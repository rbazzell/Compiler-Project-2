package src;

public class BinaryExpression extends Expression {
    
    public Expression lhs = null;
    public Expression rhs = null;
    public char type;
    
    public BinaryExpression(Expression in_lhs, Expression in_rhs, char in_type){
        lhs = in_lhs;
        rhs = in_rhs;
        type = in_type;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
