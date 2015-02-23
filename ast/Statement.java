package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public abstract class Statement extends BlockElem {

	Statement(Token firstToken) {
		super(firstToken);
	}

}
