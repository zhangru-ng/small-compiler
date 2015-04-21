package cop5555sp15;

import static org.junit.Assert.*;

import org.junit.Test;
import cop5555sp15.Parser.SyntaxException;
import cop5555sp15.ast.ASTNode;

public class TestParser {

	
	private ASTNode parseCorrectInput(String input) {
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
	public void smallest() throws SyntaxException {
		System.out.println("***********\nsmallest");
		String input = "class A { } ";
		System.out.println(input);
		ASTNode ast = parseCorrectInput(input);
		System.out.println(ast);
	}

	@Test
	public void import1() throws SyntaxException {
		System.out.println("***********\nimport1");
		String input = "import X; class A { } ";
		System.out.println(input);
		ASTNode ast = parseCorrectInput(input);
		System.out.println(ast);
	}

	@Test
	public void import2() throws SyntaxException {
		System.out.println("***********\nimport2");
		String input = "import X.Y.Z; import W.X.Y; class A { } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}


	@Test
	public void def_simple_type1() throws SyntaxException {
		System.out.println("***********\ndef_simple_type1");
		String input = "class A {def B:int; def C:boolean; def S: string;} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}


	
	@Test
	public void def_key_value_type1() throws SyntaxException {
		System.out.println("***********\ndef_key_value_type1");
		String input = "class A {def C:@[string]; def S:@@[int:boolean];} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}	
	
	@Test
	public void def_key_value_type2() throws SyntaxException {
		System.out.println("***********\ndef_key_value_type1");
		String input = "class A {def C:@[string]; def S:@@[int:@@[string:boolean]];} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}	
	
	@Test
	public void def_closure1() throws SyntaxException {
		System.out.println("***********\ndef_closure1");
		String input = "class A {def C={->};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}

	@Test
	public void def_closure2() throws SyntaxException {
		System.out.println("***********\ndef_closure2");
		String input = "class A {def C={->};  def z:string;} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void factor1() throws SyntaxException {
		System.out.println("***********\nfactor1");
		String input = "class A {def C={->x=y;};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	
	@Test
	public void factor2() throws SyntaxException {
		System.out.println("***********\nfactor2");
		String input = "class A {def C={->x=y[z];};  def D={->x=y[1];};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void factor3() throws SyntaxException {
		System.out.println("***********\nfactor3");
		String input = "class A {def C={->x=3;};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void factor4() throws SyntaxException {
		System.out.println("***********\nfactor4");
		String input = "class A {def C={->x=\"hello\";};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void factor5() throws SyntaxException {
		System.out.println("***********\nfactor5");
		String input = "class A {def C={->x=true; z = false;};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	
	@Test
	public void factor6() throws SyntaxException {
		System.out.println("***********\nfactor6");
		String input = "class A {def C={->x=-y; z = !y;};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	
	@Test
	public void expressions1() throws SyntaxException {
		System.out.println("***********\nexpressions1");
		String input = "class A {def C={->x=x+1; z = 3-4-5;};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void expressions2() throws SyntaxException {
		System.out.println("***********\nexpressions2");
		String input = "class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void expressions3() throws SyntaxException {
		System.out.println("***********\nexpressions3");
		String input = "class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void expressions4() throws SyntaxException {
		System.out.println("***********\nexpressions4");
		String input = "class A {x = a<<b; c = b>>z;} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void statements1()throws SyntaxException {
		System.out.println("***********\nstatements1");
		String input = "class A {x = y; z[1] = b; print a+b; print (x+y-z);} ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	}
	
	@Test
	public void statements2()throws SyntaxException {
		System.out.println("***********\nstatements2");
		String input = "class A  {\n while (x) {};  \n while* (1..4){}; \n while*(x>0){}; } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 
	
	@Test
	public void statements3()throws SyntaxException {
		System.out.println("***********\nstatements3");
		String input = "class A  {\n if (x) {};  \n if (y){} else {}; \n if (x) {} else {if (z) {} else {};} ; } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 
	
	@Test
	public void emptyStatement()throws SyntaxException {
		System.out.println("***********\nemptyStatement");
		String input = "class A  { ;;; } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 	
	
	@Test
	public void statements4()throws SyntaxException {
		System.out.println("***********\nstatements4");
		String input = "class A  { %a(1,2,3); } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 	
	
	@Test
	public void statements5()throws SyntaxException {
		System.out.println("***********\nstatements5");
		String input = "class A  { x = a(1,2,3); } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 	
	
	@Test
	public void closureEval()throws SyntaxException {
		System.out.println("***********\nclosureEval");
		String input = "class A  { x[z] = a(1,2,3); } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 	
	
	@Test
	public void list1()throws SyntaxException {
		System.out.println("***********\nlist1");
		String input = "class A  { \n x = @[a,b,c]; \n y = @[d,e,f]+x; \n } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 	
	
	@Test
	public void maplist1()throws SyntaxException {
		System.out.println("***********\nmaplist1");
		String input = "class A  { x = @@[x:y]; y = @@[x:y,4:5]; } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 	
	
	
	@Test
	public void expressionStatement()throws SyntaxException {
		System.out.println("***********\nmaplist1");
		String input = "class A  { %4; } ";
		System.out.println(input);
		System.out.println(parseCorrectInput(input));
	} 	
}
