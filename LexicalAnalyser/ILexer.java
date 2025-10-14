package LexicalAnalyser;

import java.util.List;
import Models.Token;

public interface ILexer {
    public List<Token> Tokenize(String input);
}
