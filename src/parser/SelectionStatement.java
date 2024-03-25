package src.parser;

public class SelectionStatement extends Statement {

    public Expression expr;
    public Statement stmt;
    public Statement elseStmt = null;

    public SelectionStatement(Expression in_expr, Statement in_stmt, Statement in_elseStmt) {
        expr = in_expr;
        stmt = in_stmt;
        elseStmt = in_elseStmt;
    }

    public String print(int indent) {
        String printStr = "\t".repeat(indent) + "if\n";
        printStr += expr.print(indent + 1);
        printStr += stmt.print(indent + 1);
        if (elseStmt != null) {
            printStr += "\t".repeat(indent) + "else\n";
            printStr += elseStmt.print(indent + 1);
        }
        return printStr;
    }
}
