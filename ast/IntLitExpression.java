package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class IntLitExpression extends Expression {
	int value;
	
	

	public IntLitExpression(Token firstToken, int value) {
		super(firstToken);
		this.value = value;
	}



	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIntLitExpression(this,arg);
	}

}
