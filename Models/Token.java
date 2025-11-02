package Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Token {

    private Object val;
    @JsonIgnore
    public TokenType Type;

    public enum TokenType {
        NUMBER, WHITESPACE, IDENTIFIER, PLUS, MULTIPLY, MINUS, EQUALS, CONDITIONAL, LAMBDA, LET, OPEN_PARENTHESES, CLOSE_PARENTHESES, NON_TERMINAL, EMPTY, END
    }

    public Token() {}
    public Token(Object tokenValue) {
        this.val = tokenValue;
        this.Type = GetTokenType(this.val);
    }

    public Token(TokenType tokenType) {
        this.val = null;
        this.Type = tokenType;
    }

    public Token(Object tokenValue, TokenType tokenType) {
        this.val = tokenValue;
        this.Type = tokenType;

    }

    public Object getVal() {
        return this.val;
    }

    public TokenType GetTokenType(Object value) {
        var tokenString = this.val.toString();
        TokenType result = null;
        try {
            Integer.parseInt(tokenString);
            result = TokenType.NUMBER;
        } catch (NumberFormatException ex) {
            if (tokenString.length() > 1) {
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
                    case '$':
                        result = TokenType.END;
                        break;
                    default: // now the NON_TERMINAL TokenType must be set manually.
                        result = TokenType.IDENTIFIER;
                        break;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        if (this.val == null) {
            return this.Type.toString();
        }
        return this.val.toString();
    }
}
