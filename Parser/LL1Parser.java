package Parser;

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
    private Map<Token, Map<TokenType, ProductionRule>> parsingTable;
    private Map<Character, Token> nonTerminals;
    private List<ProductionRule> rules;

    public LL1Parser(String inputString) {

        // extract this setup later
        nonTerminals = Map.of(
            'S', new Token("<program>", TokenType.NON_TERMINAL),
            'E', new Token("<expr>", TokenType.NON_TERMINAL),
            'e', new Token("<expr>*", TokenType.NON_TERMINAL),
            'P', new Token("<paren-expr>", TokenType.NON_TERMINAL)
        );

        rules = List.of(
            new ProductionRule(nonTerminals.get('S'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('E'), new Token(TokenType.NUMBER)),
            new ProductionRule(nonTerminals.get('E'), new Token(TokenType.IDENTIFIER)),
            new ProductionRule(nonTerminals.get('E'), new Token("(", TokenType.OPEN_PARENTHESES), nonTerminals.get('P'), new Token(")", TokenType.CLOSE_PARENTHESES)),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.PLUS), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.MULTIPLY), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.EQUALS), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.MINUS), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.CONDITIONAL), nonTerminals.get('E'), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.LAMBDA), new Token(TokenType.IDENTIFIER), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), new Token(TokenType.LET), new Token(TokenType.IDENTIFIER), nonTerminals.get('E'), nonTerminals.get('E')),
            new ProductionRule(nonTerminals.get('P'), nonTerminals.get('E'), nonTerminals.get('e')),
            new ProductionRule(nonTerminals.get('e'), nonTerminals.get('E'), nonTerminals.get('e')),
            new ProductionRule(nonTerminals.get('e'), new Token(TokenType.EMPTY))
        );

        parsingTable = Map.of(
            nonTerminals.get('S'), Map.of(TokenType.NUMBER, rules.get(0), TokenType.IDENTIFIER, rules.get(0), TokenType.OPEN_PARENTHESES, rules.get(0)),
            nonTerminals.get('E'), Map.of(TokenType.NUMBER, rules.get(1), TokenType.IDENTIFIER, rules.get(2), TokenType.OPEN_PARENTHESES, rules.get(3)),
            nonTerminals.get('e'), Map.of(TokenType.NUMBER, rules.get(12), TokenType.IDENTIFIER, rules.get(12), TokenType.OPEN_PARENTHESES, rules.get(12), TokenType.CLOSE_PARENTHESES, rules.get(13)),
            nonTerminals.get('P'), Map.of(TokenType.NUMBER, rules.get(11), TokenType.IDENTIFIER, rules.get(11), TokenType.OPEN_PARENTHESES, rules.get(11), 
                TokenType.PLUS, rules.get(4),
                TokenType.MULTIPLY, rules.get(5),
                TokenType.EQUALS, rules.get(6),
                TokenType.MINUS, rules.get(7),
                TokenType.CONDITIONAL, rules.get(8),
                TokenType.LAMBDA, rules.get(9),
                TokenType.LET, rules.get(10)
            )
        );
    }

    public ParseTree<Token> Parse(List<Token> tokens) throws ExpressionException, InvalidNodeException {
        var stack = new Stack<Token>();
        var startingToken = nonTerminals.get('S');
        var root = new TreeNode<Token>(startingToken);
        var parseTree = new ParseTree<Token>(root);

        stack.push(new Token("$"));
        stack.push(startingToken);

        var currentInputTokenIndex = 0;
        while (currentInputTokenIndex < tokens.size() && stack.size() != 0) {
            System.out.println("Current LL1 Stack: " + stack.toString());

            var top = stack.peek();
            // get current tree node based on stack top token (this is value of tree node)
            var currentTreeNode = parseTree.GetTreeNodeByValue(top);
            // if there is no node in the tree of that value then we should throw 
            // since it should have already been pushed to the stack in the last iteration
            if (currentTreeNode == null) {
                throw new ExpressionException("Stack top has a token \"" + top.GetValue() +  "\" which was not found in the parse tree.");
            }
            parseTree.SetCurrentNode(currentTreeNode);

            var currentToken = tokens.get(currentInputTokenIndex);
            var row = parsingTable.get(top);
            if (row == null) {
                if (top.Type.equals(currentToken.Type)) {
                    currentInputTokenIndex++;
                    stack.pop();
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
                // add to stack in reverse order
                for (var token : rule.GetRHS().reversed()) {
                    if (!token.Type.equals(TokenType.EMPTY)) {
                        stack.push(token);
                    }
                }
                // add to parse tree in correct order
                for (var token : rule.GetRHS()) {
                    if (!token.Type.equals(TokenType.EMPTY)) {
                        parseTree.Add(new TreeNode<Token>(token));
                    }
                }
                System.out.println("\tUsed rule No." + (rules.indexOf(rule) + 1) + " : " + rule.toString());
            }
        }

        return parseTree;
    }
}
