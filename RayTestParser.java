package cop5555sp15;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import cop5555sp15.Parser.SyntaxException;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.ast.ASTNode;
import static cop5555sp15.TokenStream.Kind.*;

public class RayTestParser {

	
	private void parseIncorrectInput(String input,
			Kind... expectedIncorrectTokenKind) throws SyntaxException {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
//		System.out.println(stream);
		ASTNode ast =parser.parse();
		assertNull(ast);
		List<SyntaxException> exceptions = parser.getExceptionList();
		for(SyntaxException e: exceptions){
			System.out.println(e.getMessage());
		}
			assertEquals(expectedIncorrectTokenKind.length, exceptions.size());
			for (int i = 0; i < exceptions.size(); ++i){
			assertEquals(expectedIncorrectTokenKind[i], exceptions.get(i).t.kind); // class is the incorrect token
		}
	}
	
	private ASTNode parseCorrectInput(String input) throws SyntaxException {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		System.out.println();
		ASTNode ast = parser.parse();
		assertNotNull(ast);
		return ast;
	}	
	
/*
 * example
 */
	@Test
	public void almostEmpty() throws SyntaxException {
		System.out.println("almostEmpty");
		String input = "class A { } ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void almostEmptyIncorrect() throws SyntaxException {
		System.out.println("almostEmpty");
		String input = "class A { ] ";
		System.out.println(input);
		parseIncorrectInput(input,RSQUARE);		
	}
/*
* example
*/
	@Test
	public void import4() throws SyntaxException {
		System.out.println("import4");
		String input = "import; class A { } ";
		System.out.println(input);
		parseIncorrectInput(input, SEMICOLON);	
	}
	
	@Test
	public void import5() throws SyntaxException {
		System.out.println("import5");
		String input = "import a class A { } ";
		System.out.println(input);
		parseIncorrectInput(input, KW_CLASS);	
	}
	
	@Test
	public void import6() throws SyntaxException {
		System.out.println("import6");
		String input = "import a.; class A { } ";
		System.out.println(input);
		parseIncorrectInput(input, SEMICOLON);	
	}
	
	@Test
	public void import7() throws SyntaxException {
		System.out.println("import7");
		String input = "import a.b class A { } ";
		System.out.println(input);
		parseIncorrectInput(input, KW_CLASS);	
	}
	
	@Test
	public void import8() throws SyntaxException {
		System.out.println("import8");
		String input = "import 1; class A { } ";
		System.out.println(input);
		parseIncorrectInput(input, INT_LIT);
	}
	
	@Test
	public void incorrectClass1() throws SyntaxException {
		System.out.println("incorrectClass1");
		String input = "import a.b{ } ";
		System.out.println(input);
		parseIncorrectInput(input, LCURLY);	
	}
	
	public void incorrectClass2() throws SyntaxException {
		System.out.println("incorrectClass2");
		String input = "import a.b; class 2{ } ";
		System.out.println(input);
		parseIncorrectInput(input, INT_LIT);	
	}
	
	@Test
	public void def_simple_type3() throws SyntaxException {
		System.out.println("def_simple_type3");
		String input = "class A {def B:int; def C,boolean; def S: string;} ";
		System.out.println(input);
		parseIncorrectInput(input, COMMA);	
	}
	
	@Test
	public void def_simple_type4() throws SyntaxException {
		System.out.println("def_simple_type4");
		String input = "class A {def B: ine;} ";
		System.out.println(input);
		parseIncorrectInput(input, IDENT);
	}
	
	@Test
	public void def_simple_type5() throws SyntaxException {
		System.out.println("def_simple_type5");
		String input = "class A {def B: 123;} ";
		System.out.println(input);
		parseIncorrectInput(input, INT_LIT);
	}
	
	@Test
	public void def_simple_type6() throws SyntaxException {
		System.out.println("def_simple_type4");
		String input = "class A {def 123: int;} ";
		System.out.println(input);
		parseIncorrectInput(input, INT_LIT);
	}
	
