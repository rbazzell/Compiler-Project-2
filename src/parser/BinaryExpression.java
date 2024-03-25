package src.parser;

public class BinaryExpression extends Expression {

    public Expression lhs = null;
    public Expression rhs = null;
    public Operator type;

    public BinaryExpression(Expression in_lhs, Expression in_rhs, Operator in_type) {
        lhs = in_lhs;
        rhs = in_rhs;
        type = in_type;
    }

    public String print(int indent) {
        String printString = "\t".repeat(indent);
        switch(type){
            case DIV:
                printString += "/\n";
                break;
            case EQ:
                printString += "==\n";
                break;
            case GT:
                printString += ">\n";
                break;
            case GTE:
                printString += ">=\n";
                break;
            case LT:
                printString += "<\n";
                break;
            case LTE:
                printString += "<=\n";
                break;
            case MINUS:
                printString += "-\n";
                break;
            case MULT:
                printString += "*\n";
                break;
            case NEQ:
                printString += "!=\n";
                break;
            case PLUS:
                printString += "+\n";
                break;
        }
        printString += lhs.print(indent + 1);
        printString += rhs.print(indent + 1);
        
        return printString;
    }
}
