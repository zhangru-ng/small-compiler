package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;
import cop5555sp15.ast.ASTVisitor;


public abstract class ASTNode {
	
	ASTNode(Token firstToken){
		this.firstToken=firstToken;
	}
	public Token firstToken;

	public abstract Object visit(ASTVisitor v, Object arg) throws Exception;

	/* toString method uses a PrintVisitor to print the  AST */
	@Override
	public String toString() {
		ToStringVisitor v = new ToStringVisitor();
		try {
			visit(v, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v.getString();
	}

}
