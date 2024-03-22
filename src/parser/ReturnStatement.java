package src.parser;

public class ReturnStatement extends Statement {

    public Expression expr = null;

    public ReturnStatement(Expression in_expr){
        expr = in_expr;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
