package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class ClosureDec extends Declaration {
	Token identToken;
	Closure closure;

	public ClosureDec(Token firstToken, Token identToken, Closure closure) {
		super(firstToken);
		this.identToken = identToken;
		this.closure = closure;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitClosureDec(this,arg);
	}

}
