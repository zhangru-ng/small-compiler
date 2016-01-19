package cop5555sp15;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import cop5555sp15.ast.ASTNode;
import cop5555sp15.ast.AssignmentStatement;
import cop5555sp15.ast.BlockElem;
import cop5555sp15.ast.Expression;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.Statement;
import cop5555sp15.ast.TypeCheckVisitor;
import cop5555sp15.symbolTable.SymbolTable;
import cop5555sp15.ast.TypeCheckVisitor.TypeCheckException;

public class TestTypeChecker {

	private ASTNode parseCorrectInput(String input) {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		System.out.println();
		ASTNode ast = parser.parse();
		if (ast == null) {
			System.out.println("errors " + parser.getErrors());
		}
		assertNotNull(ast);
		return ast;
	}

	private void typeCheckCorrectAST(ASTNode ast) throws Exception {
		SymbolTable symbolTable = new SymbolTable();
		TypeCheckVisitor v = new TypeCheckVisitor(symbolTable);
		try {
			ast.visit(v, null);
		} catch (TypeCheckException e) {
			System.out.println(e.getMessage());
			fail("no errors expected");
		}
	}

	private void typeCheckIncorrectAST(ASTNode ast) throws Exception {
		SymbolTable symbolTable = new SymbolTable();
		TypeCheckVisitor v = new TypeCheckVisitor(symbolTable);
		try {
			ast.visit(v, null);
			fail("expected error");
		} catch (TypeCheckException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void emptyProgram() throws Exception {
		System.out.println("***********smallest");
		String input = "class A { } ";
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}

	@Test
	public void block0() throws Exception {
		System.out.println("***********block0");
		String input = "class A { def B:int; def C:boolean; } ";
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}

	@Test
	// B defined twice in scope
	public void block1() throws Exception {
		System.out.println("***********block1");
		String input = "class A { def B:int; def B:boolean; } ";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}

	@Test
	public void block2() throws Exception {
		System.out.println("***********block2");
		String input = "class A { def B:int; def C:boolean; if (C) {def B: boolean;}; }";
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}

	@Test
	// C has wrong type
	public void ifStatement1() throws Exception {
		System.out.println("***********ifStatement1");
		String input = "class A { def B:int; def C:string; if (C) {def B: boolean;}; }";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}

	@Test
	public void ifElse1() throws Exception {
		System.out.println("***********ifElse1");
		String input = "class A { def B:int; def C:boolean; if (C) {def B: boolean;} else {def B: boolean;}; }";
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}

	@Test
	// C has wrong type
	public void ifElse2() throws Exception {
		System.out.println("***********ifElse2");
		String input = "class A { def B:int; def C:string; if (C) {def B: boolean;} else {def B: boolean;}; }";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}

	@Test
	public void while1() throws Exception {
		System.out.println("***********while1");
		String input = "class A { def B:int; def C:boolean; while (C) {def B: boolean;} ; }";
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}

	@Test
	// C has wrong type
	public void while2() throws Exception {
		System.out.println("***********while2");
		String input = "class A { def B:int; def C:int; while (C) {def B: boolean;} ; }";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}

	@Test
	// C not declared
	public void while3() throws Exception {
		System.out.println("***********while3");
		String input = "class A { def B:int;  while (C) {def B: boolean;} ; }";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}
	
	@Test
	public void simpleAssignments1() throws Exception{
		System.out.println("***********simpleAssignments1");
		String input = "class A { def B:int;  def C:boolean; def D:string;  B = 5; C = true; C = false; D = \"hello\"; }";	
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}
	
	@Test  //assignment to B has wrong type
	public void simpleAssignments2() throws Exception{
		System.out.println("***********simpleAssignments2");
		String input = "class A { \n  def B:boolean;\n  def C:boolean;\n  def D:string;\n  B = 5;\n  C = true;\n  C = false;\n  D = \"hello\";\n}";	
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}
	
	@Test
	// nested scopes
	public void nestedScopes1() throws Exception {
		System.out.println("***********nestedScopes1");
		String input = "class A {\n  def B:int;\n  def C: boolean;\n  while (C) {\n    def B: boolean;\n   while (B) {\n    def C: int;\n    C = 42;\n   };\n };\n}";
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}
	
	@Test
	// nested scopes with illegal assignment
	public void nestedScopes2() throws Exception {
		System.out.println("***********nestedScopes2");
		String input = "class A {\n  def B:int;\n  def C: boolean;\n  while (C) {\n    def B: boolean;\n   while (B) {\n    \n    C = 42;\n   };\n };\n}";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}
	
	@Test
	// unary expression with ! and -
	public void unaryExpression1() throws Exception {
		System.out.println("***********unaryExpression1");
		String input = "class A {\n  def B:int;\n  def C: boolean;\n  while (C) {\n    def B: boolean;\n   while (B) {\n    def C: int;\n    C = -42;\n   B = !B;\n  };\n };\n}";
		System.out.println(input);
		typeCheckCorrectAST(parseCorrectInput(input));
	}
	
	@Test
	// incorrect unary expression with ! 
	public void unaryExpression2() throws Exception {
		System.out.println("***********unaryExpression2");
		String input = "class A {\n  def B:int;\n  def C: boolean;\n  while (C) {\n    def B: boolean;\n   while (B) {\n    def C: int;\n    C = !42;\n   B = !B;\n  };\n };\n}";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}
	
	@Test
	// incorrect unary expression with - 
	public void unaryExpression3() throws Exception {
		System.out.println("***********unaryExpression3");
		String input = "class A {\n  def B:int;\n  def C: boolean;\n  while (C) {\n    def B: boolean;\n   while (B) {\n    def C: int;\n    C = -42;\n   B = -B;\n  };\n };\n}";
		System.out.println(input);
		typeCheckIncorrectAST(parseCorrectInput(input));
	}
	
	@Test
	// binary expressions
	public void binaryExpression1() throws Exception{
		System.out.println("***********binaryExpression1");
		String input = "class A {\n  def B:int;\n  def C: int;\n  B = B + C;\n  B = B * C;\n  C = C / B;\n  C = B - C; \n}";
		System.out.println(input);
		Program program = (Program) parseCorrectInput(input);
		typeCheckCorrectAST(program);	
		String t0,t1,t2,t3;
		List<BlockElem> elems = program.block.elems;
		Statement s0 = (Statement) elems.get(3);
		Expression e0 = ((AssignmentStatement)s0).expression;
		t0 = e0.getType();
		assertEquals("I",t0);
		t1 = ((AssignmentStatement) elems.get(3)).expression.getType();
		assertEquals("I",t1);
		t2 = ((AssignmentStatement) elems.get(4)).expression.getType();
		assertEquals("I",t2);	
		t3 = ((AssignmentStatement) elems.get(5)).expression.getType();
		assertEquals("I",t3);
	}



@Test
// incorrect binary expressions
public void binaryExpressionFail() throws Exception{
	System.out.println("***********binaryExpressionFail");
	String input = "class A {\n  def B:int;\n  def C: boolean;\n  B = B + C;\n  B = B * C;\n  C = C / B;\n  C = B - C; \n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	typeCheckIncorrectAST(program);	
}


@Test
// normal while statement
public void whileStatement1() throws Exception{
	System.out.println("***********whileStatement1");
	String input = "class A {\n  def B:int;\n  def C: boolean;\n  C = true;\n while(C){ def s: string;\n     s = \"hello\";\n    print s;\n   C = false;\n  }; \n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);	
}

@Test
//whileRangeStatement1
public void whileRangeStatement1() throws Exception{
	System.out.println("***********whileRangeStatement1");
	String input = "class A {\n  def B:int;\n  def C: boolean;\n  C = true;\n while*(2..7){ \n   def s: string;\n     s = \"hello\";\n    print s;\n   C = false;\n  }; \n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);	
}

@Test
//whileRangeStatement1
public void whileStarStatement1() throws Exception{
	System.out.println("***********whileStarStatement1");
	String input = "class A {\n  def B:int;\n  def C: boolean;\n  C = true;\n while*(2..7){ \n   def s: string;\n     s = \"hello\";\n    print s;\n   C = false;\n  }; \n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);	
}
}