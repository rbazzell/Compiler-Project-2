package src.parser;

public class ReturnStatement extends Statement {

    public Expression expr = null;

    public ReturnStatement(Expression in_expr) {
        expr = in_expr;
    }

    public String printNode(int indent) {
        String printString = "\t".repeat(indent);
        printString += "return\n";
        if(expr != null){
            printString += expr.printNode(indent + 1);
        }
        return printString;
    }
}
