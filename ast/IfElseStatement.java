package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class IfElseStatement extends Statement {
	Expression expression;
	Block ifBlock;
	Block elseBlock;
	
	public IfElseStatement(Token firstToken, Expression expression, Block ifBlock, Block elseBlock) {
		super(firstToken);
		this.expression = expression;
		this.ifBlock = ifBlock;
		this.elseBlock = elseBlock;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIfElseStatement(this, arg);
	}

}
