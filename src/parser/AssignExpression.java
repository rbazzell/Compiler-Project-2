package src.parser;

public class AssignExpression extends Expression {

    public IDExpression lhs = null;
    public Expression rhs = null;

    public AssignExpression(IDExpression in_lhs, Expression in_rhs) {
        lhs = in_lhs;
        rhs = in_rhs;
    }

    public String print(int indent) {
        String printStr = "\t".repeat(indent) + "=\n";
        printStr += lhs.print(indent + 1);
        printStr += rhs.print(indent + 1);
        return printStr;
    }
}
