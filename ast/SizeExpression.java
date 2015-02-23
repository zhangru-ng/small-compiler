package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class SizeExpression extends Expression {
	Expression expression;
	
	public SizeExpression(Token firstToken, Expression expression) {
		super(firstToken);
		this.expression = expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSizeExpression(this,arg);
	}

}
