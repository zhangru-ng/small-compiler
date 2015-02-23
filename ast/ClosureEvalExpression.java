package cop5555sp15.ast;

import java.util.List;

import cop5555sp15.TokenStream.Token;

public class ClosureEvalExpression extends Expression {
	Token identToken;
	List<Expression> expressionList;
	
	public ClosureEvalExpression(Token firstToken, Token identToken,
			List<Expression> expressionList) {
		super(firstToken);
		this.identToken = identToken;
		this.expressionList = expressionList;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitClosureEvalExpression(this,arg);
	}

}
