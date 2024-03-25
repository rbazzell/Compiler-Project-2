package src.parser;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;

import src.scanner.CMinusScanner2;
import src.scanner.DFAException;
import src.scanner.Token;
import src.scanner.Token.TokenType;

import src.parser.Expression.Operator;
import src.parser.Declaration.typeSpecifier;

public class CMinusParser {
    private CMinusScanner2 scanner;
    private Token currToken;

    public CMinusParser(String filename) throws IOException, DFAException {
        scanner = new CMinusScanner2(filename);
    }

    public Program parse() throws IOException, DFAException, ParseException {
        Program head = null;
        currToken = scanner.getNextToken();
        if (currToken.type != TokenType.EOF) {
            head = parseProgram();
        } else {
            throw new ParseException("parse() Expected: something, anything\nReceived: " + currToken.type.name());
        }
        
        if(currToken.type != TokenType.EOF){
            throw new ParseException("parse() did not expect any more tokens\nReceived: " + currToken.type.name());
        }

        return head;
    }

    private Program parseProgram() throws IOException, DFAException, ParseException {
        Program program = null;
        if (currToken.type == TokenType.INT || currToken.type == TokenType.VOID) {
            ArrayList<Declaration> declList = new ArrayList<Declaration>();
            Declaration decl = parseDeclaration();
            declList.add(decl);

            while (currToken.type == TokenType.INT || currToken.type == TokenType.VOID) {
                decl = parseDeclaration();
                declList.add(decl);
            }

            program = new Program(declList);

        } else {
            throw new ParseException("ParseProgram() Expected: INT or VOID\nReceived: " + currToken.type.name());

        }

        return program;
    }

    private Declaration parseDeclaration() throws IOException, DFAException, ParseException {
        Declaration decl = null;
        if (currToken.type == TokenType.VOID) {
            // Take "void"
            typeSpecifier typeSpec = typeSpecifier.VOID;
            currToken = scanner.getNextToken();

            if (currToken.type == TokenType.ID) {
                // Take ID
                String id = (String) currToken.data;
                currToken = scanner.getNextToken();

                decl = parseFunctionDeclarationPrime(typeSpec, id);
            } else {
                throw new ParseException("parseDeclaration() Expected: ID\nReceived: " + currToken.type.name());
            }

        } else if (currToken.type == TokenType.INT) {
            // Take "int"
            typeSpecifier typeSpec = typeSpecifier.INT;
            currToken = scanner.getNextToken();

            if (currToken.type == TokenType.ID) {
                // Take ID
                String id = (String) currToken.data;
                currToken = scanner.getNextToken();
                decl = parseDeclarationPrime(typeSpec, id);
            } else {
                throw new ParseException("parseDeclaration() Expected: ID\nReceived: " + currToken.type.name());
            }

        } else {
            throw new ParseException("parseDeclaration() Expected: INT or VOID\nReceived: " + currToken.type.name());
        }
        return decl;
    }

    private Declaration parseDeclarationPrime(typeSpecifier typeSpec, String id)
            throws IOException, DFAException, ParseException {
        Declaration decl = null;
        if (currToken.type == TokenType.SEMI) {
            // Take ";"
            currToken = scanner.getNextToken();
            decl = new VariableDeclaration(id, 0);
        } else if (currToken.type == TokenType.L_BRACK) {
            // Take "["
            currToken = scanner.getNextToken();
            int num = 0;

            if (currToken.type == TokenType.NUM) {
                // Take NUM
                num = (int) currToken.data;
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseDeclarationPrime() Expected: NUM\nReceived: " + currToken.type.name());
            }

            if (currToken.type == TokenType.R_BRACK) {
                // Take ]
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseDeclarationPrime() Expected: ]\nReceived: " + currToken.type.name());
            }

            if (currToken.type == TokenType.SEMI) {
                // Take ;
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseDeclarationPrime() Expected: ]\nReceived: " + currToken.type.name());
            }

