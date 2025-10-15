package LexicalAnalyser;

import java.util.List;

import Automata.IAutomata;
import Models.Token;

public class Lexer implements ILexer {

    private IAutomata automata;

    public Lexer(IAutomata dfa) {
        this.automata = dfa;
    }

    @Override
    public List<Token> Tokenize(String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Tokenize'");
    }
    
}