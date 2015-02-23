package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class WhileRangeStatement extends Statement {
	RangeExpression rangeExpression;
	Block block;

	public WhileRangeStatement(Token firstToken,
			RangeExpression rangeExpression, Block block) {
		super(firstToken);
		this.rangeExpression = rangeExpression;
		this.block = block;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitWhileRangeStatement(this,arg);
	}

}
