package cop5555sp15;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * This class holds the tokenize input. It is initialized with the input
 * (several constructors provide different options for providing the input) and
 * passed to a Scanner which fills in the tokens. The nextToken method is used
 * by the parser to retrieve the Tokens.
 * 
 * @author Beverly Sanders
 *
 */
public class TokenStream {
	char[] inputChars; // input
	public final ArrayList<Token> tokens = new ArrayList<Token>(); // holds tokens after scan
																	

	/* provide input in char array */
	public TokenStream(char[] inputChars) {
		this.inputChars = inputChars;
	}

	/* provide input via a Reader */
	public TokenStream(Reader r) {
		this.inputChars = getChars(r);
	}

	/* provide input via a String */
	public TokenStream(String inputString) {
		int length = inputString.length();
		inputChars = new char[length];
		inputString.getChars(0, length, inputChars, 0);
	}

	// reads all the characters in the given reader into a char array.
	private char[] getChars(Reader r) {
		StringBuilder sb = new StringBuilder();
		try {
			int ch = r.read();
			while (ch != -1) {
				sb.append((char) ch);
				ch = r.read();
			}
		} catch (IOException e) {
			throw new RuntimeException("IOException");
		}
		char[] chars = new char[sb.length()];
		sb.getChars(0, sb.length(), chars, 0);
		return chars;
	}

	private int pos = 0;

	/** returns the next token and increments the position */
	public Token nextToken() {
		return tokens.get(pos++);
	}

	/** resets the position in the token stream */
	public void reset() {
		pos = 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token t : tokens) {
			sb.append(t.toString());
			sb.append('\n');
		}
		return sb.toString();
	}


	public static enum Kind {
		IDENT,
		/* reserved words */
		KW_INT, KW_STRING, KW_BOOLEAN, KW_IMPORT, KW_CLASS, KW_DEF, 
		KW_WHILE, KW_IF, KW_ELSE, KW_RETURN, KW_PRINT, KW_SIZE, KW_KEY, KW_VALUE,
		/* boolean literals */
		BL_TRUE, BL_FALSE,
		/* null literal */
		NL_NULL,
		/* separators */
		DOT, // .
		RANGE, // ..
		SEMICOLON, // ;
		COMMA, // ,
		LPAREN, // (
		RPAREN, // )
		LSQUARE, // [
		RSQUARE, // ]
		LCURLY, // {
		RCURLY, // }
		COLON, // :
		QUESTION, // ?
		ASSIGN, // =
		BAR, // |
		AND, // &
		EQUAL, // ==
		NOTEQUAL, // !=
		LT, // <
		GT, // >
		LE, // <=
		GE, // >=
		PLUS, // +
		MINUS, // -
		TIMES, // *
		DIV, // /
		MOD, // %
		NOT, // !
		LSHIFT, // <<
		RSHIFT, // >>
		ARROW, // ->
		AT, // @
		INT_LIT, STRING_LIT,
		/* end of file */
		EOF,
		/* error tokens */
		ILLEGAL_CHAR,         //a character that cannot appear in that context
		UNTERMINATED_STRING,  //end of input is reached before the closing "
		UNTERMINATED_COMMENT  //end of input is reached before the closing */
	}

	/*
	 * This is a non-static inner class. Each instance is linked to a instance
	 * of StreamToken and can access that instance's variables.
	 * 
	 * Example of token creation where stream is an instance of TokenStream:
	 * Token t = stream.new Token(SEMI, beg, end, line);
	 */

	public class Token {
		public final Kind kind;
		public final int beg;
		public final int end;
		public final int lineNumber;

		public Token(Kind kind, int beg, int end, int lineNumber) {
			this.kind = kind;
			this.beg = beg;
			this.end = end;
			this.lineNumber = lineNumber;
		}

		/* this should only be applied to Tokens with kind==INT_LIT */
		public int getIntVal() {
			assert kind == Kind.INT_LIT : "attempted to get value of non-number token";
			return Integer.valueOf(getText());
		}

		/* this should only be applied to Tokens with kind==BOOLEAN_LIT */
		public boolean getBooleanVal() {
			assert (kind == Kind.BL_TRUE || kind == Kind.BL_FALSE) : "attempted to get boolean value of non-boolean token";
			return kind == Kind.BL_TRUE;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		/**This method handles the escape characters in String literals.  The
		 * getText method returns the string from the token's characters.  This means that
		 * the Scanner can ignore escape characters.
		 * 
		 * @return
		 */
		public String getText() {
			if (inputChars.length < end) {
				assert kind == Kind.EOF && beg == inputChars.length;
				return "";
			}
			if (kind == Kind.STRING_LIT) {
				StringBuilder sb = new StringBuilder();
				for (int i = beg+1; i < end-1; ++i) {
					char ch = inputChars[i];
					if (ch == '\\') {
						char nextChar = inputChars[i+1];
						if (nextChar == '"') {
							sb.append('"');
							i++;
						} else if (nextChar == 'n')  {
							sb.append('\n');
							i++;
						} else if (nextChar == 'r') {
							sb.append('\r');
							i++;
						} else if (nextChar == '\\') {
								sb.append('\\');
								i++;
						}
					} else {
						sb.append(ch);
					}	
				}
				return sb.toString();
			}
			return String.valueOf(inputChars, beg, end - beg);
		}

		public String toString() {
			return (new StringBuilder("<").append(kind).append(",")
					.append(getText()).append(",").append(beg).append(",")
					.append(end).append(",").append(lineNumber).append(">"))
					.toString();
		}

		public boolean equals(Object o) {
			if (!(o instanceof Token))
				return false;
			Token other = (Token) o;
			return kind == other.kind && beg == other.beg && end == other.end
					&& lineNumber == other.lineNumber;
		}
	}
}
