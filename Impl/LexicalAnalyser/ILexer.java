package Impl.LexicalAnalyser;

import java.util.List;

import Exceptions.ExpressionException;
import Exceptions.NumberException;
import Models.Token;

public interface ILexer {
    public List<Token> Tokenize(String input) throws NumberException, ExpressionException;
}
