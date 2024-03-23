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
        // Implement Print
        return null;
    }
}
