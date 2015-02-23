package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class ListType extends Type {
	Type type;
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitListType(this,arg);
	}
	public ListType(Token firstToken, Type type) {
		super(firstToken);
		this.type = type;
	}

}
