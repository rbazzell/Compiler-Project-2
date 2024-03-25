package src.parser;

public class ExpressionStatement extends Statement {

    public Expression expr = null;

    public ExpressionStatement(Expression in_expr) {
        expr = in_expr;
    }

    public String print(int indent) {
        String printStr = "";
        if(expr != null){
            printStr += expr.print(indent);
        }
        return printStr;
    }
}
