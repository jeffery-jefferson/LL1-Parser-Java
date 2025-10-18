package Models;

import java.util.List;

public class ProductionRule {
    /*
    ProductionRule Structure:
        - LHS will contain a token (usually a single non-terminal)
        - RHS will contain a list of tokens (can be a combnination of terminals and non-terminals)
    this should represent:
        LHS -> RHS
    
    The reason why we keep track of production rule elements in tokens is to do with
    the token type. Consider the example:
        Input string is (+ 1 1)
        Consider when parsing "1 1)"
            Stack: [E, E, ), $]
            Tokens: [NUMBER, NUMBER, CLOSE_PARENTHESES]
        Then when using the parsing table we should get the production rule E -> NUMBER.
        Then we can adjust the stack with this rule:
            Stack: [NUMBER, E, ), $]
            Tokens: [NUMBER, NUMBER, CLOSE_PARENTHESES]
        And then the comparison is simpler.
    */
    private Token LHS;
    private List<Token> RHS;

    public ProductionRule(Token lhs, Token... rhs) {
        this.LHS = lhs;
        this.RHS = List.of(rhs);
    }

    public Token GetLHS() {
        return this.LHS;
    }

    public List<Token> GetRHS() {
        return this.RHS;
    }
}
