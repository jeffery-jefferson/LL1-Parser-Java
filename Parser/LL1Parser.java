package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import Exceptions.ExpressionException;
import Exceptions.InvalidNodeException;
import Models.ParseTree;
import Models.ProductionRule;
import Models.Token;
import Models.Token.TokenType;
import Models.TreeNode;

public class LL1Parser {
    /*
    PARSING TABLE Structure
        - ROWS are NON-TERMINALS represented by Character
        - COLUMNS are TERMINALS represented by Character
        - CELLS are Production Rules which dictate how to pop and push to and from the stack
    */
    private Map<Character, Token> nonTerminals;
    private List<ProductionRule> rules;

    private Table<String, TokenType, ProductionRule> parsingTable;

    public LL1Parser(String inputMessage) {
        this.parsingTable = new Table<String, TokenType, ProductionRule>();
        SetupNonTerminalTokens();
        SetupRules();
        SetupTable();
    }

    private void SetupTable() {
        this.parsingTable.Add("<program>", TokenType.NUMBER, rules.get(0));
        this.parsingTable.Add("<program>", TokenType.IDENTIFIER, rules.get(0));
        this.parsingTable.Add("<program>", TokenType.OPEN_PARENTHESES, rules.get(0));
        this.parsingTable.Add("<expr>", TokenType.NUMBER, rules.get(1));
        this.parsingTable.Add("<expr>", TokenType.IDENTIFIER, rules.get(2));
        this.parsingTable.Add("<expr>", TokenType.OPEN_PARENTHESES, rules.get(3));
        this.parsingTable.Add("<expr>*", TokenType.NUMBER, rules.get(12));
        this.parsingTable.Add("<expr>*", TokenType.IDENTIFIER, rules.get(12));
        this.parsingTable.Add("<expr>*", TokenType.OPEN_PARENTHESES, rules.get(12));
        this.parsingTable.Add("<expr>*", TokenType.CLOSE_PARENTHESES, rules.get(13));
        this.parsingTable.Add("<paren-expr>", TokenType.NUMBER, rules.get(11));
        this.parsingTable.Add("<paren-expr>", TokenType.IDENTIFIER, rules.get(11));
        this.parsingTable.Add("<paren-expr>", TokenType.OPEN_PARENTHESES, rules.get(11));
        this.parsingTable.Add("<paren-expr>", TokenType.PLUS, rules.get(4));
        this.parsingTable.Add("<paren-expr>", TokenType.MULTIPLY, rules.get(5));
        this.parsingTable.Add("<paren-expr>", TokenType.EQUALS, rules.get(6));
        this.parsingTable.Add("<paren-expr>", TokenType.MINUS, rules.get(7));
        this.parsingTable.Add("<paren-expr>", TokenType.CONDITIONAL, rules.get(8));
        this.parsingTable.Add("<paren-expr>", TokenType.LAMBDA, rules.get(9));
        this.parsingTable.Add("<paren-expr>", TokenType.LET, rules.get(10));
    }

    private void SetupRules() {
        this.rules = List.of(
            new ProductionRule(nonTerminals.get('S'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('E'), new Token(TokenType.NUMBER)),
            new ProductionRule(nonTerminals.get('E'), new Token(TokenType.IDENTIFIER)),
            new ProductionRule(nonTerminals.get('E'), new Token("(", TokenType.OPEN_PARENTHESES), nonTerminals.get('P'), new Token(")", TokenType.CLOSE_PARENTHESES)),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.PLUS), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.MULTIPLY), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.EQUALS), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.MINUS), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.CONDITIONAL), nonTerminals.get('E'), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.LAMBDA), new Token(TokenType.IDENTIFIER), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.LET), new Token(TokenType.IDENTIFIER), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), nonTerminals.get('E'), nonTerminals.get('e')),
            new ProductionRule(nonTerminals.get('e'), nonTerminals.get('E'), nonTerminals.get('e')),
            new ProductionRule(nonTerminals.get('e'), new Token(TokenType.EMPTY))
        );
    }

    public void SetupNonTerminalTokens() {
        this.nonTerminals = Map.of(
            'S', new Token("<program>", TokenType.NON_TERMINAL),
            'E', new Token("<expr>", TokenType.NON_TERMINAL),
            'e', new Token("<expr>*", TokenType.NON_TERMINAL),
            'P', new Token("<paren-expr>", TokenType.NON_TERMINAL)
        );
    }

    public ParseTree<Token> Parse(List<Token> tokens) throws ExpressionException, InvalidNodeException {
        return Parse(tokens, false);
    }

    public ParseTree<Token> Parse(List<Token> tokens, boolean isVerbose) throws ExpressionException, InvalidNodeException {
        if (isVerbose) {
            System.out.println("Starting LL1 Parsing...");
        }
        var stack = new Stack<Token>();
        var startingToken = nonTerminals.get('S');
        var root = new TreeNode<Token>(startingToken);
        var parseTree = new ParseTree<Token>(root);

        stack.push(new Token("$"));
        stack.push(startingToken);

        var currentInputTokenIndex = 0;

        while (currentInputTokenIndex < tokens.size() && stack.size() != 0) {
            if (isVerbose) {
                System.out.println("Current LL1 Stack: " + stack.toString());
            }

            var top = stack.peek();
            // get current tree node based on stack top token (this is value of tree node)
            var currentTreeNode = parseTree.getTreeNode(top);
            // if there is no node in the tree of that value then we should throw 
            // since it should have already been pushed to the stack in the last iteration
            if (currentTreeNode == null) {
                throw new ExpressionException("Stack top has a token \"" + top.GetValue() +  "\" which was not found in the parse tree.");
            }
            parseTree.SetCurrentNode(currentTreeNode);

            var currentToken = tokens.get(currentInputTokenIndex);
            var row = parsingTable.GetRow(top.GetValue() != null ? top.GetValue().toString() : top.Type.toString());

            if (row == null || row.isEmpty()) {
                if (top.Type.equals(currentToken.Type)) {
                    currentInputTokenIndex++;
                    stack.pop();
                    if (isVerbose) {
                        System.out.println("\tConsumed symbol '" + currentToken.GetValue() + "'");
                    }
                } else {
                    throw new ExpressionException("No production rule for this token. '" + currentToken.GetValue() + "' " + currentToken.Type);
                }
            } else {
                // we add to the parse tree here...
                var rule = row.get(currentToken.Type);

                if (rule == null) {
                    throw new ExpressionException("No production rule for this token. '" + currentToken.GetValue() + "' " + currentToken.Type);
                }

                stack.pop();
                // make new list of new token objects (while also adding to parse tree)
                var pushTokens = new ArrayList<Token>();
                for (var token : rule.GetRHS()) {
                    var pushToken = new Token(token.GetValue(), token.Type);
                    pushTokens.add(pushToken);
                    parseTree.Add(new TreeNode<Token>(pushToken));
                }
                // add to stack in reverse order
                for (var token : pushTokens.reversed()) {
                    if (!token.Type.equals(TokenType.EMPTY)) {
                        stack.push(token);
                    }
                }
                if (isVerbose) {
                    System.out.println("\tUsed rule No." + (rules.indexOf(rule) + 1) + " : " + rule.toString());
                }
            }
        }

        var endSymbol = stack.pop();
        if (!endSymbol.GetValue().toString().equals("$")) {
            throw new ExpressionException("The stack was not empty after parsing was finished.");
        }

        if (isVerbose) {
            System.out.println("Finished LL1 Parsing.");
        }

        return parseTree;
    }
}
