package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public abstract class LValue extends ASTNode {
	
	String type;
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public LValue(Token firstToken) {
		super(firstToken);
	}


}
