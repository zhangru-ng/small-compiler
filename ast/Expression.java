package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public abstract class Expression extends ASTNode {

	Expression(Token firstToken) {
		super(firstToken);
	}

}
