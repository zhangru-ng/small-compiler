package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class VarDec extends Declaration {
	Token identToken;
	Type type;
	

	public VarDec(Token firstToken, Token identToken, Type type) {
		super(firstToken);
		this.identToken = identToken;
		this.type = type;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitVarDec(this,arg);
	}

}
