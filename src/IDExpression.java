package src;

public class IDExpression extends Expression {
    
    public String idStr;
    public Expression expr = null;

    public IDExpression(String in_id, Expression in_expr){
        idStr = in_id;
        expr = in_expr;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
