package src.parser;

public class Param implements Node {

    public String idStr;
    public boolean hasBrackets;

    public Param(String in_idStr, boolean in_hasBrackets) {
        idStr = in_idStr;
        hasBrackets = in_hasBrackets;
    }

    public String printNode(int indent) {
        String printString = "\t".repeat(indent) + "INT ";
        printString += idStr;
        if(hasBrackets){
            printString += "[]";
        }
        printString += "\n";
        return printString;
    }
}
