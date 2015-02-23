package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class KeyExpression extends Expression {
	Expression expression;
	

	public KeyExpression(Token firstToken, Expression expression) {
		super(firstToken);
		this.expression = expression;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitKeyExpression(this,arg);
	}

}
