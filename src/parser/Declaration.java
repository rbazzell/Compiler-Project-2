package src.parser;

public abstract class Declaration {

     public static enum typeSpecifier {
          VOID,
          INT
     }

     public abstract String print(int indent);
}
