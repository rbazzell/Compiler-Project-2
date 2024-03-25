package src.parser;

public abstract class Expression {

    public static enum Operator {
        LTE,
        LT,
        GT,
        GTE,
        EQ,
        NEQ,
        PLUS,
        MINUS,
        MULT,
        DIV
    };

    public abstract String print(int indent);
}
