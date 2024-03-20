package src;

public class ExpressionStatement extends Statement {
    
    public Expression expr = null;

    public ExpressionStatement(Expression in_expr){
        expr = in_expr;
    }
    
    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
