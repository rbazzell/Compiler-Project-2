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

    public String printNode(int indent) {
        String printStr = "\t".repeat(indent) + "if\n";
        printStr += expr.printNode(indent + 1);
        printStr += stmt.printNode(indent + 1);
        if (elseStmt != null) {
            printStr += "\t".repeat(indent) + "else\n";
            printStr += elseStmt.printNode(indent + 1);
        }
        return printStr;
    }
}
