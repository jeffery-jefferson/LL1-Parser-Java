package LexicalAnalyser;

import java.util.List;

import Models.Token;

public class LexicalAnalyser implements ILexer {

    // could be the same lexer from Assignment 1?
    // maybe just implement a simple one... string.split or something...
    // yeah we can't do the same lexer from assignment 1 since that's a DFA implementation
    // we need a PDA to tokenize the CFG... wait no we can use a DFA since we aren't doing any validation of parentheses for example.
    // ok but for now we can do a simple string.split since this isn't against the criteria specifically

    @Override
    public List<Token> Tokenize(String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Tokenize'");
    }
    
}