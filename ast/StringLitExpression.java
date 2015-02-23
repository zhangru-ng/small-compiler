package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class StringLitExpression extends Expression {
	String value;
	
	public StringLitExpression(Token firstToken, String value) {
		super(firstToken);
		this.value = value;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStringLitExpression(this,arg);
	}

}
