package src.parser;

public class IDExpression extends Expression {

    public String idStr;
    public Expression expr = null;

    public IDExpression(String in_id, Expression in_expr) {
        idStr = in_id;
        expr = in_expr;
    }

    public String print(int indent) {
        // Implement Print
        String printString = "\t".repeat(indent);
        printString += idStr;
        if(expr != null) {
            printString += "[]\n" + expr.print(indent + 1);
        } else {
            printString += "\n";
        }
        return printString;
    }
}
