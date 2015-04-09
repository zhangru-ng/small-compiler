package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class SimpleType extends Type {
	Token type;
	

	public SimpleType(Token firstToken, Token type) {
		super(firstToken);
		this.type = type;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSimpleType(this,arg);
	}


	@Override
	String getJVMType() {
		if(type.getText().equals("int")) return "I";
		else if (type.getText().equals("boolean")) return "Z";
		else return "Ljava/lang/String;";
	}

}
