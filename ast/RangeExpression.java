package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class RangeExpression extends ASTNode {
	Expression lower;
	Expression upper;

	public RangeExpression(Token firstToken, Expression lower, Expression upper) {
		super(firstToken);
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitRangeExpression(this,arg);
	}

}
