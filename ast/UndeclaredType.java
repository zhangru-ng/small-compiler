package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class UndeclaredType extends Type {

	public UndeclaredType(Token firstToken) {
		super(firstToken);
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitUndeclaredType(this, arg);
	}

	@Override
	String getJVMType() {
		// TODO Auto-generated method stub
		return null;
	}


	
	

}
