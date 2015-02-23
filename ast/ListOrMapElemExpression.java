package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class ListOrMapElemExpression extends Expression {
Token identToken;
Expression expression;


	public ListOrMapElemExpression(Token firstToken, Token identToken,
		Expression expression) {
	super(firstToken);
	this.identToken = identToken;
	this.expression = expression;
}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitListOrMapElemExpression(this,arg);
	}

}