	@Test
	public void declaration1() throws SyntaxException {
		System.out.println("declaration1");
		String input = "class A {def X;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void declaration2() throws SyntaxException {
		System.out.println("declaration2");
		String input = "class A {def X:;} ";
		System.out.println(input);
		parseIncorrectInput(input, SEMICOLON);
	}
	
	@Test
	public void declaration3() throws SyntaxException {
		System.out.println("declaration3");
		String input = "class A {def X=;} ";
		System.out.println(input);
		parseIncorrectInput(input, SEMICOLON);
	}
	
	@Test
	public void declaration4() throws SyntaxException {
		System.out.println("declaration4");
		String input = "class A {def X={;} ";
		System.out.println(input);
		parseIncorrectInput(input, SEMICOLON);
	}
	
	@Test
	public void declaration5() throws SyntaxException {
		System.out.println("declaration5");
		String input = "class A {def X={};} ";
		System.out.println(input);
		parseIncorrectInput(input, RCURLY);
	}
	
	@Test
	public void def_key_value_type3() throws SyntaxException {
		System.out.println("def_key_value_type3");
		String input = "class A {def S:@@[int:@@[int:@@[int:@@[int:string]]]];} ";
		System.out.println(input);
		parseCorrectInput(input);
	}		
		
	@Test
	public void def_key_value_type4() throws SyntaxException {
		System.out.println("def_key_value_type4");
		String input = "class A {def S:@@(int:string];} ";
		System.out.println(input);
		parseIncorrectInput(input, LPAREN);
	}	
	
	@Test
	public void def_key_value_type5() throws SyntaxException {
		System.out.println("def_key_value_type5");
		String input = "class A {def S:@@[int];} ";
		System.out.println(input);
		parseIncorrectInput(input, RSQUARE);
	}	
	
	@Test
	public void def_key_value_type6() throws SyntaxException {
		System.out.println("def_key_value_type6");
		String input = "class A {def S:@@[int:];} ";
		System.out.println(input);
		parseIncorrectInput(input, RSQUARE);
	}		
			
	@Test
	public void def_list_type1() throws SyntaxException {
		System.out.println("def_key_value_type1");
		String input = "class A {def L:@[@[@[@[int]]]];} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_list_type2() throws SyntaxException {
		System.out.println("def_key_value_type2");
		String input = "class A {def L:@{int];} ";
		System.out.println(input);
		parseIncorrectInput(input, LCURLY);
	}	
	
	@Test
	public void def_list_type3() throws SyntaxException {
		System.out.println("def_key_value_type3");
		String input = "class A {def L:@[0];} ";
		System.out.println(input);
		parseIncorrectInput(input, INT_LIT);
	}	
	
	@Test
	public void def_list_type4() throws SyntaxException {
		System.out.println("def_key_value_type4");
		String input = "class A {def L:@[];} ";
		System.out.println(input);
		parseIncorrectInput(input, RSQUARE);
	}	
	
	@Test
	public void def_closure3() throws SyntaxException {
		System.out.println("def_closure3");
		String input = "class A {def x={y->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_closure4() throws SyntaxException {
		System.out.println("def_closure4");
		String input = "class A {def x={y, z:string ,v:int->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_closure5() throws SyntaxException {
		System.out.println("def_closure5");
		String input = "class A {def x={y, z, v:@@[int:string]->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_closure6() throws SyntaxException {
		System.out.println("def_closure6");
		String input = "class A {def x={y, z, v,->};} ";
		System.out.println(input);
		parseIncorrectInput(input, ARROW);
	}	
	
	@Test
	public void def_closure7() throws SyntaxException {
		System.out.println("def_closure7");
		String input = "class A {def x={ def ->};} ";
		System.out.println(input);
		parseIncorrectInput(input, KW_DEF);
	}	
	
