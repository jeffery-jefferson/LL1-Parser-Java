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
		var identifierBuffer = new ArrayList<Character>();
		var isProcessingIdentifier = false;
		while (!dfa.IsFinished()) {
			try {
				var processedChar = dfa.Next().charValue();
				System.out.println("Processed: '" + processedChar + "', now on state: " + dfa.CurrentState);
				// Collect numbers in the buffer
				if (IsNumber(processedChar)) {
					if (isProcessingIdentifier) {
						identifierBuffer.add(processedChar);
					} else {
						numberBuffer.add(processedChar);
					}
					continue;
				}

				if (IsAlphabet(processedChar)) {
					isProcessingIdentifier = true;
					identifierBuffer.add(processedChar);
					continue;
				} else {
					isProcessingIdentifier = false;
				}

				// Tokenize
				if (numberBuffer.size() > 0) {
					var num = CharArrayToDouble(numberBuffer);
					numberBuffer.clear();
					var token = new Token(num);
					result.add(token);
				}
				if (identifierBuffer.size() > 0) {
					var identifier = CharArrayToString(identifierBuffer);
					identifierBuffer.clear();
					var token = new Token(identifier);
					result.add(token);
				}
				if (!IsWhitespace(processedChar)) {
					var token = new Token(processedChar);
					result.add(token);
				}

			} catch (ExpressionException ex) {
				if (numberBuffer.size() > 0) {
					throw new NumberException("Line 50");
				}
				throw ex;
			}
		}

		// if there are numbers left in the buffer tokenize this too
		if (numberBuffer.size() > 0) {
			result.add(new Token(CharArrayToDouble(numberBuffer)));
		}
		if (identifierBuffer.size() > 0) {
			result.add(new Token(CharArrayToString(identifierBuffer)));
		}

		
		// now check if we can accept at this finished state 
		if (!dfa.CanAccept()) {
			if (numberBuffer.size() > 0) {
				throw new NumberException("Line 64");
			}
			throw new ExpressionException("Line 66");
		}

		return result;
    }

    static boolean IsNumber(char c) {
		return c >= (char)('0') && c <= (char)('9');
	}

	static boolean IsWhitespace(char c) {
		return c == ' ';
	}

	static boolean IsAlphabet(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

    static Double CharArrayToDouble(ArrayList<Character> charArray) {
		StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            sb.append(c);
        }
        return Double.parseDouble(sb.toString());
	}

	static String CharArrayToString(ArrayList<Character> charArray) {
		StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            sb.append(c);
        }
        return sb.toString();
	}
}