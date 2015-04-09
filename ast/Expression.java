package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public abstract class Expression extends ASTNode {
	
	String expressionType;

	public String getType() {
		return expressionType;
	}

	public void setType(String type) {
		this.expressionType = type;
	}

	Expression(Token firstToken) {
		super(firstToken);
	}

}
