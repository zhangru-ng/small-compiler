package cop5555sp15.ast;

import java.util.List;

import cop5555sp15.TokenStream.Token;

public class Block extends ASTNode {
	List<BlockElem> elems;

	public Block(Token firstToken, List<BlockElem> elems) {
		super(firstToken);
		this.elems = elems;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitBlock(this, arg);
	}
}
