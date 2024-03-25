package src.parser;

public class IterationStatement extends Statement {

    public Expression expr;
    public Statement stmt;

    public IterationStatement(Expression in_expr, Statement in_stmt) {
        expr = in_expr;
        stmt = in_stmt;
    }

    public String printNode(int indent) {
        String printStr = "\t".repeat(indent) + "while\n";
        printStr += expr.printNode(indent + 1);
        printStr += stmt.printNode(indent + 1);
        return printStr;
    }
}