	@Test
	public void def_closure8() throws SyntaxException {
		System.out.println("def_closure8");
		String input = "class A {def x={ - ->};} ";
		System.out.println(input);
		parseIncorrectInput(input, MINUS);
	}	
	
	@Test
	public void def_closure9() throws SyntaxException {
		System.out.println("def_closure9");
		String input = "class A {def x={ 1 ->};} ";
		System.out.println(input);
		parseIncorrectInput(input, INT_LIT);
	}	
	
	@Test
	public void def_closure10() throws SyntaxException {
		System.out.println("def_closure10");
		String input = "class A {def x={ y, @ ->};} ";
		System.out.println(input);
		parseIncorrectInput(input, AT);
	}		
	
	@Test
	public void def_closure11() throws SyntaxException {
		System.out.println("def_closure11");
		String input = "class A {def x={ y, z -> if(a > 0){t=y-z;} else {t=y+z;}; };} ";
		System.out.println(input);
		parseCorrectInput(input);
	}		
	
	@Test
	public void def_closure12() throws SyntaxException {
		System.out.println("def_closure12");
		String input = "class A {def x={ ,z ->};} ";
		System.out.println(input);
		parseIncorrectInput(input, COMMA);
	}		
	
	@Test
	public void statement6() throws SyntaxException {
		System.out.println("statement6");
		String input = "class A  { while*(x < y) { return x+y; }; } ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void statement7() throws SyntaxException {
		System.out.println("statement7");
		String input = "class A  { while*(a[1]..a[10]) { ; }; } ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void statement8() throws SyntaxException {
		System.out.println("statement8");
		String input = "class A  { while/(a[1]..a[10]) { }; } ";
		System.out.println(input);
		parseIncorrectInput(input, DIV);
	}
	
	@Test
	public void statement9() throws SyntaxException {
		System.out.println("statement9");
		String input = "class A  { while a) { }; } ";
		System.out.println(input);
		parseIncorrectInput(input, IDENT);
	}
	
	@Test
	public void statement10() throws SyntaxException {
		System.out.println("statement10");
		String input = "class A  { while(a { }; } ";
		System.out.println(input);
		parseIncorrectInput(input, LCURLY);
	}
	
	@Test
	public void statement11() throws SyntaxException {
		System.out.println("statement11");
		String input = "class A  { if a) { }; } ";
		System.out.println(input);
		parseIncorrectInput(input, IDENT);
	}
	
	@Test
	public void statement12() throws SyntaxException {
		System.out.println("statement12");
		String input = "class A  { if(a { }; } ";
		System.out.println(input);
		parseIncorrectInput(input, LCURLY);
	}
	
	@Test
	public void statement13() throws SyntaxException {
		System.out.println("statement13");
		String input = "class A  { if(a) { } else() { }; } ";
		System.out.println(input);
		parseIncorrectInput(input, LPAREN);
	}
	
	@Test
	public void closureEval1()throws SyntaxException {
		System.out.println("closureEval1");
		String input = "class A  { x[z] = a(b,c[x+y],2,true, false, \"str\", (d+e)); } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void closureEval2()throws SyntaxException {
		System.out.println("closureEval2");
		String input = "class A  { x[z] = a(b,c]; } ";
		System.out.println(input);
		parseIncorrectInput(input, RSQUARE);
	} 	
	
	@Test
	public void expressionList1()throws SyntaxException {
		System.out.println("expressionList1");
		String input = "class A  { x[z] = a(b,); } ";
		System.out.println(input);
		parseIncorrectInput(input, RPAREN);
	} 	
	
	@Test
	public void expressionList2()throws SyntaxException {
		System.out.println("expressionList2");
		String input = "class A  { x[z] = a(b;); } ";
		System.out.println(input);
		parseIncorrectInput(input, SEMICOLON);
	} 	
	