            decl = new VariableDeclaration(id, num);

        } else if (currToken.type == TokenType.L_PAREN) {
            decl = parseFunctionDeclarationPrime(typeSpec, id);

        } else {
            throw new ParseException(
                    "parseDeclarationPrime() Expected: ;, [, or (\nReceived: " + currToken.type.name());
        }
        return decl;
    }

    private Declaration parseFunctionDeclarationPrime(typeSpecifier typeSpec, String id)
            throws IOException, DFAException, ParseException {
        Declaration funDecl = null;
        if (currToken.type == TokenType.L_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();
            ArrayList<Param> paramList = parseParamList();

            if (currToken.type == TokenType.R_PAREN) {
                // Take )
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException(
                        "parseFunctionDeclarationPrime() Expected: )\nReceived: " + currToken.type.name());
            }

            CompoundStatement compoundStatement = parseCompoundStatement();

            funDecl = new FunctionDeclaration(typeSpec, id, paramList, compoundStatement);
        } else {
            throw new ParseException("parseFunctionDeclarationPrime() Expected: (\nReceived: " + currToken.type.name());
        }

        return funDecl;
    }

    private ArrayList<Param> parseParamList() throws IOException, DFAException, ParseException {
        ArrayList<Param> paramList = new ArrayList<Param>();
        Param param = null;
        if (currToken.type == TokenType.INT) {
            param = parseParam();
            paramList.add(param);
            while (currToken.type == TokenType.COMMA) {
                // take ","
                currToken = scanner.getNextToken();
                param = parseParam();
                paramList.add(param);
            }
        } else if (currToken.type == TokenType.VOID) {
            // take "void"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseParamList() Expected either a list of int params or \"void\"");
        }
        return paramList;
    }

    private Param parseParam() throws IOException, DFAException, ParseException {
        Param param = null;
        boolean array = false;
        String id;
        if (currToken.type == TokenType.INT) {
            // Take "int"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseParam() Expected: INT\nReceived: " + currToken.type.name());
        }

        if (currToken.type == TokenType.ID) {
            // Take ID
            id = (String) currToken.data;
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseParam() Expected: ID\nReceived: " + currToken.type.name());
        }

        if (currToken.type == TokenType.L_BRACK) {
            // Take [
            currToken = scanner.getNextToken();
            array = true;

            if (currToken.type == TokenType.R_BRACK) {
                // Take ]
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseParam() Expected: ]\nReceived: " + currToken.type.name());
            }
        }

        param = new Param(id, array);
        return param;
    }

    private CompoundStatement parseCompoundStatement() throws IOException, DFAException, ParseException {
        if (currToken.type == TokenType.L_CURLY) {
            // Take "{"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseCompoundStatement() Expected: {\nRecieved: " + currToken.type.name());
        }
        ArrayList<VariableDeclaration> localDeclarations = parseLocalDeclarations();
        ArrayList<Statement> statementList = parseStatementList();
        if(currToken.type == TokenType.R_CURLY) {
            // Take "}"
            currToken = scanner.getNextToken(); 
        } else {
            throw new ParseException("parseCompoundStatement() Expected: }\nReceived: " + currToken.type.name());
        }
        CompoundStatement compoundStatement = new CompoundStatement(localDeclarations, statementList);
        return compoundStatement;
    }

    private ArrayList<VariableDeclaration> parseLocalDeclarations() throws IOException, DFAException, ParseException {
        ArrayList<VariableDeclaration> localDecls = new ArrayList<VariableDeclaration>();
        String id = null;
        int num = 0;
        VariableDeclaration localDeclaration = null;
        while (currToken.type == TokenType.INT) {

            // Take INT
            currToken = scanner.getNextToken();
            if (currToken.type == TokenType.ID) {
                // Take ID
                id = (String) currToken.data;
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseLocalDeclarations() Expected: ID\nReceived: " + currToken.type.name());
            }

            if (currToken.type == TokenType.L_BRACK) {
                // Take [
                currToken = scanner.getNextToken();

                if (currToken.type == TokenType.NUM) {
                    // Take NUM
                    num = (int) currToken.data;
                    currToken = scanner.getNextToken();
                } else {
                    throw new ParseException(
                            "parseLocalDeclarations() Expected: NUM\nReceived: " + currToken.type.name());
                }

                if (currToken.type == TokenType.R_BRACK) {
                    // Take ]
                    currToken = scanner.getNextToken();
                } else {
                    throw new ParseException(
                            "parseLocalDeclarations() Expected: ]\nReceived: " + currToken.type.name());
                }
            }
            if (currToken.type == TokenType.SEMI) {
                // Take ;
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseLocalDeclarations() Expected: ;\nReceived: " + currToken.type.name());
            }
            localDeclaration = new VariableDeclaration(id, num);

            localDecls.add(localDeclaration);
        }
        return localDecls;
    }

    private ArrayList<Statement> parseStatementList() throws IOException, DFAException, ParseException {
        ArrayList<Statement> statementList = new ArrayList<Statement>();
        while (currToken.type == TokenType.ID || currToken.type == TokenType.NUM || currToken.type == TokenType.L_PAREN
                || currToken.type == TokenType.L_CURLY || currToken.type == TokenType.IF
                || currToken.type == TokenType.WHILE
                || currToken.type == TokenType.RETURN) {
            statementList.add(parseStatement());
            // currToken = scanner.getNextToken(); DONT THINK WE NEED THIS HERE
        }
        return statementList;
    }

    private Statement parseStatement() throws IOException, DFAException, ParseException {
        Statement lhs = null;
        switch (currToken.type) {
            case ID:
            case NUM:
            case L_PAREN:
                lhs = parseExpressionStatement();
                break;
            case L_CURLY:
                lhs = parseCompoundStatement();
                break;
            case IF:
                lhs = parseSelectionStatement();
                break;
            case WHILE:
                lhs = parseIterationStatement();
                break;
            case RETURN:
                lhs = parseReturnStatement();
                break;
            default:
                throw new ParseException("parseStatement() Expected: ID, NUM, (, {, IF, WHILE, or RETURN\nRecieved: " + currToken.type.name());
        }
        return lhs;
    }

    private ExpressionStatement parseExpressionStatement() throws IOException, DFAException, ParseException {
        // no need to check what currToken.type is b/c we already did that to get sent
        // here
        Expression expression = parseExpression();

        if (currToken.type == TokenType.SEMI) {
            // Take ";"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseExpressionStatement() Expected: ;\nRecieved: " + currToken.type.name());
        }

        return new ExpressionStatement(expression);
    }

    private SelectionStatement parseSelectionStatement() throws IOException, DFAException, ParseException {
        if(currToken.type == TokenType.IF){
            // Take "IF"
            currToken = scanner.getNextToken();
        }
        else{
            throw new ParseException("parseSelectionStatement() Expected: IF\nRecieved: " + currToken.type.name());
        }
        Expression ifExpression = null;
        Statement ifStatement = null, elseStatement = null;

        if (currToken.type == TokenType.L_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseSelectionStatement() Expected: (\nRecieved: " + currToken.type.name());
        }

        ifExpression = parseExpression();

        if (currToken.type == TokenType.R_PAREN) {
            // Take ")"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseSelectionStatement() Expected: )\nRecieved: " + currToken.type.name());
        }

        ifStatement = parseStatement();

        if (currToken.type == TokenType.ELSE) {
            // Take "ELSE"
            currToken = scanner.getNextToken();
            elseStatement = parseStatement();
        }

        return new SelectionStatement(ifExpression, ifStatement, elseStatement);

    }

    private IterationStatement parseIterationStatement() throws IOException, DFAException, ParseException {
        if(currToken.type == TokenType.WHILE){
            // Take "WHILE"
            currToken = scanner.getNextToken();
        }
        else{
            throw new ParseException("parseIterationStatement() Expected: WHILE\nRecieved: " + currToken);
        }
        Expression whileExpression = null;
        Statement whileStatement = null;

        if (currToken.type == TokenType.L_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseIterationStatement() Expected: (\nRecieved: " + currToken.type.name());
        }

        whileExpression = parseExpression();

        if (currToken.type == TokenType.R_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseIterationStatement() Expected: )\nRecieved: " + currToken.type.name());
        }

        whileStatement = parseStatement();

        return new IterationStatement(whileExpression, whileStatement);
    }

    private ReturnStatement parseReturnStatement() throws IOException, DFAException, ParseException {
        Expression returnExpression = null;
        if (currToken.type == TokenType.RETURN) {
            // Take "RETURN"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseReturnStatement() Expected: return\nRecieved: " + currToken.type.name());
        }
       
        if (currToken.type == TokenType.ID || currToken.type == TokenType.NUM || currToken.type == TokenType.L_PAREN) {
            returnExpression = parseExpression();
        }

        if (currToken.type == TokenType.SEMI) {
            // Take ";"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("parseReturnStatement() Expected: ;\nRecieved: " + currToken.type.name());
        }

        return new ReturnStatement(returnExpression);

    }

    private Expression parseExpression() throws IOException, DFAException, ParseException {
        Expression expression = null;
        if (currToken.type == TokenType.ID) {
            // Take "ID"
            String id = (String) currToken.data;
            currToken = scanner.getNextToken();
            expression = parseExpressionPrime(id);

        } else if (currToken.type == TokenType.NUM) {
            // Take "NUM"
            NUMExpression num = new NUMExpression((int) currToken.data);
            currToken = scanner.getNextToken();
            expression = parseSimpleExpressionPrime(num);

        } else if (currToken.type == TokenType.L_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();
            Expression inExpression = parseExpression();
            if (currToken.type == TokenType.R_PAREN) {
                // Take ")"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseExpression() Expected: )\nRecieved: " + currToken.type.name());
            }

            expression = parseSimpleExpressionPrime(inExpression);

        } else {
            throw new ParseException(
                    "parseExpression() Expected: ID, NUM, L_PAREN\nRecieved: " + currToken.type.name());
        }
        return expression;
    }

    private Expression parseExpressionPrime(String id) throws IOException, DFAException, ParseException {
        Expression expression = null;
        if (currToken.type == TokenType.ASSIGN) {
            // Take "="
            currToken = scanner.getNextToken();
            IDExpression idExpression = new IDExpression(id, null);
            Expression rhs = parseExpression();
            expression = new AssignExpression(idExpression, rhs);

        } else if (currToken.type == TokenType.L_BRACK) {
            // Take "["
            currToken = scanner.getNextToken();
            Expression inExpression = parseExpression();

            if (currToken.type == TokenType.R_BRACK) {
                // Take "]"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseExpressionPrime() Expected: ]\nRecieved: " + currToken.type.name());
            }

            IDExpression idExpression = new IDExpression(id, inExpression);
            expression = parseExpressionDoublePrime(idExpression);

        } else if (currToken.type == TokenType.L_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();

            ArrayList<Expression> args = parseArgs();

            if (currToken.type == TokenType.R_PAREN) {
                // Take ")"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseExpressionPrime() Expected: )\nRecieved: " + currToken.type.name());
            }

            CallExpression callExpression = new CallExpression(id, args);
            expression = parseSimpleExpressionPrime(callExpression);

        } else {
            IDExpression idExpression = new IDExpression(id, null);
            expression = parseSimpleExpressionPrime(idExpression);
        }

        return expression;
    }

    private Expression parseExpressionDoublePrime(IDExpression idExpression)
            throws IOException, DFAException, ParseException {
        Expression expression = null;
        if (currToken.type == TokenType.ASSIGN) {
            // Take "="
            currToken = scanner.getNextToken();
            Expression rhs = parseExpression();
            expression = new AssignExpression(idExpression, rhs);
        } else {
            expression = parseSimpleExpressionPrime(idExpression);
        }
        return expression;
    }

    private Expression parseSimpleExpressionPrime(Expression lhs) throws IOException, DFAException, ParseException {
        Expression expression = parseAdditiveExpression(lhs);

        if (currToken.type == TokenType.LTE || currToken.type == TokenType.LT || currToken.type == TokenType.GT
                || currToken.type == TokenType.GTE
                || currToken.type == TokenType.EQ || currToken.type == TokenType.NEQ) {
            // Take relop
            Operator relop = parseRelop();
            currToken = scanner.getNextToken();
            Expression rhs = parseAdditiveExpression(null);

            expression = new BinaryExpression(expression, rhs, relop);
        }

        return expression;

    }

    private Expression parseAdditiveExpression(Expression lhs) throws IOException, DFAException, ParseException {
        lhs = parseTerm(lhs); // if not null, treated as AdditiveExpressionPrime()
        while (currToken.type == TokenType.PLUS || currToken.type == TokenType.MINUS) {
            // Take addop
            Operator addop = parseAddop();
            currToken = scanner.getNextToken();
            Expression rhs = parseTerm(null);

            lhs = new BinaryExpression(lhs, rhs, addop);
        }

        return lhs;
    }

    private Expression parseTerm(Expression lhs) throws IOException, DFAException, ParseException {
        if (lhs == null) { // parse Term
            lhs = parseFactor();
        }
        // now just do parse Term', but since we made sure we have the lhs, works for
        // Term and Term'
        while (currToken.type == TokenType.MULT || currToken.type == TokenType.DIV) {
            // Take mulop
            Operator mulop = parseMulop();
            currToken = scanner.getNextToken();
            Expression rhs = parseFactor();

            lhs = new BinaryExpression(lhs, rhs, mulop);
        }

        return lhs;
    }

    private Operator parseRelop() throws IOException, DFAException, ParseException {
        Operator retOp = null;
        // if (currToken.type == TokenType.LTE || currToken.type == TokenType.LT ||
        // currToken.type == TokenType.GT || currToken.type == TokenType.GTE
        // || currToken.type == TokenType.EQ || currToken.type == TokenType.NEQ) {
        switch (currToken.type) {
            case LTE:
                retOp = Operator.LTE;
                break;
            case LT:
                retOp = Operator.LT;
                break;
            case GT:
                retOp = Operator.GT;
                break;
            case GTE:
                retOp = Operator.GTE;
                break;
            case EQ:
                retOp = Operator.EQ;
                break;
            case NEQ:
                retOp = Operator.NEQ;
                break;
            default:
                throw new ParseException("parseRelop() Error: Recieved a non-Relop token");
        }
        return retOp;
    }

    private Operator parseAddop() throws IOException, DFAException, ParseException {
        Operator addOp = null;
        switch (currToken.type) {
            case PLUS:
                addOp = Operator.PLUS;
                break;
            case MINUS:
                addOp = Operator.MINUS;
                break;
            default:
                throw new ParseException("parseAddop() Error: Recieved a non-Addop token");
        }
        return addOp;
    }

    private Operator parseMulop() throws IOException, DFAException, ParseException {
        Operator mulOp = null;
        switch (currToken.type) {
            case DIV:
                mulOp = Operator.DIV;
                break;
            case MULT:
                mulOp = Operator.MULT;
                break;
            default:
                throw new ParseException("parseMulop() Error: Recieved a non-Mulop token");
        }
        return mulOp;
    }

    private Expression parseFactor() throws IOException, DFAException, ParseException {
        // return parseExpression(), parseVarcall(), or NUMExpression
        // ^^^^^^
        // Parens are not needed in AST b/c they are just used to override precedence
        // so just return parseExpression()
        Expression expression = null;

        if (currToken.type == TokenType.L_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();
            expression = parseExpression();

        } else if (currToken.type == TokenType.ID) {
            // Take ID
            String id = (String) currToken.data;
            currToken = scanner.getNextToken();
            expression = parseVarcall(id);

        } else if (currToken.type == TokenType.NUM) {
            // Take NUM
            int num = (int) currToken.data;
            currToken = scanner.getNextToken();
            expression = new NUMExpression(num);

        } else {
            throw new ParseException("parseFactor() Expected: (, ID, or NUM\nRecieved: " + currToken.type.name());
        }

        return expression;
    }

    private Expression parseVarcall(String id) throws IOException, DFAException, ParseException {
        Expression expression = null;
        // return IDExpression or CallExpression
        if (currToken.type == TokenType.L_BRACK) {
            // Take "["
            currToken = scanner.getNextToken();
            Expression inExpression = parseExpression();

            if (currToken.type == TokenType.R_BRACK) {
                // Take "]"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseVarcall() Expected: ]\nRecieved: " + currToken.type.name());
            }

            expression = new IDExpression(id, inExpression);

        } else if (currToken.type == TokenType.L_PAREN) {
            // Take "("
            currToken = scanner.getNextToken();
            ArrayList<Expression> args = parseArgs();

            if (currToken.type == TokenType.R_PAREN) {
                // Take ")"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("parseVarcall() Expected: )\nRecieved: " + currToken.type.name());
            }
            expression = new CallExpression(id, args);

        } else {
            // Return ID Expression
            expression = new IDExpression(id, null);
        }
        return expression;
    }

    private ArrayList<Expression> parseArgs() throws IOException, DFAException, ParseException {
        ArrayList<Expression> args = new ArrayList<Expression>();

        if (currToken.type == TokenType.ID || currToken.type == TokenType.NUM || currToken.type == TokenType.L_PAREN) {
            Expression expression = parseExpression();
            args.add(expression);

            while (currToken.type == TokenType.COMMA) {
                // Take ","
                currToken = scanner.getNextToken();
                expression = parseExpression();
                args.add(expression);
            }
        }

        return args;
    }

    public static void printTree(Program head) {
        printTree(head, false, "");
    }

    public static void printTree(Program head, String filename) {
        printTree(head, true, filename);
    }

    private static void printTree(Program head, boolean toFile, String filename) {
        String output = head.print(0);
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
        String filename_prefix = "code/parse3";
        CMinusParser parser = new CMinusParser(filename_prefix + ".cm");
        Program head = parser.parse();
        printTree(head, filename_prefix + ".ast");
    }
}