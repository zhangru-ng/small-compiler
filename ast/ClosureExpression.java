package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class ClosureExpression extends Expression {
	Closure closure;
	

	public ClosureExpression(Token firstToken, Closure closure) {
		super(firstToken);
		this.closure = closure;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitClosureExpression(this,arg);
	}

}
