package src.parser;

public abstract class Expression implements Node{

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

    public abstract String printNode(int indent);
}
