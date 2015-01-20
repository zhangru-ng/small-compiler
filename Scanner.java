package cop5555sp15;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import static cop5555sp15.TokenStream.Kind.*;

public class Scanner {

	private enum State {
		START, 
		GOT_EQUALS, 
		GOT_DOT,
		GOT_EXCLAM,
		GOT_LANGLE,
		GOT_RANGLE,
		GOT_HYPHEN,
		
		GOT_SLASH, 
		GOT_SLASHSTAR,
		GOT_SLASH2STAR,
		
		IDENT_PART, 
		DIGITS, 
		EOF
	}
	//the current state of the DFA 
	private State state;
	
	//the index of (next) char to process during scanning, or if none, past the end of the array
	private int index; 
	
	//the char to process during scanning
	private char ch;

	//the current line number
	private int line;	
	
    //local references to TokenStream objects for convenience
    final TokenStream stream;  //set in constructor

	public Scanner(TokenStream stream) {
		//IMPLEMENT THIS
		this.stream = stream;
	}

	
	private char getch(){
	//	index++;
		return stream.inputChars[index++];
	}

	// Fills in the stream.tokens list with recognized tokens 
     //from the input
	public void scan() {
		Token t = null;
		ch = getch();
		do{
		//System.out.println("perform next()");
			t = next();
			stream.tokens.add(t);
		//	System.out.println("beg:"+t.beg+"\t"+"end:"+t.end+"\t"+"kind:"+t.kind);
		}while(!t.kind.equals(EOF));	
	}
	
	public Token next(){
		state = State.START;
		Token t = null;
		int begOffset = 0;		

		do{
			switch(state){
			case START:
				begOffset = index;
				switch(ch){
				case '.':
					state = State.GOT_DOT;
					break;
				case ';':
					t= stream.new Token(SEMICOLON, begOffset, index, line);
					break;
				case ',':
					t= stream.new Token(COMMA, begOffset, index, line);
					break;
				case '(':
					t= stream.new Token(LPAREN, begOffset, index, line);
					break;
				case ')':
					t= stream.new Token(RPAREN, begOffset, index, line);
					break;
				case '[':
					t= stream.new Token(LSQUARE, begOffset, index, line);
					break;
				case ']':
					t= stream.new Token(RSQUARE, begOffset, index, line);
					break;
				case '{':
					t= stream.new Token(LCURLY, begOffset, index, line);
					break;
				case '}':
					t= stream.new Token(RCURLY, begOffset, index, line);
					break;
				case ':':
					t= stream.new Token(COLON, begOffset, index, line);
					break;
				case '?':
					t= stream.new Token(QUESTION, begOffset, index, line);
					break;
				case '=':
					state = State.GOT_EQUALS;
					break;
				case '|':
					t= stream.new Token(BAR, begOffset, index, line);
					break;
				case '&':
					t= stream.new Token(AND, begOffset, index, line);
					break;	
				case '<':
					state = State.GOT_LANGLE;
					break;
				case '>':
					state = State.GOT_RANGLE;
					break;	
				case '+':
					t= stream.new Token(PLUS, begOffset, index, line);
					break;
				case '-':
					state = State.GOT_HYPHEN;
					break;
				case '*':
					t= stream.new Token(TIMES, begOffset, index, line);
					break;
				case '/':
					state = State.GOT_SLASH;
					break;
				case '%':
					t= stream.new Token(MOD, begOffset, index, line);
					break;		
				case '!':
					state = State.GOT_EXCLAM;
					break;
				case '@':
					t= stream.new Token(AT, begOffset, index, line);
					break;
		/************************************************************/		
				case 1://may be error
					state = State.EOF;
					break;
		/************************************************************/	
				default:
					if(Character.isDigit(ch)){
						state = State.DIGITS;
					}else if(Character.isJavaIdentifierStart(ch)){
						state = State.IDENT_PART;
					}else if(Character.isWhitespace(ch)){
						state = State.START;
					}else{
						System.out.println("other character");
					}
				}
				ch = getch();
				break;
				
			case GOT_DOT:
				switch(ch){
				case '.':
					t= stream.new Token(RANGE, begOffset, index, line);
					break;
				default:
					t= stream.new Token(DOT, begOffset, index--, line);
				}
				ch = getch();
				break;
			case GOT_EQUALS:
				switch(ch){
				case '=':
					t= stream.new Token(EQUAL, begOffset, index, line);
					break;
				default:
					t= stream.new Token(ASSIGN, begOffset, index--, line);
				}
				ch = getch();
				break;
			case GOT_LANGLE:
				switch(ch){
				case '=':
					t= stream.new Token(LE, begOffset, index, line);
					break;
				case '<':
					t= stream.new Token(LSHIFT, begOffset, index, line);
					break;
				default:
					t= stream.new Token(LT, begOffset, index--, line);
				}
				ch = getch();
				break;
			case GOT_RANGLE:
				switch(ch){
				case '=':
					t= stream.new Token(GE, begOffset, index, line);
					break;
				case '>':
					t= stream.new Token(RSHIFT, begOffset, index, line);
					break;
				default:
					t= stream.new Token(GT, begOffset, index--, line);
				}
				ch = getch();
				break;
			case GOT_HYPHEN:
				switch(ch){
				case '>':
					t= stream.new Token(ARROW, begOffset, index, line);
					break;
				default:
					t= stream.new Token(MINUS, begOffset, index--, line);
				}
				ch = getch();
				break;
				
			case GOT_SLASH:
				switch(ch){
				case '*':
					state = State.GOT_SLASHSTAR;
					break;
				default:
					t= stream.new Token(DIV, begOffset, index--, line);						
				}
				ch = getch();
				break;
			case GOT_SLASHSTAR:
				if(ch == '*'){
					state = State.GOT_SLASH2STAR;
				}
				ch = getch();
				break;
			case GOT_SLASH2STAR:
				switch(ch){
				case '/':
					state = State.START;
					break;
				case '*':
					break;
				default:
					state = State.GOT_SLASHSTAR;
				}
				ch = getch();
				break;
				
			case IDENT_PART:
				if(Character.isJavaIdentifierStart(ch)){
					state = State.IDENT_PART;
				}else{
					t= stream.new Token(IDENT, begOffset, --index, line);
					
				}
				ch = getch();
				break;
		/*	case DIGITS:
				if(Character.isDigit(ch)){
					state = State.DIGITS;
				}else{
					t= stream.new Token(NUM_LIT, begOffset, --index,line);
				}
				ch = getch();
				break;
		*/
			case EOF:
				t= stream.new Token(EOF, begOffset, index, line);
				break;
			default:
				assert false:"should not reach here";
			}			
		}while(t == null);
		return t;		
	}	
	
	public static void main(String[] args){
		TokenStream st = new TokenStream("asdhufidhf");
		Scanner sc = new Scanner(st);
		System.out.println(sc.stream.inputChars);
	}

}