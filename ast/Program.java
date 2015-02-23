package cop5555sp15.ast;

import java.util.List;

import cop5555sp15.TokenStream.Token;

public class Program extends ASTNode {
	List<QualifiedName> imports;
	String name;
	Block block;

	public Program(Token firstToken, List<QualifiedName> imports, String name, Block block) {
		super(firstToken);
		this.imports = imports;
		this.name = name;
		this.block = block;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitProgram(this,arg);
	}

	
}
