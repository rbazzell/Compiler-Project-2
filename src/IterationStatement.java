package src;

public class IterationStatement extends Statement {
    
    public Expression expr;
    public Statement stmt;

    public IterationStatement(Expression in_expr, Statement in_stmt){
        expr = in_expr;
        stmt = in_stmt;
    }
        
    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
