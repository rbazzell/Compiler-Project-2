package src.parser;

public abstract class Declaration implements Node {
     
     public static enum typeSpecifier{
          VOID,
          INT
     }

     public abstract String printNode(int indent);
}
