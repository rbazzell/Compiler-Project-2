package src;

import scanner.CMinusScanner2;
import scanner.DFAException;
import scanner.Token;
import scanner.Token.TokenType;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.function.Function;


public class CMinusParser {
    private CMinusScanner2 scanner;
    private Token currToken;
    
    public CMinusParser (String filename) throws IOException, DFAException {
        scanner = new CMinusScanner2(filename);
    }

    public Program parse() throws IOException, DFAException, ParseException {
        Program head = null;
        if (scanner.viewNextToken().type != Token.TokenType.EOF) {
            head = parseProgram();

        }
        return head;
        //TODO: throw error if left over tokens after parseProgram()?
    }

    private Program parseProgram() throws IOException, DFAException, ParseException {
        Program program = null;
        currToken = scanner.getNextToken(); //TODO: is this needed? Doesn't the scanner start on the first token?
        if (currToken.type == TokenType.INT || currToken.type == TokenType.VOID) {
            ArrayList<Declaration> declList = new ArrayList<Declaration>();
            Declaration decl = parseDeclaration();
            declList.add(decl);
            
            while(currToken.type == TokenType.INT || currToken.type == TokenType.VOID) {
                decl = parseDeclaration();
                declList.add(decl);
            }

            program = new Program(declList);

        } else {
            throw new ParseException("Expected: INT or VOID\nReceived: " + currToken.type.name());

        }

        return program;
    }

    private Declaration parseDeclaration() throws IOException, DFAException, ParseException {
        Declaration decl = null;
        if (currToken.type == TokenType.VOID){
            //Take "void"
            //TODO: add typeSpec ENUM
            currToken = scanner.getNextToken();

            if (currToken.type == TokenType.ID) {
                //Take ID
                String id = (String) currToken.data;
                currToken = scanner.getNextToken();

                decl = parseFunctionDeclarationPrime(typeSpec, id);
            } else {
                throw new ParseException("Expected: ID\nReceived: " + currToken.type.name());
            }
            
        } else if (currToken.type == TokenType.INT) {
            //Take "int"

            if (currToken.type == TokenType.ID) {
                //Take ID
                String id = (String) currToken.data;
                currToken = scanner.getNextToken();
                decl = parseDeclarationPrime(typeSpec, id);
            } else {
                throw new ParseException("Expected: ID\nReceived: " + currToken.type.name());
            }

        } else {
            throw new ParseException("Expected: INT or VOID\nReceived: " + currToken.type.name());
        }
        return null;
    }

    private Declaration parseDeclarationPrime(TypeSpec typeSpec, String id) throws IOException, DFAException, ParseException {
        Declaration decl = null;
        if (currToken.type == TokenType.SEMI) {
            //Take ";"
            currToken = scanner.getNextToken();
            IDExpression idExpression = new IDExpression(id, null);
            NUMExpression numExpression = new NUMExpression(0);
            decl = new VariableDeclaration(idExpression, numExpression);
        } else if (currToken.type == TokenType.L_BRACK) {
            //Take "["
            currToken = scanner.getNextToken();
            NUMExpression numExpression = null;

            if (currToken.type == TokenType.NUM) {
                //Take NUM
                numExpression = new NUMExpression((int)currToken.data);
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: NUM\nReceived: " + currToken.type.name());
            }

            if (currToken.type == TokenType.R_BRACK) {
                //Take ]
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: ]\nReceived: " + currToken.type.name());
            }

            if (currToken.type == TokenType.SEMI) {
                //Take ;
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: ]\nReceived: " + currToken.type.name());
            }

