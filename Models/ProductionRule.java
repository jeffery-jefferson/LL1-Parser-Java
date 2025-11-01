package Models;

import java.util.List;

public class ProductionRule {
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

    @Override
    public String toString() {
        return this.LHS + " -> " + this.RHS;
    }
}
