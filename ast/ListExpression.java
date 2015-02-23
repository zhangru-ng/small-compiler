package cop5555sp15.ast;

import java.util.List;

import cop5555sp15.TokenStream.Token;

public class ListExpression extends Expression {
	List<Expression> expressionList;

	public ListExpression(Token firstToken, List<Expression> expressionList) {
		super(firstToken);
		this.expressionList = expressionList;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitListExpression(this,arg);
	}

}
