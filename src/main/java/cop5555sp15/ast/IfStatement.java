package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class IfStatement extends Statement {
	Expression expression;
	Block block;
	
	public IfStatement(Token firstToken, Expression expression, Block block) {
		super(firstToken);
		this.expression = expression;
		this.block = block;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIfStatement(this,arg);	}
}


	

