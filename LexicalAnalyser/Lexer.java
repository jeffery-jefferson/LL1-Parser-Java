package LexicalAnalyser;

import java.util.ArrayList;
import java.util.List;

import Automata.Dfa;
import Exceptions.ExpressionException;
import Exceptions.NumberException;
import Models.Token;

public class Lexer implements ILexer {

    private Dfa dfa;

    public Lexer(Dfa dfa) {
        this.dfa = dfa;
    }

    @Override
    public List<Token> Tokenize(String input) throws NumberException, ExpressionException {
		System.out.println("Starting state: " + dfa.StartState);

		// Tokenising logic
		var result = new ArrayList<Token>();
		var numberBuffer = new ArrayList<Character>();
		while (!dfa.IsFinished()) {
			try {
				var processedChar = dfa.Next().charValue();
				System.out.println("Processed: '" + processedChar + "', now on state: " + dfa.CurrentState);
				// Collect numbers in the buffer
				if (IsNumber(processedChar)) {
					numberBuffer.add(processedChar);
					continue;
				}

				// Tokenize
				if (numberBuffer.size() > 0) {
					var num = CharArrayToDouble(numberBuffer);
					numberBuffer.clear();
					var token = new Token(num);
					result.add(token);
				}
				if (!IsWhitespace(processedChar)) {
					var token = new Token(processedChar);
					result.add(token);
				}

			} catch (ExpressionException ex) {
				if (numberBuffer.size() > 0 || dfa.GetCurrentCharacter().equals('.')) {
					throw new NumberException();
				}
				throw ex;
			}
		}

		// if there are numbers left in the buffer tokenize this too
		if (numberBuffer.size() > 0) {
			result.add(new Token(CharArrayToDouble(numberBuffer)));
		}

		// now check if we can accept at this finished state 
		if (!dfa.CanAccept()) {
			if (numberBuffer.size() > 0) {
				throw new NumberException();
			}
			throw new ExpressionException();
		}

		return result;
    }

    static boolean IsNumber(char c) {
		return (int)c >= 30 && (int)c <= 39;
	}

	static boolean IsWhitespace(char c) {
		return c == ' ';
	}

    static Double CharArrayToDouble(ArrayList<Character> charArray) {
		StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            sb.append(c);
        }
        return Double.parseDouble(sb.toString());
	}
}