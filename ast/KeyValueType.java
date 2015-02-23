package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class KeyValueType extends Type {
	SimpleType keyType;
	Type valueType;
	

	public KeyValueType(Token firstToken, SimpleType keyType, Type valueType) {
		super(firstToken);
		this.keyType = keyType;
		this.valueType = valueType;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitKeyValueType(this,arg);
	}

}
