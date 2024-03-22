package src.parser;

public abstract class Expression implements Node{

    public static enum Operator { LTE, LT, GT, GTE, EQ, NEQ, PLUS, ADD, SUB, MUL, DIV };

    public abstract String printNode(int indent);
}
