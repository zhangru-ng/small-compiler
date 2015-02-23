package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class UnaryExpression extends Expression {
	Token op;
	Expression expression;
	

	public UnaryExpression(Token firstToken, Token op,
			Expression expression) {
		super(firstToken);
		this.op = op;
		this.expression = expression;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitUnaryExpression(this,arg);
	}

}
