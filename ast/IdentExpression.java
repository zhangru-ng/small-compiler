package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class IdentExpression extends Expression {
	Token identToken;
	

	public IdentExpression(Token firstToken, Token identToken) {
		super(firstToken);
		this.identToken = identToken;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIdentExpression(this,arg);
	}

}
