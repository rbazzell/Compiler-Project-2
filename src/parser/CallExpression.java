package src.parser;
import java.util.ArrayList;

public class CallExpression extends Expression {
    
    public IDExpression callID = null;
    public ArrayList<Expression> args = null;

    public CallExpression(IDExpression in_ID, ArrayList<Expression> in_args){
        args = in_args;
    }

    public String printNode(int indent) {
        //Implement Print
        return null;
    }
}
