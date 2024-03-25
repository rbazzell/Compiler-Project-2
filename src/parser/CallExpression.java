package src.parser;

import java.util.ArrayList;

public class CallExpression extends Expression {

    public String callID = null;
    public ArrayList<Expression> args = null;

    public CallExpression(String in_ID, ArrayList<Expression> in_args) {
        callID = in_ID;
        args = in_args;
    }

    public String print(int indent) {
        String printStr = "\t".repeat(indent);
        printStr += callID + "()\n";
        for (Expression arg : args) {
            printStr += arg.print(indent + 1);
        }
        return printStr;
    }
}
