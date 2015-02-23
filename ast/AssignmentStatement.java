package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class AssignmentStatement extends Statement {
	
	LValue lvalue;
	Expression expression;

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitAssignmentStatement(this,arg);
	}

	public AssignmentStatement(Token firstToken, LValue lvalue,
			Expression expression) {
		super(firstToken);
		this.lvalue = lvalue;
		this.expression = expression;
	}

}
