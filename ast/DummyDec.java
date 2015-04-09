package cop5555sp15.ast;


public class DummyDec extends Declaration {

	String ident;
	String dummyType;
	public DummyDec(String ident, String comment) {
		super(null);
		this.ident=ident;
		this.dummyType=comment;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return null;
	}
	
	@Override
	public String toString(){
		return  dummyType;
	}

}
