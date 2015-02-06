package cop5555sp15;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import static cop5555sp15.TokenStream.Kind.*;


public class SimpleParser {

	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;
		Kind[] expected;
		String msg;

		SyntaxException(Token t, Kind expected) {
			this.t = t;
			msg = "";
			this.expected = new Kind[1];
			this.expected[0] = expected;

		}

		public SyntaxException(Token t, String msg) {
			this.t = t;
			this.msg = msg;
		}

		public SyntaxException(Token t, Kind[] expected) {
			this.t = t;
			msg = "";
			this.expected = expected;
		}

		public String getMessage() {
			StringBuilder sb = new StringBuilder();
			sb.append(" error at token ").append(t.toString()).append(" ")
					.append(msg);
			sb.append(". Expected: ");
			for (Kind kind : expected) {
				sb.append(kind).append(" ");
			}
			return sb.toString();
		}
	}
	
	//the token which is parsed currently
	private Token t;	 

	//local references to TokenStream objects for convenience
	final TokenStream tokens;	

	SimpleParser(TokenStream tokens) {
		this.tokens = tokens;
		t = tokens.nextToken();
	}

	private Kind match(Kind kind) throws SyntaxException {
		if (isKind(kind)) {
			consume();
			return kind;
		}
		throw new SyntaxException(t, kind);
	}

	private Kind match(Kind... kinds) throws SyntaxException {
		Kind kind = t.kind;
		if (isKind(kinds)) {
			consume();
			return kind;
		}
		StringBuilder sb = new StringBuilder();
		for (Kind kind1 : kinds) {
			sb.append(kind1).append(kind1).append(" ");
		}
		throw new SyntaxException(t, "expected one of " + sb.toString());
	}

	private boolean isKind(Kind kind) {
		return (t.kind == kind);
	}

	private void consume() {
		if (t.kind != EOF)
			t = tokens.nextToken();
	}

	private boolean isKind(Kind... kinds) {
		for (Kind kind : kinds) {
			if (t.kind == kind)
				return true;
		}
		return false;
	}

	//This is a convenient way to represent fixed sets of
	//token kinds.  You can pass these to isKind.
	static final Kind[] REL_OPS = { BAR, AND, EQUAL, NOTEQUAL, LT, GT, LE, GE };
	static final Kind[] WEAK_OPS = { PLUS, MINUS };
	static final Kind[] STRONG_OPS = { TIMES, DIV };
	static final Kind[] VERY_STRONG_OPS = { LSHIFT, RSHIFT };
	static final Kind[] SIMPLE_TYPE = { KW_INT, KW_BOOLEAN, KW_STRING }; 
	static final Kind[] STATEMENT_FIRST = { IDENT, KW_PRINT, KW_WHILE, KW_IF, MOD, KW_RETURN };
	
	public void parse() throws SyntaxException {
		Program();
		match(EOF);
	}

	//<Program> ::= <ImportList> class IDENT <Block>
	private void Program() throws SyntaxException {
		ImportList();
		match(KW_CLASS);
		match(IDENT);
		Block();
	}

	//<ImportList> ∷=( import IDENT ( . IDENT )* ;) *
	private void ImportList() throws SyntaxException {
		while(isKind(KW_IMPORT)){
			match(KW_IMPORT);
			match(IDENT);
			while(t.kind == DOT){
				match(DOT);
				match(IDENT);
			}
			match(SEMICOLON);
		}
	}

	//<Block> ∷= { (<Declaration> ; | <Statement> ; )* }
	private void Block() throws SyntaxException {
		match(LCURLY);
		while(isKind(KW_DEF) || isKind(STATEMENT_FIRST) || isKind(SEMICOLON)){
			if(isKind(KW_DEF)){
				Declaration();
			}else if(isKind(STATEMENT_FIRST)){
				Statement();
				match(SEMICOLON);
			}else if(isKind(SEMICOLON)){
				match(SEMICOLON);
			}
		}		
		match(RCURLY);
	}
	
	//<Declaration> :≔ def <VarDec> | def <ClosureDec>
	private void Declaration() throws SyntaxException{
		match(KW_DEF);
		match(IDENT);
		if(isKind(COLON) || isKind(SEMICOLON)){
			VarDec();
		}else if(isKind(ASSIGN)){
			ClosureDec();
		}else{
			throw new SyntaxException(t, t.kind);
		}
	}
	
	//<VarDec> ∷= IDENT ( : <Type> | empty ) ;
	private void VarDec() throws SyntaxException{
		if(isKind(COLON)){
			match(COLON);
			Type();
		}
		match(SEMICOLON);		
	}
	
	//<Type> ::= <SimpleType> | <KeyValueType> | <ListType>
	private void Type() throws SyntaxException{
		if(isKind(SIMPLE_TYPE)){
			SimpleType();
		}else if(isKind(AT)){
			match(AT);
			if(isKind(AT)){
				KeyValueType();
			}else if(isKind(LSQUARE)){
				ListType();
			}else{
				throw new SyntaxException(t, t.kind);
			}
		}
	}
	
	//<SimpleType> ::= int | boolean | string
	private void SimpleType() throws SyntaxException{
		match(SIMPLE_TYPE);
	}
	
	//<KeyValueType> ::= @@ [ <SimpleType> : <Type>]
	private void KeyValueType() throws SyntaxException{
		match(AT);
		match(LSQUARE);
		SimpleType();
		match(COLON);
		Type();
		match(RSQUARE);
	}
	
	//<ListType> ∷= @ [ <Type> ]
	private void ListType() throws SyntaxException{
		match(LSQUARE);
		Type();
		match(RSQUARE);
	}
	
	//<ClosureDec> ∷= IDENT = <Closure>
	private void ClosureDec() throws SyntaxException{
		match(ASSIGN);
		Closure();
		match(SEMICOLON);
	}
	
	//<Closure> ∷= { <FormalArgList> - > <StatementList> }
	private void Closure() throws SyntaxException{
		match(LCURLY);
		FormalArgList();
		match(ARROW);
		if(isKind(STATEMENT_FIRST)){
			Statement();
			match(SEMICOLON);
			while(isKind(STATEMENT_FIRST)){
				Statement();
				match(SEMICOLON);
			}
		}		
		match(RCURLY);
	}
	
	//<FormalArgList> ∷= ϵ | <VarDec> (, <VarDec>)*
	private void FormalArgList() throws SyntaxException{
		if(isKind(KW_DEF)){
			match(KW_DEF);
			if(isKind(COLON) || isKind(SEMICOLON)){
				VarDec();
			}
			while(isKind(COMMA)){
				match(KW_DEF);
				VarDec();
			}
		}		
	}	
	
	/**
	<Statement> ::= <LValue> = <Expression>
	| print <Expression>
	| while (<Expression>) <Block>
	| while* ( <Expression> ) <Block>
	| while* (<RangeExpression>) < Block>
	| if (<Expression> ) <Block>
	| if (<Expression>) <Block> else <Block>
	| %<Expression>
	| return <Expression>
	| ϵ
	**/
	private void Statement() throws SyntaxException{
		switch(t.kind){
		case IDENT:
			LValue();
			match(ASSIGN);
			Expression();
			break;
		case KW_PRINT:
			match(KW_PRINT);
			Expression();
			break;
		case KW_WHILE:
			match(KW_WHILE);
			if(isKind(TIMES)){
				match(TIMES);
				match(LPAREN);
				Expression();
				if(isKind(RANGE)){
					RangeExpression();
				}
				match(RPAREN);
			}else if(isKind(LPAREN)){
				match(LPAREN);
				Expression();
				match(RPAREN);
			}else{
				throw new SyntaxException(t, t.kind);
			}
			Block();
			break;
		case KW_IF:
			match(KW_IF);
			match(LPAREN);
			Expression();
			match(RPAREN);
			Block();
			if(isKind(KW_ELSE)){
				match(KW_ELSE);
				Block();
			}
			break;
		case KW_RETURN:
			match(KW_RETURN);			
			Expression();
			break;
		case MOD:
			match(MOD);
			Expression();
			break;
		default:	
			throw new SyntaxException(t, t.kind);
		}
	}
	
	//<ClosureEvalExpression> ∷= IDENT (<ExpressionList>)
	private void ClosureEvalExpression() throws SyntaxException{
	//	match(IDENT);
		match(LPAREN);
		ExpressionList();
		match(RPAREN);
	}
	
	//<LValue> ::= IDENT | IDENT [ <Expression> ]
	private void LValue() throws SyntaxException{
		if(isKind(IDENT)){
			match(IDENT);
			if(isKind(LSQUARE)){
				match(LSQUARE);
				Expression();
				match(RSQUARE);
			}			
		}
	}
	
	//<List> ∷=@ [ <ExpressionList> ]
	private void List() throws SyntaxException{
		match(LSQUARE);
		ExpressionList();
		match(RSQUARE);
	}
	
	//<ExpressionList> ∷= ϵ | <Expression> ( , <Expression> )*
	private void ExpressionList() throws SyntaxException{
		Expression();
		while(isKind(COMMA)){
			match(COMMA);
			Expression();
		}
	}
		
	//<KeyValueExpression> ::= <Expression> : <Expression>
	private void KeyValueExpression() throws SyntaxException{
		Expression();
		match(COLON);
		Expression();
	}
			
	//<KeyValueList> ∷= ϵ | <KeyValueExpression> ( , <KeyValueExpression> ) *
	private void KeyValueList() throws SyntaxException{
		KeyValueExpression();
		while(isKind(COMMA)){
			match(COMMA);
			KeyValueExpression();
		}
	}
	
	//<MapList> ∷= @@[ <KeyValueList> ]
	private void MapList() throws SyntaxException{
		match(AT);
		match(LSQUARE);
		KeyValueList();
		match(RSQUARE);
	}
	
	//<RangeExpression> ∷ <Expression> .. <Expression>
	private void RangeExpression() throws SyntaxException{
		match(RANGE);
		Expression();
	}	
	
	//<Expression> ::= <Term> (<RelOp> <Term>)*
	private void Expression() throws SyntaxException{
		Term();
		while(isKind(REL_OPS)){
			match(REL_OPS);
			Term();
		}
	}
	
	//<Term> ::= <Elem> (<WeakOp> <Elem>)*
	private void Term() throws SyntaxException{
		Element();
		while(isKind(WEAK_OPS)){
			match(WEAK_OPS);
			Element();
		}
	}
	
	//<Elem> ::= <Thing> ( <StrongOp> <Thing>)*
	private void Element() throws SyntaxException{
		Thing();
		while(isKind(STRONG_OPS)){
			match(STRONG_OPS);
			Thing();
		}
	}

	//<Thing> ∷= <Factor> ( <VeryStrongOp> <Factor )*
	private void Thing() throws SyntaxException{
		Factor();
		while(isKind(VERY_STRONG_OPS)){
			match(VERY_STRONG_OPS);
			Factor();
		}
	}
	
	/**
	 IDENT | 
	 IDENT [ <Expression> ] | 
	 INT_LIT | true | false | STRING_LIT | 
	 ( <Expression> ) | 
	 ! <Factor> | -<Factor> | 
	 size(<Expression> ) | 
	 key(<Expression ) | 
	 value(<Expression >) | 
	 <ClosureEvalExpression> | <Closure> | 
	 <List> | <MapList>
	 **/
	private void Factor() throws SyntaxException{
		switch(t.kind){
		case IDENT:
			match(IDENT);
			if(isKind(LSQUARE)){
				match(LSQUARE);
				Expression();
				match(RSQUARE);
			}else if(isKind(LPAREN)){
				ClosureEvalExpression();
			}
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
			match(LPAREN);
			Expression();
			match(RPAREN);
			break;
		case NOT:
			match(NOT);
			Factor();
			break;
		case MINUS:
			match(MINUS);
			Factor();
			break;
		case KW_SIZE:
			match(KW_SIZE);
			match(LPAREN);
			Expression();
			match(RPAREN);
			break;
		case KW_KEY:
			match(KW_KEY);
			match(LPAREN);
			Expression();
			match(RPAREN);
			break;
		case KW_VALUE:
			match(KW_VALUE);
			match(LPAREN);
			Expression();
			match(RPAREN);
			break;
		case LCURLY:
			Closure();
			break;
		case AT:
			match(AT);
			if(isKind(LSQUARE)){
				List();
				break;
			}else if(isKind(AT)){
				MapList();
				break;
			}			
		default:
			throw new SyntaxException(t, t.kind);		
		}
	}
		
	public static void main(String[] args) throws SyntaxException {
		TokenStream stream = new TokenStream("class A  { \n x = @[a,b,c]; \n y = @[d,e,f]+x; \n");
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		SimpleParser parser = new SimpleParser(stream);
		parser.parse();		
	}

}
