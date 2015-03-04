package cop5555sp15;

import java.util.ArrayList;
import java.util.List;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import static cop5555sp15.TokenStream.Kind.*;
import cop5555sp15.ast.*;


public class Parser {

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

	Parser(TokenStream tokens) {
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
	static final Kind[] EXPRESSION_FIRST = { IDENT, INT_LIT, BL_TRUE, BL_FALSE, STRING_LIT, NL_NULL, LPAREN, NOT, MINUS, KW_SIZE, KW_KEY, KW_VALUE, LCURLY, AT };
	
	List<SyntaxException> exceptionList = new ArrayList<SyntaxException>();
	
	public List<SyntaxException> getExceptionList(){
		return exceptionList;
	}
	
	public Program parse() throws SyntaxException {
		Program p = null;
		try{
			p = program();
			if(p != null){
				match(EOF);
			}
		}catch(SyntaxException e){
			exceptionList.add(e);			
		}
		if(exceptionList.isEmpty()){
			return p;
		}else{
			return null;
		}		
	}

	//<Program> ::= <ImportList> class IDENT <Block>
	private Program program() throws SyntaxException {
		Token first = t;
		List<QualifiedName> imports = ImportList();
		match(KW_CLASS);
		String name = t.getText();
		match(IDENT);
		Block block = block();
		return new Program(first, imports, name, block);
	}

	//<ImportList> ::=( import IDENT ( . IDENT )* ;) *
	private List<QualifiedName> ImportList() throws SyntaxException {
		List<QualifiedName> imports = new ArrayList<QualifiedName>();
		QualifiedName qname = null;
		Token first = t;
		Token name = null;
		while(isKind(KW_IMPORT)){
			String qualifiedname = null;
			consume();
			name = t;
			match(IDENT);
			qualifiedname = name.getText();
			while(isKind(DOT)){
				consume();
				name = t;
				match(IDENT);	
				//qualified name contain all the idents with the "." replaced by "\"
				qualifiedname += "\\"+name.getText();
			}
			match(SEMICOLON);
			qname = new QualifiedName(first, qualifiedname);
			imports.add(qname);
		}
		return imports;
	}

	//<Block> ::= { (<Declaration> ; | <Statement> ; )* }
	private Block block() throws SyntaxException {
		List<BlockElem> elems = new ArrayList<BlockElem>();
		BlockElem blockelem = null;
		Token first = t;
		match(LCURLY);
		while(isKind(KW_DEF) || isKind(STATEMENT_FIRST) || isKind(SEMICOLON)){
			if(isKind(KW_DEF)){
				blockelem = declaration();
				match(SEMICOLON);
			}else if(isKind(STATEMENT_FIRST)){
				blockelem = statement();
				match(SEMICOLON);
			}else if(isKind(SEMICOLON)){
				//statement may be empty
				blockelem = null;
				match(SEMICOLON);
			}
			elems.add(blockelem);
		}		
		match(RCURLY);
		return new Block(first, elems);
	}
	
	//<Declaration> :鈮� def <VarDec> | def <ClosureDec>
	private BlockElem declaration() throws SyntaxException{
		Declaration d = null;
		Token first = t;
		match(KW_DEF);
		Token identToken = t;
		match(IDENT);
		if(isKind(COLON) || isKind(SEMICOLON)){
			//<VarDec> ::= IDENT ( : <Type> | empty ) ;
			if(isKind(COLON)){
				consume();
				d = new VarDec(first, identToken, type());
			}else{
				d = new VarDec(first, identToken, null);
			}
		}else if(isKind(ASSIGN)){
			consume();
			//<ClosureDec> ::= IDENT = <Closure>
			d = new ClosureDec(first, identToken, closure());
		}else{
			throw new SyntaxException(t, t.kind);
		}
		return d;
	}
	
	//<VarDec> ::= IDENT ( : <Type> | empty ) ;
	private VarDec varDec() throws SyntaxException{
		VarDec v = null;
		Token first = t;
		Token identToken = t;
		match(IDENT);
		if(isKind(COLON)){
			consume();
			v = new VarDec(first, identToken, type());
		}else{
			v = new VarDec(first, identToken, null);
		}
		return v;
	}
		
	//<Type> ::= <SimpleType> | <KeyValueType> | <ListType>
	private Type type() throws SyntaxException{
		Token first = t;
		Type type = null;
		if(isKind(SIMPLE_TYPE)){
			Token typeToken = t;
			consume();
			//<SimpleType> ::= int | boolean | string
			type = new SimpleType(first, typeToken);
		}else if(isKind(AT)){
			consume();
			if(isKind(AT)){
				consume();
				match(LSQUARE);
				SimpleType st = new SimpleType(t, t);		
				match(SIMPLE_TYPE);					
				match(COLON);
				//<KeyValueType> ::= @@ [ <SimpleType> : <Type>]
				type = new KeyValueType(first, st, type());
				match(RSQUARE);
			}else if(isKind(LSQUARE)){
				consume();
				//<ListType> ::= @ [ <Type> ]
				type = new ListType(first, type());
				match(RSQUARE);
			}else{
				throw new SyntaxException(t, t.kind);
			}
		}else{
			throw new SyntaxException(t, t.kind);
		}
		return type;
	}	
	
	//<Closure> ::= { <FormalArgList> - > <StatementList> }
	private Closure closure() throws SyntaxException{
		Token first = t;
		Statement s = null;
		List<VarDec> formalArgs = new ArrayList<VarDec>();
		List<Statement> statements = new ArrayList<Statement>();
		match(LCURLY);
		formalArgs = formalArgList();
		match(ARROW);
		if(isKind(STATEMENT_FIRST)){
			s = statement();
			statements.add(s);
			match(SEMICOLON);
			while(isKind(STATEMENT_FIRST)){
				s = statement();
				statements.add(s);
				match(SEMICOLON);
			}
		}		
		match(RCURLY);		
		return new Closure(first, formalArgs, statements); 
	}
	
	//<FormalArgList> ::= empty | <VarDec> (, <VarDec>)*
	private List<VarDec> formalArgList() throws SyntaxException{
		List<VarDec> falist = new ArrayList<VarDec>();
		VarDec v = null;
		if(isKind(IDENT)){
			v = varDec();
			falist.add(v);
			while(isKind(COMMA)){
				consume();
				v = varDec();
				falist.add(v);
			}
		}	
		return falist;
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
	| empty
	**/
	private Statement statement() throws SyntaxException{
		Statement s = null;
		Token first = t;
		Expression e = null;
		Block block = null;
		switch(t.kind){
		case IDENT:
			LValue lvalue = lValue();
			match(ASSIGN);
			s = new AssignmentStatement(first, lvalue, expression());
			break;
		case KW_PRINT:
			consume();
			s = new PrintStatement(first, expression());
			break;
		case KW_WHILE:
			consume();
			if(isKind(TIMES)){
				consume();
				match(LPAREN);
				e = expression();
				//<RangeExpression> :: <Expression> .. <Expression>
				if(isKind(RANGE)){
					consume();
					RangeExpression re= new RangeExpression(first, e, expression());
					match(RPAREN);
					s = new WhileRangeStatement(first, re, block());
					break;
				}
				match(RPAREN);
				s = new WhileStarStatement(first, e, block());
			}else if(isKind(LPAREN)){
				consume();
				e = expression();
				match(RPAREN);
				s = new WhileStatement(first, e, block()); 
			}else{
				throw new SyntaxException(t, t.kind);
			}			
			break;
		case KW_IF:
			consume();
			match(LPAREN);
			e = expression();
			match(RPAREN);
			block = block();
			if(isKind(KW_ELSE)){
				consume();
				s = new IfElseStatement(first, e, block, block());
				break;
			}
			s = new IfStatement(first, e, block);
			break;
		case KW_RETURN:
			consume();
			s = new ReturnStatement(first, expression());
			break;
		case MOD:
			consume();
			s = new ExpressionStatement(first, expression());
			break;
		default:	
			throw new SyntaxException(t, t.kind);
		}
		return s;
	}
	
	//<LValue> ::= IDENT | IDENT [ <Expression> ]
	private LValue lValue() throws SyntaxException{
		LValue l = null;
		Token first = t;
		if(isKind(IDENT)){
			Token identToken = t;
			consume();
			if(isKind(LSQUARE)){
				consume();
				l = new ExpressionLValue(first, identToken, expression());
				match(RSQUARE);
			}else{
				l = new IdentLValue(first, identToken);
			}
		}
		return l;
	}
	
	//<ExpressionList> ::= empty | <Expression> ( , <Expression> )*
	private List<Expression> expressionList() throws SyntaxException{
		List<Expression> expressions= new ArrayList<Expression>();
		if(isKind(EXPRESSION_FIRST)){
			expressions.add(expression());
			while(isKind(COMMA)){
				match(COMMA);
				expressions.add(expression());
			}
		}
		return expressions;
	}
		
	//<KeyValueExpression> ::= <Expression> : <Expression>
	private KeyValueExpression keyValueExpression() throws SyntaxException{
		Expression key = null;
		Expression value = null;
		Token first = t;
		key = expression();
		match(COLON);
		value = expression();
		return new KeyValueExpression(first, key, value);
	}
	
	//<KeyValueList> ::= empty | <KeyValueExpression> ( , <KeyValueExpression> ) *
	private List<KeyValueExpression> keyValueList() throws SyntaxException{
		List<KeyValueExpression> kvexpressions= new ArrayList<KeyValueExpression>();
		if(isKind(EXPRESSION_FIRST)){
			kvexpressions.add(keyValueExpression());
			while(isKind(COMMA)){
				match(COMMA);
				kvexpressions.add(keyValueExpression());
			}
		}
		return kvexpressions;
	}
	
	//<Expression> ::= <Term> (<RelOp> <Term>)*
	private Expression expression() throws SyntaxException{
		Expression e1 = null;
		Expression e2 = null;
		Token first = t;
		e1 = term();
		while(isKind(REL_OPS)){
			Token op = t;
			match(REL_OPS);
			e2 = term();
			e1 = new BinaryExpression(first, e1, op, e2);
		}
		return e1;		
	}
	
	//<Term> ::= <Elem> (<WeakOp> <Elem>)*
	private Expression term() throws SyntaxException{
		Expression e1 = null;
		Expression e2 = null;
		Token first = t;
		e1 = element();
		while(isKind(WEAK_OPS)){
			Token op = t;
			match(WEAK_OPS);
			e2 = element();
			e1 = new BinaryExpression(first, e1, op, e2);
		}
		return e1;
	}
	
	//<Elem> ::= <Thing> ( <StrongOp> <Thing>)*
	private Expression element() throws SyntaxException{
		Expression e1 = null;
		Expression e2 = null;
		Token first = t;
		e1 = thing();		
		while(isKind(STRONG_OPS)){
			Token op = t;
			match(STRONG_OPS);
			e2 = thing();
			e1 = new BinaryExpression(first, e1, op, e2);
		}
		return e1;
	}

	//<Thing> ::= <Factor> ( <VeryStrongOp> <Factor )*
	private Expression thing() throws SyntaxException{
		Expression e1 = null;
		Expression e2 = null;
		Token first = t;
		e1 = factor();		
		while(isKind(VERY_STRONG_OPS)){
			Token op = t;
			match(VERY_STRONG_OPS);
			e2 = factor();
			e1 = new BinaryExpression(first, e1, op, e2);
		}
		return e1;
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
	private Expression factor() throws SyntaxException{
		Expression e = null;
		Token first = t;
		Token op = null;
		switch(t.kind){
		case IDENT:
			Token identToken = t;
			match(IDENT);
			switch(t.kind){
			case LSQUARE:
				consume();
				e = new ListOrMapElemExpression(first, identToken, expression());
				match(RSQUARE);
				break;
			case LPAREN:
				consume();
				//<ClosureEvalExpression> ::= IDENT (<ExpressionList>)
				e = new ClosureEvalExpression(first, identToken, expressionList());
				match(RPAREN);
				break;
			default:
				e = new IdentExpression(first, identToken);
			}
			break;
		case INT_LIT:
			e = new IntLitExpression(t, t.getIntVal());
			consume();
			break;
		case BL_TRUE:						
			e = new BooleanLitExpression(t, t.getBooleanVal());
			consume();
			break;
		case BL_FALSE:
			e = new BooleanLitExpression(t, t.getBooleanVal());
			consume();
			break;
		case STRING_LIT:
			e = new StringLitExpression(t, t.getText());
			consume();
			break;
		case LPAREN:
			consume();
			e = expression();
			match(RPAREN);
			break;
		case NOT:
			op = t;
			consume();
			e = new UnaryExpression(first, op,	expression());
			break;
		case MINUS:
			op = t;
			consume();
			e = new UnaryExpression(first, op,	expression());
			break;
		case KW_SIZE:
			consume();
			match(LPAREN);
			e = new SizeExpression(first, expression());			
			match(RPAREN);
			break;
		case KW_KEY:
			consume();
			match(LPAREN);
			e = new KeyExpression(first, expression());			
			match(RPAREN);
			break;
		case KW_VALUE:
			consume();
			match(LPAREN);
			e = new ValueExpression(first, expression());			
			match(RPAREN);
			break;
		case LCURLY:
			e = new ClosureExpression(first, closure());
			break;
		case AT:
			consume();
			if(isKind(LSQUARE)){
				consume();
				//<List> ::=@ [ <ExpressionList> ]
				e = new ListExpression(first, expressionList());
				match(RSQUARE);
				break;
			}else if(isKind(AT)){
				consume();
				match(LSQUARE);
				//<MapList> ::= @@[ <KeyValueList> ]
				e = new MapListExpression(first, keyValueList());
				match(RSQUARE);
				break;
			}else{
				throw new SyntaxException(t, t.kind);	
			}			
		default:
			throw new SyntaxException(t, t.kind);		
		}
		return e;
	}
	
	public static void main(String[] args) throws SyntaxException {
		TokenStream stream = new TokenStream("import X; class A { }    ");
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		System.out.println();
		ASTNode ast = parser.parse();
		System.out.println(ast);
	}

}
