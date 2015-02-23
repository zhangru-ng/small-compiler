package cop5555sp15.ast;

import java.util.List;

import cop5555sp15.TokenStream.Token;

public class MapListExpression extends Expression {
	List<KeyValueExpression> mapList;
	
	public MapListExpression(Token firstToken, List<KeyValueExpression> mapList) {
		super(firstToken);
		this.mapList = mapList;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitMapListExpression(this,arg);
	}

}
