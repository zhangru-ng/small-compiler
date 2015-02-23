package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class IdentLValue extends LValue {
	Token identToken;
	

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIdentLValue(this,arg);
	}


	public IdentLValue(Token firstToken, Token identToken) {
		super(firstToken);
		this.identToken = identToken;
	}

}
