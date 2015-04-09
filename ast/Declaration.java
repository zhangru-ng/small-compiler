package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;


public abstract class Declaration extends BlockElem {
	
	public boolean globalScope;

	Declaration(Token firstToken) {
		super(firstToken);
	}
	
}
