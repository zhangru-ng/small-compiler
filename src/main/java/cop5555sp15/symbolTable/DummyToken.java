package cop5555sp15.symbolTable;

import cop5555sp15.TokenStream;
import static cop5555sp15.TokenStream.Kind.IDENT;
import cop5555sp15.TokenStream.Token;

public class DummyToken extends Token {

	static TokenStream stream = new TokenStream("");
	
	String text;
	
	public DummyToken(String id) {
		stream.super(IDENT, 0, 0, 0);
		this.text = id;
	}
	
	@Override
	public String getText(){
		return text;
	}

}
