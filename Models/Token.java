package Models;


public class Token {

    private Object value;
    public TokenType Type;

    public enum TokenType {
        NUMBER, 
        WHITESPACE, 
        IDENTIFIER, 
        PLUS, 
        MULTIPLY, 
        MINUS, 
        EQUALS, 
        CONDITIONAL,
        LAMBDA,
        LET,
        OPEN_PARENTHESES,
        CLOSE_PARENTHESES
    }

    public Token(Object tokenValue) {
        this.value = tokenValue;
        this.Type = GetTokenType(this.value);
    }

    public TokenType GetTokenType(Object value) {
        var tokenString = this.value.toString();
        TokenType result = null;
        try {
            Integer.parseInt(tokenString);
            result = TokenType.NUMBER;
        } catch (NumberFormatException ex) {
            if (tokenString.length() > 1) {
                // must be id
                result = TokenType.IDENTIFIER;
            } else {
                var tokenCharValue = tokenString.charAt(0);
                switch (tokenCharValue) {
                    // plus, minus, mult, equals, conditional, lambda, let
                    // '\u002B', '\u2212', '\u00D7', '\u003D', '\u003F', '\u03BB', '\u225C'
                    case '\u002B':
                        result = TokenType.PLUS;
                        break;
                    case '\u2212':
                        result = TokenType.MINUS;
                        break;
                    case '\u00D7':
                        result = TokenType.MULTIPLY;
                        break;
                    case '\u003D':
                        result = TokenType.EQUALS;
                        break;
                    case '\u003F':
                        result = TokenType.CONDITIONAL;
                        break;
                    case '\u03BB':
                        result = TokenType.LAMBDA;
                        break;
                    case '\u225C':
                        result = TokenType.LET;
                        break;
                    case '(':
                        result = TokenType.OPEN_PARENTHESES;
                        break;
                    case ')':
                        result = TokenType.CLOSE_PARENTHESES;
                        break;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
