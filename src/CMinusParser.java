package src;

import scanner.CMinusScanner2;
import scanner.DFAException;
import scanner.Token;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;


public class CMinusParser {
    private CMinusScanner2 scanner;
    private Token currToken;
    
    public CMinusParser (String filename) throws IOException, DFAException {
        scanner = new CMinusScanner2(filename);
    }

    public Program parse() {
        Program head = null;
        if (scanner.viewNextToken().type != Token.TokenType.EOF) {
            head = parseProgram();

        }
        return head;
        //TODO: throw error if left over tokens after parseProgram()?
    }

    private Program parseProgram() throws ParseException, IOException, DFAException {
        currToken = scanner.getNextToken();
        if((currToken.type == Token.TokenType.INT) || (currToken.type == Token.TokenType.VOID)){
            ArrayList<Declaration> declList = new ArrayList<Declaration>();
            Declaration firstDecl = parseDeclaration();
            declList.add(firstDecl);
            while((currToken.type == Token.TokenType.INT) || (currToken.type == Token.TokenType.VOID)){
                Declaration decl = parseDeclaration();
                declList.add(decl);
            }
            return new Program(declList);
        }else{
            throw new ParseException("ERROR: ParseProgram expects \"int\" or \"void\"");
        }
    }

    private Declaration parseDeclaration() throws ParseException, IOException, DFAException{
        if(currToken.type == Token.TokenType.VOID){
            currToken = scanner.getNextToken();
            if(currToken.type == Token.TokenType.ID){
                currToken = scanner.getNextToken();
            }
        }
        return null;
    }

    private Declaration parseDeclarationPrime() {
        return null;
    }

    private FunctionDeclaration parseFunctionDeclarationPrime(Token typeSpec, Token id) {
        return null;
    }

    private ArrayList<Param> parseParamList() {
        return null;
    }

    private Param parseParam() {
        return null;
    }

    private CompoundStatement parseCompoundStatement() {
        return null;
    }

    private ArrayList<VariableDeclaration> parseLocalDeclarations() {
        return null;
    }

    private ArrayList<Statement> parseStatementList() {
        return null;
    }

    private Statement parseStatement() {
        return null;
    }

    private ExpressionStatement parseExpressionStatement() {
        return null;
    }

    private SelectionStatement parseSelectionStatement() {
        return null;
    }

    private IterationStatement parseIterationStatement() {
        return null;
    }

    private ReturnStatement parseReturnStatement() {
        return null;
    }

    private Expression parseExpression() {
        return null;
    }

    private Expression parseExpressionPrime(Token id) {
        return null;
    }

    private Expression parseExpressionDoublePrime(Expression lhs) {
        return null;
    }

    private BinaryExpression parseSimpleExpressionPrime(Expression lhs) {        
        return null;
    }

    private BinaryExpression parseAdditiveExpression(Expression lhs) {
        if (lhs == null) { //parse AdditiveExpression
            lhs = parseFactor();
        }
        return null;
    }

    private BinaryExpression parseTerm(Expression lhs) {
        if (lhs == null) { //parse Term
            lhs = parseFactor();
        }
        return null;
        //now just do parse Term', but since we made sure we have the lhs, works for Term and Term'
    }

    private Token parseRelop() {
        return null;
    }

    private Token parseAddop() {
        return null;
    }

    private Token parseMulop() {
        return null;
    }

    private Expression parseFactor() {
        //return parseExpression(), parseVarcall(), or NUMExpression
        //       ^^^^^^
        //       Parens are not needed in AST b/c they are just used to override precedence
        //       so just return parseExpression()
        return null;
    }

    private Expression parseVarcall(Token id) {
        //return IDExpression or CallExpression
        return null;
    }

    private ArrayList<Expression> parseArgs() {
        return null;
    }






    public void printTree(Program head) {
        printTree(head, false, "");
    }

    public void printTree(Program head, String filename) {
        printTree(head, true, filename);
    }

    private void printTree(Program head, boolean toFile, String filename) {
        String output = head.printNode(0);
        if (toFile) {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(filename));
                out.write(output);
                out.close();
            } catch (IOException e) {
                System.err.println("COULD NOT OUTPUT TO FILE");
                e.printStackTrace();
            }
        }
        System.out.println(output);
    }




    public static void main(String[] args) throws Exception {
        CMinusParser parser = new CMinusParser("code/gcd.cm");
        Program head = parser.parse();
    }
}
