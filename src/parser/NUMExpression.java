package src.parser;

public class NUMExpression extends Expression {

    public int num;

    public NUMExpression(int in_num) {
        num = in_num;
    }

    public String print(int indent) {
        String printString = "\t".repeat(indent);
        printString += num + "\n";
        return printString;
    }
}
