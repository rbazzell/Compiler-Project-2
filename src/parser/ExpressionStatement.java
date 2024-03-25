package src.parser;

public class ExpressionStatement extends Statement {

    public Expression expr = null;

    public ExpressionStatement(Expression in_expr) {
        expr = in_expr;
    }

    public String printNode(int indent) {
        String printStr = "";
        if(expr != null){
            printStr += expr.printNode(indent);
        }
        return printStr;
    }
}