	@Test
	public void expressionList3()throws SyntaxException {
		System.out.println("expressionList3");
		String input = "class A  { x[z] = a( ); } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void list1()throws SyntaxException {
		System.out.println("list1");
		String input = "class A  { x = @[!a, -a, size(n), key(c), value(d)]; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void list2()throws SyntaxException {
		System.out.println("list2");
		String input = "class A  { x = @[a}; } ";
		System.out.println(input);
		parseIncorrectInput(input, RCURLY);
	} 	
	
	@Test
	public void list3()throws SyntaxException {
		System.out.println("list3");
		String input = "class A  { x = @[]; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void mapList1()throws SyntaxException {
		System.out.println("mapList1");
		String input = "class A  { x = @@[a:b,(!c-d):e>=f]; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void mapList2()throws SyntaxException {
		System.out.println("mapList2");
		String input = "class A  { x = @@@[a]; } ";
		System.out.println(input);
		parseIncorrectInput(input, AT);
	} 	
	
	@Test
	public void mapList3()throws SyntaxException {
		System.out.println("mapList3");
		String input = "class A  { x = @@[); } ";
		System.out.println(input);
		parseIncorrectInput(input, RPAREN);
	} 	
	
	@Test
	public void mapList4()throws SyntaxException {
		System.out.println("mapList4");
		String input = "class A  { x = @@[]; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void mapList5()throws SyntaxException {
		System.out.println("mapList5");
		String input = "class A  { x = @@[a:b,]; } ";
		System.out.println(input);
		parseIncorrectInput(input, RSQUARE);
	} 	
	
	@Test
	public void mapList6()throws SyntaxException {
		System.out.println("mapList6");
		String input = "class A  { x = @@[a,b]; } ";
		System.out.println(input);
		parseIncorrectInput(input, COMMA);
	} 	
	
	@Test
	public void expressions5() throws SyntaxException {
		System.out.println("expressions5");
		String input = "class A {x=a|b; x=a&b; x=a==b; x=a!=b; x=a<b; x=a>b; x=a<=b; x=a>=b;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions6() throws SyntaxException {
		System.out.println("expressions6");
		String input = "class A {x=a||b;} ";
		System.out.println(input);
		parseIncorrectInput(input, BAR);
	}
	
	@Test
	public void expressions7() throws SyntaxException {
		System.out.println("expressions7");
		String input = "class A {x=a&&b;} ";
		System.out.println(input);
		parseIncorrectInput(input, AND);
	}
	
	@Test
	public void expressions8() throws SyntaxException {
		System.out.println("expressions8");
		String input = "class A {x=a===b;} ";
		System.out.println(input);
		parseIncorrectInput(input, ASSIGN);
	}
	
	@Test
	public void expressions9() throws SyntaxException {
		System.out.println("expressions9");
		String input = "class A {x=a@b;} ";
		System.out.println(input);
		parseIncorrectInput(input, AT);
	}
		
	@Test
	public void factor8() throws SyntaxException {
		System.out.println("factor8");
		String input = "class A {def C={->x= (a+b);};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor9() throws SyntaxException {
		System.out.println("factor9");
		String input = "class A {def C={->x= a(1, 2, 3);};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor10() throws SyntaxException {
		System.out.println("factor10");
		String input = "class A {def C={->x= a(1, a, \"c\");};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor11() throws SyntaxException {
		System.out.println("factor11");
		String input = "class A {def C={->x= {a-> b= 4+5; };};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor12() throws SyntaxException {
		System.out.println("factor12");
		String input = "class A {def C={->x= @[a, b, 1];};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor13() throws SyntaxException {
		System.out.println("factor13");
		String input = "class A {def C={->x= @@[a:b, 1:3];};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor14() throws SyntaxException {
		System.out.println("factor14");
		String input = "class A {def C={->x= +;};} ";
		System.out.println(input);
		parseIncorrectInput(input, PLUS);
	}
	

}
