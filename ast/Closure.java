package cop5555sp15.ast;

import java.util.List;

import cop5555sp15.TokenStream.Token;
import cop5555sp15.ast.ASTNode;
import cop5555sp15.ast.ASTVisitor;

public class Closure extends ASTNode {
	List<VarDec>formalArgList;
	List<Statement> statementList;

	public Closure(Token firstToken, List<VarDec> formalArgList,
			List<Statement> statementList) {
		super(firstToken);
		this.formalArgList = formalArgList;
		this.statementList = statementList;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitClosure(this, arg);
	}

}
