package src.parser;

import java.util.ArrayList;

public class Program {

    public ArrayList<Declaration> decls;

    public Program(ArrayList<Declaration> in_decls) {
        decls = in_decls;
    }

    public String print(int indent) {
        String printStr = "\t".repeat(indent) + "PROGRAM\n";
        for (Declaration decl : decls) {
            printStr += decl.print(indent + 1);
        }
        return printStr;
    }
}