            IDExpression idExpression = new IDExpression(id, null);
            decl = new VariableDeclaration(idExpression, numExpression);

        } else if (currToken.type == TokenType.L_PAREN) {
            decl = parseFunctionDeclarationPrime(typeSpec, id);

        } else {
            throw new ParseException("Expected: ;, [, or (\nReceived: " + currToken.type.name());
        }
        return decl;
    }

    private Declaration parseFunctionDeclarationPrime(TypeSpec typeSpec, String id) throws IOException, DFAException, ParseException {
        Declaration funDecl = null;
        if (currToken.type == TokenType.L_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
            ArrayList<Param> paramList = parseParamList();

            if (currToken.type == TokenType.R_PAREN) {
                //Take )
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: )\nReceived: " + currToken.type.name());
            }

            CompoundStatement compoundStatement = parseCompoundStatement();

            //TODO: Change FunctionDeclaration to take a typeSpec
            funDecl = new FunctionDeclaration(typeSpec, paramList, compoundStatement);
        } else {
            throw new ParseException("Expected: (\nReceived: " + currToken.type.name());
        }

        return funDecl;
    }

    private ArrayList<Param> parseParamList() throws IOException, DFAException, ParseException {
        ArrayList<Param> paramList = new ArrayList<Param>();
        Param param = null;
        //TODO: return null or empty paramList on a VOID?
        if (currToken.type != TokenType.VOID) {
            param = parseParam();
            paramList.add(param);
            while (currToken.type == TokenType.COMMA) {
                //take ","
                currToken = scanner.getNextToken();
                param = parseParam();
                paramList.add(param);
            }
        }
        return paramList;
    }

    private Param parseParam() throws IOException, DFAException, ParseException {
        Param param = null;
        boolean array = false;
        String id;
        if (currToken.type == TokenType.INT) {
            //Take "int"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: INT\nReceived: " + currToken.type.name());
        }

        if (currToken.type == TokenType.ID) {
            //Take ID
            id = (String)currToken.data;
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: ID\nReceived: " + currToken.type.name());
        }

        if (currToken.type == TokenType.L_BRACK) {
            //Take [
            currToken = scanner.getNextToken();
            array = true;

            if (currToken.type == TokenType.R_BRACK) {
                //Take ]
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: ]\nReceived: " + currToken.type.name());
            }
        } else {
            throw new ParseException("Expected: [\nReceived: " + currToken.type.name());
        }

        param = new Param(id, array);
        return param;
    }

    private CompoundStatement parseCompoundStatement() throws IOException, DFAException, ParseException {
        //Take "{"
        currToken = scanner.getNextToken();
        ArrayList<VariableDeclaration> localDeclarations = parseLocalDeclarations();
        ArrayList<Statement> statementList = parseStatementList();
        return new CompoundStatement(localDeclarations, statementList);
    }

    private ArrayList<VariableDeclaration> parseLocalDeclarations() throws IOException, DFAException, ParseException {
        ArrayList<VariableDeclaration> localDecls = new ArrayList<VariableDeclaration>();
        String id = null;
        int num = 0;
        VariableDeclaration localDeclaration = null;
        NUMExpression numExpression = null;
        IDExpression idExpression = null;
        while (currToken.type == TokenType.INT) {
            
            //Take INT
            currToken = scanner.getNextToken();
            if (currToken.type == TokenType.ID) {
                //Take ID
                id = (String)currToken.data;
                //TODO: Is this how we do this? or do we pass the NUM as a num expression to the ID?
                idExpression = new IDExpression(id, null);
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: ID\nReceived: " + currToken.type.name());
            }

            if (currToken.type == TokenType.L_BRACK) {
                //Take [
                currToken = scanner.getNextToken();

                if (currToken.type == TokenType.NUM) {
                    //Take NUM
                    num = (int)currToken.data;
                    currToken = scanner.getNextToken();
                } else {
                    throw new ParseException("Expected: NUM\nReceived: " + currToken.type.name());
                }

                if (currToken.type == TokenType.R_BRACK) {
                    //Take ]
                    currToken = scanner.getNextToken();
                } else {
                    throw new ParseException("Expected: ]\nReceived: " + currToken.type.name());
                }

            }
            numExpression = new NUMExpression(num);
            localDeclaration = new VariableDeclaration(idExpression, numExpression);

            localDecls.add(localDeclaration);
        }
        return localDecls;
    }

    private ArrayList<Statement> parseStatementList() throws IOException, DFAException, ParseException {
        ArrayList<Statement> statementList = new ArrayList<Statement>();
        while (currToken.type == TokenType.ID || currToken.type == TokenType.NUM || currToken.type == TokenType.L_PAREN
            || currToken.type == TokenType.L_CURLY || currToken.type == TokenType.IF || currToken.type == TokenType.WHILE 
            || currToken.type == TokenType.RETURN) {
                statementList.add(parseStatement());
                //currToken = scanner.getNextToken(); DONT THINK WE NEED THIS HERE
        }
        return statementList;    
    }

    private Statement parseStatement() throws IOException, DFAException, ParseException {
        Statement lhs;
        if (currToken.type == TokenType.ID || currToken.type == TokenType.NUM || currToken.type == TokenType.L_PAREN) {
            lhs = parseExpressionStatement();
        } else if (currToken.type == TokenType.L_CURLY) {
            lhs = parseCompoundStatement();
        } else if (currToken.type == TokenType.IF) {
            lhs = parseSelectionStatement();
        } else if (currToken.type == TokenType.WHILE) {
            lhs = parseIterationStatement();
        } else if (currToken.type == TokenType.RETURN) {
            lhs = parseReturnStatement();
        } else {
            throw new ParseException("Expected: ID, NUM, (, {, IF, WHILE, or RETURN\nRecieved: " + currToken.type.name());
        }
        return lhs;
    }

    private ExpressionStatement parseExpressionStatement() throws IOException, DFAException, ParseException {
        //no need to check what currToken.type is b/c we already did that to get sent here
        Expression expression = parseExpression();

        if (currToken.type == TokenType.SEMI) {
            //Take ";"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: ;\nRecieved: " + currToken.type.name());
        }

        return new ExpressionStatement(expression);
    }

    private SelectionStatement parseSelectionStatement() throws IOException, DFAException, ParseException {
        //Take "IF"
        currToken = scanner.getNextToken();
        Expression ifExpression = null;
        Statement ifStatement = null, elseStatement = null;

        if (currToken.type == TokenType.L_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: (\nRecieved: " + currToken.type.name());
        }

        ifExpression = parseExpression();

        if (currToken.type == TokenType.R_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: )\nRecieved: " + currToken.type.name());
        }

        ifStatement = parseStatement();

        if (currToken.type == TokenType.ELSE) {
            //Take "ELSE"
            currToken = scanner.getNextToken();
            elseStatement = parseStatement();
        }

        return new SelectionStatement(ifExpression, ifStatement, elseStatement);

    }

    private IterationStatement parseIterationStatement() throws IOException, DFAException, ParseException {
        //Take "WHILE"
        currToken = scanner.getNextToken();
        Expression whileExpression = null;
        Statement whileStatement = null;

        if (currToken.type == TokenType.L_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: (\nRecieved: " + currToken.type.name());
        }

        whileExpression = parseExpression();

        if (currToken.type == TokenType.R_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: )\nRecieved: " + currToken.type.name());
        }

        whileStatement = parseStatement();

        return new IterationStatement(whileExpression, whileStatement);
    }

    private ReturnStatement parseReturnStatement() throws IOException, DFAException, ParseException {
        //Take "RETURN"
        currToken = scanner.getNextToken();
        Expression returnExpression = null;
        
        if (currToken.type == TokenType.ID || currToken.type == TokenType.NUM || currToken.type == TokenType.L_PAREN) {
            returnExpression = parseExpression();
        }

        if (currToken.type == TokenType.SEMI) {
            //Take ";"
            currToken = scanner.getNextToken();
        } else {
            throw new ParseException("Expected: ;\nRecieved: " + currToken.type.name());
        }

        return new ReturnStatement(returnExpression);
        
    }

    private Expression parseExpression() throws IOException, DFAException, ParseException {
        Expression expression = null;
        if (currToken.type == TokenType.ID) {
            //Take "ID"
            String id = (String) currToken.data;
            currToken = scanner.getNextToken();
            expression = parseExpressionPrime(id);

        } else if (currToken.type == TokenType.NUM) {
            //Take "NUM"
            NUMExpression num = new NUMExpression((int)currToken.data);
            currToken = scanner.getNextToken();
            expression = parseSimpleExpressionPrime(num);

        } else if (currToken.type == TokenType.L_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
            Expression inExpression = parseExpression();
            if (currToken.type == TokenType.R_PAREN) {
                //Take ")"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: )\nRecieved: " + currToken.type.name());
            }

            expression = parseSimpleExpressionPrime(inExpression);

        } else {
            throw new ParseException("Expected: ID, NUM, L_PAREN\nRecieved: " + currToken.type.name());
        }
        return expression;
    }

    private Expression parseExpressionPrime(String id) throws IOException, DFAException, ParseException {
        Expression expression = null;
        if(currToken.type == TokenType.ASSIGN) {
            //Take "="
            currToken = scanner.getNextToken();
            IDExpression idExpression = new IDExpression(id, null);
            Expression rhs = parseExpression();
            expression = new AssignExpression(idExpression, rhs);

        } else if (currToken.type == TokenType.L_BRACK) {
            //Take "["
            currToken = scanner.getNextToken();
            Expression inExpression = parseExpression();

            if (currToken.type == TokenType.R_BRACK) {
                //Take "]"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: ]\nRecieved: " + currToken.type.name());
            }

            IDExpression idExpression = new IDExpression(id, inExpression);
            expression = parseExpressionDoublePrime(idExpression);

        } else if (currToken.type == TokenType.L_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();

            ArrayList<Expression> args = parseArgs();

            if (currToken.type == TokenType.R_PAREN) {
                //Take "("
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: )\nRecieved: " + currToken.type.name());
            }

            IDExpression idExpression = new IDExpression(id, null);
            CallExpression callExpression = new CallExpression(idExpression, args);
            expression = parseSimpleExpressionPrime(callExpression);

        } else {
            IDExpression idExpression = new IDExpression(id, null);
            expression = parseSimpleExpressionPrime(idExpression);
        }

        return expression;
    }

    private Expression parseExpressionDoublePrime(IDExpression idExpression) throws IOException, DFAException, ParseException {
        Expression expression = null;
        if (currToken.type == TokenType.ASSIGN) {
            //Take "="
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
        
        if (currToken.type == TokenType.LTE || currToken.type == TokenType.LT || currToken.type == TokenType.GT || currToken.type == TokenType.GTE 
            || currToken.type == TokenType.EQ || currToken.type == TokenType.NEQ) {
                //Take relop
                //TODO: CHANGE TO SPECIFIC RELOP ENUM TYPE
                Token relop = parseRelop();
                currToken = scanner.getNextToken();
                Expression rhs = parseAdditiveExpression(null);

                //TODO: PART OF THAT ENUM FIX                     VVVVVV
                expression = new BinaryExpression(expression, rhs, 'r');
        }

        return expression;

    }

    private Expression parseAdditiveExpression(Expression lhs) throws IOException, DFAException, ParseException {
        lhs = parseTerm(lhs); //if null, gets passed and treated as AdditiveExpressionPrime
        while (currToken.type == TokenType.PLUS || currToken.type == TokenType.MINUS) {
            //Take addop
            //TODO: CHANGE TO SPECIFIC ADDOP ENUM TYPE
            Token addop = parseAddop();
            currToken = scanner.getNextToken();
            Expression rhs = parseTerm(null);

            //TODO: PART OF THAT ENUM FIX       VVVVVV
            lhs = new BinaryExpression(lhs, rhs, 'a');
        }

        return lhs;
    }

    private Expression parseTerm(Expression lhs) throws IOException, DFAException, ParseException {
        if (lhs == null) { //parse Term
            lhs = parseFactor();
        }
        //now just do parse Term', but since we made sure we have the lhs, works for Term and Term'
        while (currToken.type == TokenType.MULT || currToken.type == TokenType.DIV) {
            //Take mulop
            //TODO: CHANGE TO SPECIFIC MULOP ENUM TYPE
            Token mulop = parseMulop();
            currToken = scanner.getNextToken();
            Expression rhs = parseFactor();

            //TODO: PART OF THAT ENUM FIX      VVVVVV
            lhs = new BinaryExpression(lhs, rhs, 'm');
        }

        return lhs;
    }

    private Token parseRelop() throws IOException, DFAException, ParseException {
        return null;
    }

    private Token parseAddop() throws IOException, DFAException, ParseException {
        return null;
    }

    private Token parseMulop() throws IOException, DFAException, ParseException {
        return null;
    }

    private Expression parseFactor() throws IOException, DFAException, ParseException {
        //return parseExpression(), parseVarcall(), or NUMExpression
        //       ^^^^^^
        //       Parens are not needed in AST b/c they are just used to override precedence
        //       so just return parseExpression()
        Expression expression = null;

        if (currToken.type == TokenType.L_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
            expression = parseExpression();

        } else if (currToken.type == TokenType.ID) {
            //Take ID
            String id = (String)currToken.data;
            expression = parseVarcall(id);

        } else if (currToken.type == TokenType.NUM) {
            //Take NUM
            int num = (int)currToken.data;
            expression = new NUMExpression(num);

        } else {
            throw new ParseException("Expected: (, ID, or NUM\nRecieved: " + currToken.type.name());
        }

        return expression;
    }

    private Expression parseVarcall(String id) throws IOException, DFAException, ParseException {
        Expression expression = null;
        //return IDExpression or CallExpression
        if (currToken.type == TokenType.L_BRACK) {
            //Take "["
            currToken = scanner.getNextToken();
            Expression inExpression = parseExpression();

            if (currToken.type == TokenType.R_BRACK) {
                //Take "]"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: ]\nRecieved: " + currToken.type.name());
            }

            expression = new IDExpression(id, inExpression);
            
        } else if (currToken.type == TokenType.L_PAREN) {
            //Take "("
            currToken = scanner.getNextToken();
            ArrayList<Expression> args = parseArgs();
            
            if (currToken.type == TokenType.R_PAREN) {
                //Take ")"
                currToken = scanner.getNextToken();
            } else {
                throw new ParseException("Expected: )\nRecieved: " + currToken.type.name());
            }

            IDExpression idExpression = new IDExpression(id, null);
            expression = new CallExpression(idExpression, args);

        } else {
            //Return ID Expression
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
                //Take ","
                currToken = scanner.getNextToken();
                expression = parseExpression();
                args.add(expression);
            }
        }

        return args;
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
