package Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import Exceptions.ExpressionException;
import Models.ProductionRule;
import Models.Token;
import Models.Token.TokenType;

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

    private Stack<Token> stack;

    public LL1Parser(String inputString) {

        this.stack = new Stack<Token>();

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

    public void Parse(List<Token> tokens) throws ExpressionException {
        stack.push(new Token("$"));
        stack.push(nonTerminals.get('S'));

        var currentInputTokenIndex = 0;
        while (currentInputTokenIndex < tokens.size() && stack.size() != 0) {
            System.out.println("Current LL1 Stack: " + stack.toString());

            var top = stack.peek();
            var currentToken = tokens.get(currentInputTokenIndex);
            var row = parsingTable.get(top);
            if (row == null) {
                if (top.Type.equals(currentToken.Type)) {
                    currentInputTokenIndex++;
                    stack.pop();
                } else {
                    throw new ExpressionException("No production rule for this token.");
                }
            } else {
                var rule = row.get(currentToken.Type);

                if (rule == null) {
                    throw new ExpressionException();
                }

                stack.pop();
                for (var token : rule.GetRHS().reversed()) {
                    if (!token.Type.equals(TokenType.EMPTY)) {
                        stack.push(token);
                    }
                }
            }
        }
    }
}
