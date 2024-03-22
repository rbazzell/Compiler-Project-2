package src.parser;

public class AssignExpression extends Expression {

    public IDExpression lhs = null;
    public Expression rhs = null;

    public AssignExpression(IDExpression in_lhs, Expression in_rhs){
        lhs = in_lhs;
        rhs = in_rhs;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
