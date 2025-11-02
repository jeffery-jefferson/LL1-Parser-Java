package Impl.Runners;

import java.util.List;

import Exceptions.*;
import Impl.Automata.Dfa;
import Impl.LexicalAnalyser.Lexer;
import Impl.Parser.LL1Parser;
import Impl.Parser.ParseTreeSimplifier;
import Models.*;

public class Runner {
    public static ParseTree<Token> Run(String input) {
        return Runner.Run(input, true);
    }

    public static ParseTree<Token> Run(String input, boolean isVerbose) {
        var dfa = new Dfa(input);
        setupDFA(dfa);

        var lexer = new Lexer(dfa);

        List<Token> tokens = null;
        try {
            tokens = lexer.Tokenize(input, isVerbose);
        } catch (NumberException ex) {
            System.out.println("Malformed number: " + ex.getMessage());
            return null;
        } catch (ExpressionException ex) {
            System.out.println("Invalid expression: " + ex.getMessage());
            return null;
        }
        if (isVerbose) {
            System.out.println("Tokens: " + tokens.toString());
        }

        var parser = new LL1Parser(input);
        ParseTree<Token> result = null;
        try {
            result = parser.Parse(tokens, isVerbose);
        } catch (ExpressionException ex) {
            System.out.println("Invalid expression: " + ex.getMessage());
            return null;
        } catch (InvalidNodeException ex) {
            System.out.println("An error occurred during parse tree generation: " + ex.getMessage());
            return null;
        }
        if (isVerbose) {
            System.out.println("\nParse Tree:");
            System.out.println(result);
            System.out.println("Simplified representation:");
            System.out.println(ParseTreeSimplifier.simplifyToString(result.getRoot()));
        }
        return result;
    }

    // this is pretty chunky since every transition is technically possible
    // I wanted to reuse the DFA architecture so...
    static void setupDFA(Dfa dfa) {
        var start = new State("START");
        var number = new State("NUMBER");
        var zero = new State("ZERO");
        var identifier = new State("IDENTIFIER");
        var openParentheses = new State("OPEN_PARENTHESES");
        var closeParentheses = new State("CLOSE_PARENTHESES");
        var lambda = new State("LAMBDA");
        var let = new State("LET");
        var whitespace = new State("WHITESPACE");
        var operator = new State("OPERATOR");

        var states = List.of(start, number, zero, identifier, openParentheses, operator, closeParentheses, lambda, let, whitespace);
        var operators = List.of('\u002B', '\u2212', '\u00D7', '\u003D', '\u003F'); // plus, minus, mult, equals, conditional
        var lambdaOperator = '\u03BB';
        var letOperator = '\u225C';

        dfa.AddStates(states);
        dfa.AddAcceptStates(List.of(start, number, identifier, closeParentheses));
        
        dfa.SetStartState(start);

        for (int i = 1; i < 10; i++) {
            dfa.AddTransition(start, number, (char)('0' + i));
            dfa.AddTransition(number, number, (char)('0' + i));
            dfa.AddTransition(openParentheses, number, (char)('0' + i));
            dfa.AddTransition(operator, number, (char)('0' + i));
            dfa.AddTransition(whitespace, number, (char)('0' + i));
            dfa.AddTransition(identifier, identifier, (char)('0' + i));
        }
        dfa.AddTransition(number, number, '0');
        dfa.AddTransition(whitespace, number, '0');
        dfa.AddTransition(identifier, identifier, '0');

        dfa.AddTransition(start, zero, '0');
        dfa.AddTransition(openParentheses, zero, '0');
        dfa.AddTransition(operator, zero, '0');
        dfa.AddTransition(whitespace, zero, '0');
        
        for (int i = 0; i < 26; i++) {
            dfa.AddTransition(start, identifier, (char)('a' + i));
            dfa.AddTransition(start, identifier, (char)('A' + i));
            dfa.AddTransition(identifier, identifier, (char)('a' + i));
            dfa.AddTransition(identifier, identifier, (char)('A' + i));
            dfa.AddTransition(lambda, identifier, (char)('a' + i));
            dfa.AddTransition(lambda, identifier, (char)('A' + i));
            dfa.AddTransition(let, identifier, (char)('a' + i));
            dfa.AddTransition(let, identifier, (char)('A' + i));
            dfa.AddTransition(whitespace, identifier, (char)('a' + i));
            dfa.AddTransition(whitespace, identifier, (char)('A' + i));
            dfa.AddTransition(openParentheses, identifier, (char)('a' + i));
            dfa.AddTransition(openParentheses, identifier, (char)('A' + i));
        }

        dfa.AddTransition(start, openParentheses, '(');
        dfa.AddTransition(number, openParentheses, '(');
        dfa.AddTransition(identifier, openParentheses, '(');
        dfa.AddTransition(whitespace, openParentheses, '(');
        dfa.AddTransition(openParentheses, openParentheses, '(');

        for (var op : operators) {
            dfa.AddTransition(openParentheses, operator, op);
            dfa.AddTransition(whitespace, operator, op);
        }

        dfa.AddTransition(openParentheses, lambda, lambdaOperator);
        dfa.AddTransition(openParentheses, let, letOperator);
        dfa.AddTransition(whitespace, lambda, lambdaOperator);
        dfa.AddTransition(whitespace, let, letOperator);

        for (var state : states) {
            dfa.AddTransition(state, whitespace, ' ');
        }

        dfa.AddTransition(whitespace, closeParentheses, ')');
        dfa.AddTransition(number, closeParentheses, ')');
        dfa.AddTransition(closeParentheses, closeParentheses, ')');
        dfa.AddTransition(identifier, closeParentheses, ')');
        dfa.AddTransition(zero, closeParentheses, ')');
    }
}
