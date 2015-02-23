package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class KeyValueExpression extends Expression {
	Expression key;
	Expression value;
	

	public KeyValueExpression(Token firstToken, Expression key, Expression value) {
		super(firstToken);
		this.key = key;
		this.value = value;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitKeyValueExpression(this,arg);
	}

}
