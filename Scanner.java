package cop5555sp15;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import static cop5555sp15.TokenStream.Kind.*;
import java.util.HashMap;

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
		INT_PART, 
		STRING_PART,
		EOF
	}
	//the current state of the DFA 
	private State state;
	
	//the index of (next) char to process during scanning, or if none, past the end of the array
	private int index ; 
	
	//the char to process during scanning
	private char ch;

	//the current line number
	private int line = 1;	
	
    //local references to TokenStream objects for convenience
      //set in constructor
	final TokenStream stream;

	//the keywords hash map
	final HashMap<String, Kind> resWords = new HashMap<String, Kind>();			
    
	public void initKeyword(){
		this.resWords.put("int", Kind.KW_INT);
		this.resWords.put("string", Kind.KW_STRING);
		this.resWords.put("boolean", Kind.KW_BOOLEAN);
		this.resWords.put("import", Kind.KW_IMPORT);
		this.resWords.put("class", Kind.KW_CLASS);
		this.resWords.put("def", Kind.KW_DEF);
		this.resWords.put("while", Kind.KW_WHILE);
		this.resWords.put("if", Kind.KW_IF);
		this.resWords.put("else", Kind.KW_ELSE);
		this.resWords.put("return", Kind.KW_RETURN);
		this.resWords.put("print", Kind.KW_PRINT);
		this.resWords.put("true", Kind.BL_TRUE);
		this.resWords.put("false", Kind.BL_FALSE);
		this.resWords.put("null", Kind.NL_NULL);
	}
	
	public Scanner(TokenStream stream) {
		//initialize input stream
		this.stream = stream;
		//initialize keywords Hashmap
		this.initKeyword();
	}


	
	private char getch(){
		
		if(index < stream.inputChars.length){
			return stream.inputChars[index++];
		}else{
			index++;
			//deal with EOF
			return (char)-1;
		}
		
		
	}

	// Fills in the stream.tokens list with recognized tokens 
     //from the input
	public void scan() {
		Token t = null; 
		do{
		//System.out.println("perform next()");
			t = next();
			stream.tokens.add(t);
			System.out.println("beg:"+t.beg+"\t"+"end:"+t.end+"\t"+"kind:"+t.kind+"\t"+"line:"+t.lineNumber);
		}while(!t.kind.equals(EOF));	
	}
	
	public Token next(){
		state = State.START;
		Token t = null;
		int begOffset = 0;		

		do{
			ch = getch();
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
				case '"':
					state = State.STRING_PART;
					break;
				case '\n':
					//detect new line
					line++;
					break;	
				case (char) -1:
					//detect end of file
					state = State.EOF;
					break;
				default:
					if(Character.isDigit(ch)){
						if(ch == '0'){
							t= stream.new Token(INT_LIT, begOffset, index, line);
						}else{
							state = State.INT_PART;
						}						
					}else if(Character.isJavaIdentifierStart(ch)){
						state = State.IDENT_PART;
					}else if(Character.isWhitespace(ch)){
						state = State.START;
					}else{
						System.out.println("other character");
					}
				}
				break;
				
			case GOT_DOT:
				switch(ch){
				case '.':
					t= stream.new Token(RANGE, begOffset, index, line);
					break;
				default:
					t= stream.new Token(DOT, begOffset, --index, line);
				}
				break;
			case GOT_EQUALS:
				switch(ch){
				case '=':
					t= stream.new Token(EQUAL, begOffset, index, line);
					break;
				default:
					t= stream.new Token(ASSIGN, begOffset, --index, line);
				}
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
					t= stream.new Token(LT, begOffset, --index, line);
				}
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
					t= stream.new Token(GT, begOffset, --index, line);
				}
				break;
			case GOT_HYPHEN:
				switch(ch){
				case '>':
					t= stream.new Token(ARROW, begOffset, index, line);
					break;
				default:
					t= stream.new Token(MINUS, begOffset, --index, line);
				}
				break;
			case GOT_EXCLAM:
				switch(ch){
				case '=':
					t= stream.new Token(NOTEQUAL, begOffset, index, line);
					break;
				default:
					t= stream.new Token(NOT, begOffset, --index, line);
				}
				break;
			case GOT_SLASH:
				switch(ch){
				case '*':
					state = State.GOT_SLASHSTAR;
					break;
				default:
					t= stream.new Token(DIV, begOffset, --index, line);						
				}
				break;
			case GOT_SLASHSTAR:
				if(ch == '*'){
					state = State.GOT_SLASH2STAR;
				}
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
				break;
				
			case IDENT_PART:
				if(Character.isJavaIdentifierPart(ch)){
					state = State.IDENT_PART;
				}else{
					int len = index - begOffset;
					char[] temp = new char[len];
					for(int i=0; i<len; i++){
						temp[i] = stream.inputChars[begOffset + i -1];
					}
					String s = String.valueOf(temp);
					if(resWords.containsKey(s)){					
						t= stream.new Token(resWords.get(s), begOffset, --index, line);					
					}else{
						t= stream.new Token(IDENT, begOffset, --index, line);		
					}						
				}
				break;
			case INT_PART:
				if(Character.isDigit(ch)){
					state = State.INT_PART;
				}else{
					t= stream.new Token(INT_LIT, begOffset, --index,line);
				}		
				break;
			case STRING_PART:
				switch(ch){
				case '"':
					t= stream.new Token(STRING_LIT, begOffset, index, line);
					break;
				default:
					state = State.STRING_PART;					
				}
				break;
			case EOF:
				//take two iteration to end
				t= stream.new Token(EOF, begOffset, --index, line);
				break;
			default:
				assert false:"should not reach here";
			}			
		}while(t == null);
		return t;		
	}	
	
	public static void main(String[] args){
		TokenStream st = new TokenStream("string sw = null");
		Scanner sc = new Scanner(st);
		sc.scan();
		//System.out.println(sc.stream.inputChars);
	}

}