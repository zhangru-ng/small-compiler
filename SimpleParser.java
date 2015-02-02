package cop5555sp15;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import static cop5555sp15.TokenStream.Kind.*;

public class SimpleParser {
	
	//the token which is parsed currently
	private Token t;
	 
	//local references to TokenStream objects for convenience
	final TokenStream stream;
		
	public SimpleParser(TokenStream stream){
		this.stream = stream;
		Scanner s = new Scanner(stream);
		s.scan();
		consume();
	}
	
	public boolean parse() {
		clasS();
		return isKind(EOF);  
	}

	
	private void consume() 
	{
		t = stream.nextToken();
	}

	private boolean isKind(Kind kind) {
		return kind == t.kind;
	}

	private void match(Kind kind) {
		if (isKind(kind)) {
			consume();
		} else{
			error("expected" + kind);
		}			
	}

	private void error(String msg) {
		//deal with error
		System.out.println(msg);
	}
	
	private void clasS(){
		match(KW_CLASS);
		match(IDENT);
		block();
	}
	
	private void block(){
		while(isKind(KW_INT) || isKind(KW_BOOLEAN) || isKind(KW_STRING) || isKind(IDENT) || isKind(KW_PRINT) || 
			  isKind(KW_WHILE) || isKind(KW_IF) || isKind(KW_RETURN) || isKind(KW_DEF)){
			if(isKind(KW_INT) || isKind(KW_BOOLEAN) || isKind(KW_STRING)){
				declaration();
			}else{
				command();
			}
			match(SEMICOLON);
		}
	}
	
	private void declaration(){
		type();
		match(IDENT);
	}
	
	private void type(){
		switch(t.kind){
		case KW_INT:
			match(KW_INT);
			break;
		case KW_BOOLEAN:
			match(KW_BOOLEAN);
			break;
		case KW_STRING:
			match(KW_STRING);
			break;
		default:
			error("beg:"+t.beg+" "+"end"+t.end+"unmatch in type");		
		}
	}
	
	private void command(){
		switch(t.kind){
		case IDENT:
			lValue();
			match(ASSIGN);
			expression();
			break;
		case KW_PRINT:
			match(KW_PRINT);
			expression();
			break;
		case KW_WHILE:
			match(LPAREN);
			expression();
			match(RPAREN);
			block();
			break;
		case KW_IF:
			match(KW_IF);
			match(LPAREN);
			expression();
			match(RPAREN);
			block();
			if(isKind(KW_ELSE)){
				match(KW_ELSE);
				block();
			}
			break;
		case KW_RETURN:
			match(KW_RETURN);			
			expression();
			break;
		case KW_DEF:
			match(KW_DEF);
			lValue();
			match(ASSIGN);
			expression();
			break;
		default:
			error("beg:"+t.beg+" "+"end"+t.end+"unmatch in command");	
		}
	}
	
	private void lValue(){
		if(isKind(IDENT)){
			match(IDENT);
			if(isKind(LSQUARE)){
				match(LSQUARE);
				expression();
				match(RSQUARE);
			}			
		}
	}
	
	private void pairList(){
		match(LCURLY);
		if(isKind(LSQUARE)){
			match(LSQUARE);
			pair();
			while(isKind(COMMA)){
				match(COMMA);
				pair();
			}
			match(RCURLY);
		}else if(isKind(RCURLY)){
			match(RCURLY);
		}else{
			error("beg:"+t.beg+" "+"end"+t.end+"unmatch in pairList");		
		}
	}
	
	private void pair(){
		match(LSQUARE);
		expression();
		match(COMMA);
		expression();
		match(RSQUARE);
	}
	
	private void expression(){
		term();
		while(isKind(BAR) || isKind(AND) || isKind(EQUAL) || isKind(NOTEQUAL) || isKind(LT) || 
			  isKind(GT) || isKind(LE) || isKind(GE)){
			relOp();
			term();
		}
	}
	
	private void term(){
		element();
		while(isKind(PLUS) || isKind(MINUS)){
			weakOp();
			element();
		}
	}
	
	private void element(){
		factor();
		while(isKind(TIMES) || isKind(DIV)){
			strongOp();
			factor();
		}
	}

	private void factor(){
		switch(t.kind){
		case IDENT:
			lValue();
			break;
		case INT_LIT:
			match(INT_LIT);
			break;
		case BL_TRUE:
			match(BL_TRUE);
			break;
		case BL_FALSE:
			match(BL_FALSE);
			break;
		case STRING_LIT:
			match(STRING_LIT);
			break;
		case NL_NULL:
			match(NL_NULL);
			break;
		case LPAREN:
			expression();
			break;
		case NOT:
			factor();
			break;
		case MINUS:
			factor();
			break;
		case LSQUARE:
			pairList();
			break;
		default:
			error("beg:"+t.beg+" "+"end"+t.end+"unmatch in factor");			
		}
	}
	
	private void relOp(){
		switch(t.kind){
		case BAR:
			match(BAR);
			break;
		case AND:
			match(AND);
			break;
		case EQUAL:
			match(EQUAL);
			break;
		case NOTEQUAL:
			match(NOTEQUAL);
			break;
		case LT:
			match(LT);
			break;
		case GT:
			match(GT);
			break;
		case LE:
			match(LE);
			break;
		case GE:
			match(GE);
			break;
		default:
			error("beg:"+t.beg+" "+"end"+t.end+"unmatch in relOP");
		}
	}
	
	private void weakOp(){
		switch(t.kind){
		case PLUS:
			match(PLUS);
			break;
		case MINUS:
			match(MINUS);
			break;
		default:
			error("beg:"+t.beg+" "+"end"+t.end+"unmatch in weakOP");
		}
	}
	
	private void strongOp(){
		switch(t.kind){
		case TIMES:
			match(TIMES);
			break;
		case DIV:
			match(DIV);
			break;
		default:
			error("beg:"+t.beg+" "+"end"+t.end+"unmatch in strongOP");
		}
	}
	
	public static void main(String[] args) {
		TokenStream st = new TokenStream("0123 1020 5400 00031");
		SimpleParser pa = new SimpleParser(st);
		pa.parse();
	}

}
