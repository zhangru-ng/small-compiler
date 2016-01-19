package cop5555sp15;

import static org.junit.Assert.*;

import org.junit.Test;

import cop5555sp15.Parser.SyntaxException;
import cop5555sp15.ast.ASTNode;

public class RayTestAST {
	private ASTNode parseInput(String input) throws SyntaxException {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		System.out.println();
		ASTNode ast = parser.parse();
		assertNotNull(ast);
		return ast;
	}	
	
	@Test
	public void closureEval() throws SyntaxException {
		System.out.println("closureEval");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    AssignmentStatement\n"
						+"      ExpressionLValue\n"
						+"        x\n"
						+"        IdentExpression\n"
						+"          z\n"
						+"      ClosureEvalExpression\n"
						+"        a\n"
						+"        IntLitExpression\n"
						+"          1\n"
						+"        IntLitExpression\n"
						+"          2\n"
						+"        IntLitExpression\n"
						+"          3\n\n";
		ASTNode ast = parseInput("class A { x[z] = a(1,2,3); }");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void def_key_value_type1() throws SyntaxException {
		System.out.println("def_key_value_type1");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    VarDec\n"
						+"      C\n"
						+"      ListType\n"
						+"        SimpleType\n"
						+"          string\n"
						+"    VarDec\n"
						+"      S\n"
						+"      KeyValueType\n"
						+"        SimpleType\n"
						+"          int\n"
						+"        SimpleType\n"
						+"          boolean\n\n";
		ASTNode ast = parseInput("class A {def C:@[string]; def S:@@[int:boolean];} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void def_key_value_type2() throws SyntaxException {
		System.out.println("def_key_value_type1");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    VarDec\n"
						+"      C\n"
						+"      ListType\n"
						+"        SimpleType\n"
						+"          string\n"
						+"    VarDec\n"
						+"      S\n"
						+"      KeyValueType\n"
						+"        SimpleType\n"
						+"          int\n"
						+"        KeyValueType\n"
						+"          SimpleType\n"
						+"            string\n"
						+"          SimpleType\n"
						+"            boolean\n\n";
		ASTNode ast = parseInput("class A {def C:@[string]; def S:@@[int:@@[string:boolean]];} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void expressionStatement() throws SyntaxException {
		System.out.println("expressionStatement");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    ExpressionStatement\n"
						+"      IntLitExpression\n"
						+"        4\n\n";
		ASTNode ast = parseInput("class A { %4; } ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void factor1() throws SyntaxException {
		System.out.println("factor1");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    ClosureDec\n"
						+"      C\n"
						+"      Closure\n"
						+"        AssignmentStatement\n"
						+"          IdentLValue\n"
						+"            x\n"
						+"          IdentExpression\n"
						+"            y\n\n";
		ASTNode ast = parseInput("class A {def C={->x=y;};} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
				
	@Test
	public void factor2() throws SyntaxException {
		System.out.println("factor2");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    ClosureDec\n"
						+"      C\n"
						+"      Closure\n"
						+"        AssignmentStatement\n"
						+"          IdentLValue\n"
						+"            x\n"
						+"          ListOrMapElemExpression\n"
						+"            y\n"
						+"            IdentExpression\n"
						+"              z\n"
						+"    ClosureDec\n"
						+"      D\n"
						+"      Closure\n"
						+"        AssignmentStatement\n"
						+"          IdentLValue\n"
						+"            x\n"
						+"          ListOrMapElemExpression\n"
						+"            y\n"
						+"            IntLitExpression\n"
						+"              1\n\n";
		ASTNode ast = parseInput("class A {def C={->x=y[z];};  def D={->x=y[1];};} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void factor3() throws SyntaxException {
		System.out.println("factor3");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    ClosureDec\n"
						+"      C\n"
						+"      Closure\n"
						+"        AssignmentStatement\n"
						+"          IdentLValue\n"
						+"            x\n"
						+"          IntLitExpression\n"
						+"            3\n\n";						
		ASTNode ast = parseInput("class A {def C={->x=3;};} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void factor4() throws SyntaxException {
		System.out.println("factor4");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    ClosureDec\n"
						+"      C\n"
						+"      Closure\n"
						+"        AssignmentStatement\n"
						+"          IdentLValue\n"
						+"            x\n"
						+"          StringLitExpression\n"
						+"            hello\n\n";						
		ASTNode ast = parseInput("class A {def C={->x=\"hello\";};} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void factor5() throws SyntaxException {
		System.out.println("factor5");
		String correct = "Program\n"
						+"import\n"
						+"class A\n"
						+"  Block\n"
						+"    ClosureDec\n"
						+"      C\n"
						+"      Closure\n"
						+"        AssignmentStatement\n"
						+"          IdentLValue\n"
						+"            x\n"
						+"          BooleanLitExpression\n"
						+"            true\n"
						+"        AssignmentStatement\n"
						+"          IdentLValue\n"
						+"            z\n"
						+"          BooleanLitExpression\n"
						+"            false\n\n";						
		ASTNode ast = parseInput("class A {def C={->x=true; z = false;};} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void factor6() throws SyntaxException {
		System.out.println("factor6");
		String correct = "Program\n"
				+"import\n"
				+"class A\n"
				+"  Block\n"
				+"    ClosureDec\n"
				+"      C\n"
				+"      Closure\n"
				+"        AssignmentStatement\n"
				+"          IdentLValue\n"
				+"            x\n"
				+"          UnaryExpression\n"
				+"            -\n"
				+"            IdentExpression\n"
				+"              y\n"
				+"        AssignmentStatement\n"
				+"          IdentLValue\n"
				+"            z\n"
				+"          UnaryExpression\n"
				+"            !\n"
				+"            IdentExpression\n"
				+"              y\n\n";
		ASTNode ast = parseInput("class A {def C={->x=-y; z = !y;};}");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void def_simple_type1() throws SyntaxException {
		System.out.println("def_simple_type1");
		String correct = "Program\n"+
						 "import\n"+
						 "class A\n"+
						 "  Block\n"+
						 "    VarDec\n"+
						 "      B\n"+
						 "      SimpleType\n"+
						 "        int\n"+
						 "    VarDec\n"+
						 "      C\n"+
						 "      SimpleType\n"+
						 "        boolean\n"+
						 "    VarDec\n"+
						 "      S\n"+
						 "      SimpleType\n"+
						 "        string\n\n";						
		ASTNode ast = parseInput("class A {def B:int; def C:boolean; def S: string;} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void smallest() throws SyntaxException {
		System.out.println("smallest");
		String correct = "Program\n"+
						 "import\n"+
						 "class A\n"+
						 "  Block\n\n";					
		ASTNode ast = parseInput("class A { }");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void emptyStatement() throws SyntaxException {
		System.out.println("emptyStatement");
		String correct = "Program\n"+
						 "import\n"+
						 "class A\n"+
						 "  Block\n\n";						
		ASTNode ast = parseInput("class A  { ;;; }");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void list1() throws SyntaxException {
		System.out.println("list1");
		String correct = "Program\n"+
						 "import\n"+
						 "class A\n"+
						 "  Block\n"+
						 "    AssignmentStatement\n"+
						 "      IdentLValue\n"+
						 "        x\n"+
						 "      ListExpression\n"+
						 "        IdentExpression\n"+
						 "          a\n"+
						 "        IdentExpression\n"+
						 "          b\n"+
						 "        IdentExpression\n"+
						 "          c\n"+
						 "    AssignmentStatement\n"+
						 "      IdentLValue\n"+
						 "        y\n"+
						 "      BinaryExpression\n"+
						 "        ListExpression\n"+
						 "          IdentExpression\n"+
						 "            d\n"+
						 "          IdentExpression\n"+
						 "            e\n"+
						 "          IdentExpression\n"+
						 "            f\n"+
						 "        +\n"+
						 "        IdentExpression\n"+
						 "          x\n\n";						
		ASTNode ast = parseInput("class A  { x = @[a,b,c]; y = @[d,e,f]+x; } ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	
	@Test
	public void maplist1() throws SyntaxException {
		System.out.println("maplist1");
		String correct = "Program\n"+
						 "import\n"+
						 "class A\n"+
						 "  Block\n"+
						 "    AssignmentStatement\n"+
						 "      IdentLValue\n"+
						 "        x\n"+
						 "      MapListExpression\n"+
						 "        KeyValueExpression\n"+
						 "          IdentExpression\n"+
						 "            x\n"+
						 "          IdentExpression\n"+
						 "            y\n"+
						 "    AssignmentStatement\n"+
						 "      IdentLValue\n"+
						 "        y\n"+
						 "      MapListExpression\n"+
						 "        KeyValueExpression\n"+
						 "          IdentExpression\n"+
						 "            x\n"+
						 "          IdentExpression\n"+
						 "            y\n"+
						 "        KeyValueExpression\n"+
						 "          IntLitExpression\n"+
						 "            4\n"+
						 "          IntLitExpression\n"+
						 "            5\n\n";					
		ASTNode ast = parseInput("class A  { x = @@[x:y]; y = @@[x:y,4:5]; } ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void expressions1() throws SyntaxException {
		System.out.println("expressions1");
		String correct = "Program\n"+
						 "import\n"+
						 "class A\n"+
						 "  Block\n"+
						 "    ClosureDec\n"+
						 "      C\n"+
						 "      Closure\n"+
						 "        AssignmentStatement\n"+
						 "          IdentLValue\n"+
						 "            x\n"+
						 "          BinaryExpression\n"+
						 "            IdentExpression\n"+
						 "              x\n"+
						 "            +\n"+
						 "            IntLitExpression\n"+
						 "              1\n"+
						 "        AssignmentStatement\n"+
						 "          IdentLValue\n"+
						 "            z\n"+
						 "          BinaryExpression\n"+
						 "            BinaryExpression\n"+
						 "              IntLitExpression\n"+
						 "                3\n"+
						 "              -\n"+
						 "              IntLitExpression\n"+
						 "                4\n"+
						 "            -\n"+
						 "            IntLitExpression\n"+
						 "              5\n\n";			
		ASTNode ast = parseInput("class A {def C={->x=x+1; z = 3-4-5;};} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void expressions2() throws SyntaxException {
		System.out.println("expressions2");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    ClosureDec\n"+
						"      C\n"+
						"      Closure\n"+
						"        AssignmentStatement\n"+
						"          IdentLValue\n"+
						"            x\n"+
						"          BinaryExpression\n"+
						"            BinaryExpression\n"+
						"              IdentExpression\n"+
						"                x\n"+
						"              +\n"+
						"              BinaryExpression\n"+
						"                BinaryExpression\n"+
						"                  IntLitExpression\n"+
						"                    1\n"+
						"                  /\n"+
						"                  IntLitExpression\n"+
						"                    2\n"+
						"                *\n"+
						"                IntLitExpression\n"+
						"                  3\n"+
						"            -\n"+
						"            UnaryExpression\n"+
						"              -\n"+
						"              IntLitExpression\n"+
						"                4\n"+
						"        AssignmentStatement\n"+
						"          IdentLValue\n"+
						"            z\n"+
						"          BinaryExpression\n"+
						"            BinaryExpression\n"+
						"              IntLitExpression\n"+
						"                3\n"+
						"              -\n"+
						"              IntLitExpression\n"+
						"                4\n"+
						"            -\n"+
						"            IntLitExpression\n"+
						"              5\n\n";						
		ASTNode ast = parseInput("class A {def C={->x=x+1/2*3--4; z = 3-4-5;};}");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void expressions3() throws SyntaxException {
		System.out.println("expressions3");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    ClosureDec\n"+
						"      C\n"+
						"      Closure\n"+
						"        AssignmentStatement\n"+
						"          IdentLValue\n"+
						"            x\n"+
						"          BinaryExpression\n"+
						"            BinaryExpression\n"+
						"              IdentExpression\n"+
						"                x\n"+
						"              +\n"+
						"              BinaryExpression\n"+
						"                BinaryExpression\n"+
						"                  IntLitExpression\n"+
						"                    1\n"+
						"                  /\n"+
						"                  IntLitExpression\n"+
						"                    2\n"+
						"                *\n"+
						"                IntLitExpression\n"+
						"                  3\n"+
						"            -\n"+
						"            UnaryExpression\n"+
						"              -\n"+
						"              IntLitExpression\n"+
						"                4\n"+
						"        AssignmentStatement\n"+
						"          IdentLValue\n"+
						"            z\n"+
						"          BinaryExpression\n"+
						"            BinaryExpression\n"+
						"              IntLitExpression\n"+
						"                3\n"+
						"              -\n"+
						"              IntLitExpression\n"+
						"                4\n"+
						"            -\n"+
						"            IntLitExpression\n"+
						"              5\n\n";					
		ASTNode ast = parseInput("class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void expressions4() throws SyntaxException {
		System.out.println("expressions4");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    AssignmentStatement\n"+
						"      IdentLValue\n"+
						"        x\n"+
						"      BinaryExpression\n"+
						"        IdentExpression\n"+
						"          a\n"+
						"        <<\n"+
						"        IdentExpression\n"+
						"          b\n"+
						"    AssignmentStatement\n"+
						"      IdentLValue\n"+
						"        c\n"+
						"      BinaryExpression\n"+
						"        IdentExpression\n"+
						"          b\n"+
						"        >>\n"+
						"        IdentExpression\n"+
						"          z\n\n";				
		ASTNode ast = parseInput("class A {x = a<<b; c = b>>z;}");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void def_closure1() throws SyntaxException {
		System.out.println("def_closure1");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    ClosureDec\n"+
						"      C\n"+
						"      Closure\n\n";					
		ASTNode ast = parseInput("class A {def C={->};}");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void def_closure2() throws SyntaxException {
		System.out.println("def_closure2");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    ClosureDec\n"+
						"      C\n"+
						"      Closure\n"+
						"    VarDec\n"+
						"      z\n"+
						"      SimpleType\n"+
						"        string\n\n";					
		ASTNode ast = parseInput("class A {def C={->};  def z:string;}");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void statements1() throws SyntaxException {
		System.out.println("statements1");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    AssignmentStatement\n"+
						"      IdentLValue\n"+
						"        x\n"+
						"      IdentExpression\n"+
						"        y\n"+
						"    AssignmentStatement\n"+
						"      ExpressionLValue\n"+
						"        z\n"+
						"        IntLitExpression\n"+
						"          1\n"+
						"      IdentExpression\n"+
						"        b\n"+
						"    PrintStatement\n"+
						"      BinaryExpression\n"+
						"        IdentExpression\n"+
						"          a\n"+
						"        +\n"+
						"        IdentExpression\n"+
						"          b\n"+
						"    PrintStatement\n"+
						"      BinaryExpression\n"+
						"        BinaryExpression\n"+
						"          IdentExpression\n"+
						"            x\n"+
						"          +\n"+
						"          IdentExpression\n"+
						"            y\n"+
						"        -\n"+
						"        IdentExpression\n"+
						"          z\n\n";				
		ASTNode ast = parseInput("class A {x = y; z[1] = b; print a+b; print (x+y-z);}");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void statements2() throws SyntaxException {
		System.out.println("statements2");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    WhileStatement\n"+
						"      IdentExpression\n"+
						"        x\n"+
						"      Block\n"+
						"    WhileRangeStatement\n"+
						"      RangeExpression\n"+
						"        IntLitExpression\n"+
						"          1\n"+
						"        IntLitExpression\n"+
						"          4\n"+
						"      Block\n"+
						"    WhileStarStatement\n"+
						"      BinaryExpression\n"+
						"        IdentExpression\n"+
						"          x\n"+
						"        >\n"+
						"        IntLitExpression\n"+
						"          0\n"+
						"      Block\n\n";				
		ASTNode ast = parseInput("class A  { while (x) {}; while* (1..4){}; while*(x>0){}; }");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void statements3() throws SyntaxException {
		System.out.println("statements3");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    IfStatement\n"+
						"      IdentExpression\n"+
						"        x\n"+
						"      Block\n"+
						"    IfElseStatement\n"+
						"      IdentExpression\n"+
						"        y\n"+
						"      Block\n"+
						"      Block\n"+
						"    IfElseStatement\n"+
						"      IdentExpression\n"+
						"        x\n"+
						"      Block\n"+
						"      Block\n"+
						"        IfElseStatement\n"+
						"          IdentExpression\n"+
						"            z\n"+
						"          Block\n"+
						"          Block\n\n";			
		ASTNode ast = parseInput("class A  { if (x) {}; if (y){} else {}; if (x) {} else {if (z) {} else {};} ; } ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void statements4() throws SyntaxException {
		System.out.println("statements4");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    ExpressionStatement\n"+
						"      ClosureEvalExpression\n"+
						"        a\n"+
						"        IntLitExpression\n"+
						"          1\n"+
						"        IntLitExpression\n"+
						"          2\n"+
						"        IntLitExpression\n"+
						"          3\n\n";					
		ASTNode ast = parseInput("class A  { %a(1,2,3); } ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void statements5() throws SyntaxException {
		System.out.println("statements5");
		String correct ="Program\n"+
						"import\n"+
						"class A\n"+
						"  Block\n"+
						"    AssignmentStatement\n"+
						"      IdentLValue\n"+
						"        x\n"+
						"      ClosureEvalExpression\n"+
						"        a\n"+
						"        IntLitExpression\n"+
						"          1\n"+
						"        IntLitExpression\n"+
						"          2\n"+
						"        IntLitExpression\n"+
						"          3\n\n";						
		ASTNode ast = parseInput("class A  { x = a(1,2,3); }");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void import1() throws SyntaxException {
		System.out.println("import1");
		String correct ="Program\n"+
						"import\n"+
						"  X\n"+
						"class A\n"+
						"  Block\n\n";				
		ASTNode ast = parseInput("import X; class A { } ");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
	@Test
	public void import2() throws SyntaxException {
		System.out.println("import2");
		String correct ="Program\n"+
						"import\n"+
						"  X/Y/Z\n"+
						"  W/X/Y\n"+
						"class A\n"+
						"  Block\n\n";					
		ASTNode ast = parseInput("import X.Y.Z; import W.X.Y; class A { }");
		assertEquals(correct, ast.toString());
		System.out.println(ast);
	}
}
