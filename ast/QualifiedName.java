package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;



public class QualifiedName extends ASTNode{
	
	String name;

	public QualifiedName(Token firstToken, String name) {
		super(firstToken);
		this.name = name;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitQualifiedName(this, arg);
	}
	
}
