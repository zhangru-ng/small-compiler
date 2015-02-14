package cop5555sp15;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

import cop5555sp15.SimpleParser.SyntaxException;
import cop5555sp15.TokenStream.Kind;
import static cop5555sp15.TokenStream.Kind.*;

public class TestSimpleParser {

	@Rule
	public ExpectedException exception = ExpectedException.none();

    @Rule
    public Timeout globalTimeout= new Timeout(1000);
	
	private void parseIncorrectInput(String input,
			Kind ExpectedIncorrectTokenKind) {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		SimpleParser parser = new SimpleParser(stream);
		try {
			parser.parse();
			fail(); // should have thrown an exception
		} catch (SyntaxException e) {
			assertEquals(ExpectedIncorrectTokenKind, e.t.kind); // class is the incorrect token
		}
	}
	
	private void parseCorrectInput(String input) throws SyntaxException {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		SimpleParser parser = new SimpleParser(stream);
		parser.parse();
	}	


	@Test
	public void smallest() throws SyntaxException {
		System.out.println("smallest");
		String input = "class A { } ";
		parseCorrectInput(input);
	}

	@Test
	public void import1() throws SyntaxException {
		System.out.println("import1");
		String input = "import X; class A { } ";
		parseCorrectInput(input);
	}

	@Test
	public void import2() throws SyntaxException {
		System.out.println("import2");
		String input = "import X.Y.Z; import W.X.Y; class A { } ";
		parseCorrectInput(input);
	}

	@Test
	public void import3() throws SyntaxException {
		System.out.println("import3");
		String input = "import class A { } "; // this input is wrong.
		Kind ExpectedIncorrectTokenKind = KW_CLASS;
		parseIncorrectInput(input, ExpectedIncorrectTokenKind);

	}
	
	@Test
	public void import4() throws SyntaxException {
		System.out.println("import4");
		String input = "import X class A { } "; // this input is wrong.
		Kind ExpectedIncorrectTokenKind = KW_CLASS;
		parseIncorrectInput(input, ExpectedIncorrectTokenKind);

	}

	@Test
	public void def_simple_type1() throws SyntaxException {
		System.out.println("def_simple_type1");
		String input = "class A {def B:int; def C:boolean; def S: string;} ";
		parseCorrectInput(input);
	}

	@Test
	public void def_simple_type2() throws SyntaxException {
		System.out.println("def_simple_type2");
		String input = "class A {def B:int; def C:boolean; def S: string} ";
		parseIncorrectInput(input, RCURLY);
	}
	
	@Test
	public void def_key_value_type1() throws SyntaxException {
		System.out.println("def_key_value_type1");
		String input = "class A {def C:@[string]; def S:@@[int:boolean];} ";
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_key_value_type2() throws SyntaxException {
		System.out.println("def_key_value_type2");
		String input = "class A {def C:@[string]; def S:@@[int:@@[string:boolean]];} ";
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_key_value_type3() throws SyntaxException {
		System.out.println("def_key_value_type3");
		String input = "class A {def C:@[@[int]]; def S:@@[int:@@[string:@[int]]];} ";
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_key_value_type4() throws SyntaxException {
		System.out.println("def_key_value_type4");
		String input = "class A {def C; def S:@@[int:@[int]];} ";
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_key_value_type5() throws SyntaxException {
		System.out.println("def_key_value_type5");
		String input = "class A { def S:@[int:string];} ";
		parseIncorrectInput(input, COLON);
	}
	
	@Test
	public void def_key_value_type6() throws SyntaxException {
		System.out.println("def_key_value_type6");
		String input = "class A { def S:@@[int];} ";
		parseIncorrectInput(input, RSQUARE);
	}
	
	@Test
	public void def_closure1() throws SyntaxException {
		System.out.println("def_closure1");
		String input = "class A {def C={->};} ";
		parseCorrectInput(input);
	}

	@Test
	public void def_closure2() throws SyntaxException {
		System.out.println("def_closure2");
		String input = "class A {def C={->x=1;};  def z:string;} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void def_closure3() throws SyntaxException {
		System.out.println("def_closure3");
		String input = "class A {def C={s:string,i:int->};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void def_closure4() throws SyntaxException {
		System.out.println("def_closure4");
		String input = "class A {def C={s:string,i:int->x=1;};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void def_closure5() throws SyntaxException {
		System.out.println("def_closure5");
		String input = "class A {def C={s:@@[string:string],i:@[int]->x=1;};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void factor1() throws SyntaxException {
		System.out.println("factor1");
		String input = "class A {def C={->x=y;};} ";
		parseCorrectInput(input);
	}
	
	
	@Test
	public void factor2() throws SyntaxException {
		System.out.println("factor2");
		String input = "class A {def C={->x=y[z];};  def D={->x=y[1];};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void factor3() throws SyntaxException {
		System.out.println("factor3");
		String input = "class A {def C={->x=3;};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void factor4() throws SyntaxException {
		System.out.println("factor4");
		String input = "class A {def C={->x=\"hello\";};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void factor5() throws SyntaxException {
		System.out.println("factor5");
		String input = "class A {def C={->x=true; z = false;};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void factor6() throws SyntaxException {
		System.out.println("factor6");
		String input = "class A {def C={->x=-y; z = !y;};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void factor7() throws SyntaxException {
		System.out.println("factor7");
		String input = "class A {def C={->x= &y; z = !y;};} ";
		parseIncorrectInput(input,AND);
	}
	
	@Test
	public void expressions1() throws SyntaxException {
		System.out.println("expressions1");
		String input = "class A {def C={->x=x+1; z = 3-4-5;};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions2() throws SyntaxException {
		System.out.println("expressions2");
		String input = "class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions3() throws SyntaxException {
		System.out.println("expressions3");
		String input = "class A {def C={->x=x+1/2*3-!4; z = 3-(4-5);};} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions4() throws SyntaxException {
		System.out.println("expressions4");
		String input = "class A {x = a<<b; c = b>>z;} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions5() throws SyntaxException {
		System.out.println("expressions5");
		String input = "class A {x = a<<b+b>>a; c = a/x+!b>>z;} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void statements1()throws SyntaxException {
		System.out.println("statements1");
		String input = "class A {x = y; z[1] = b; print a+b; print (x+y-z);} ";
		parseCorrectInput(input);
	}
	
	@Test
	public void statements2()throws SyntaxException {
		System.out.println("statements2");
		String input = "class A  {\n while (x) {};  \n while* (1..4){}; } ";
		parseCorrectInput(input);
	} 
	
	@Test
	public void statements6()throws SyntaxException {
		System.out.println("statements6");
		String input = "class A  {%5; return (a+b+c);} ";
		parseCorrectInput(input);
	} 	

	@Test
	public void statements3()throws SyntaxException {
		System.out.println("statements3");
		String input = "class A  {\n if (x) {};  \n if (y){} else {}; \n if (x) {} else {if (z) {} else {};} ; } ";
		parseCorrectInput(input);
	} 
	
	@Test
	public void statements8()throws SyntaxException {
		System.out.println("statements8");
		String input = "class A  {\n if (x) { def X={->}; def x:int; x=2; }; }";
		parseCorrectInput(input);
	} 
	
	@Test
	public void statements9()throws SyntaxException {
		System.out.println("statements9");
		String input = "class A  {\n if (x) {} else { def X={->}; def x:int; x=2; }; }";
		parseCorrectInput(input);
	} 
	
	@Test
	public void statements10()throws SyntaxException {
		System.out.println("statements10");
		String input = "class A  {\n if (x) {} else { def X={->}; def x:int; x=2; while* (1..x) { while (x) { return x; }; print aabcc;}; }; }";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void emptyStatement()throws SyntaxException {
		System.out.println("emptyStatement");
		String input = "class A  { ;;; } ";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void statements4()throws SyntaxException {
		System.out.println("statements4");
		String input = "class A  { %a(1,2,3); } ";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void statements5()throws SyntaxException {
		System.out.println("statements5");
		String input = "class A  { x = a(1,2,3); x[z] = a(1,2,3); } ";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void testRelOp()throws SyntaxException {
		System.out.println("testRelOp");
		String input = "class A  { b=a|x; c=a&b|d; c=b==d; c=c!=a<s; d=d<=b>=s; x=q!=d>s; }";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void list1()throws SyntaxException {
		System.out.println("list1");
		String input = "class A  { \n x = @[a,b,c]; \n y = @[d,e,f]+x; \n } ";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void list2()throws SyntaxException {
		System.out.println("list2");
		String input = "class A  {x = @[a+b, !b<<s, {->}]; y=@[ @@[a+b:{s:string->a=b;}]];} ";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void maplist1()throws SyntaxException {
		System.out.println("maplist1");
		String input = "class A  { x = @@[x:y]; y = @@[x:y,4:5]; z= @@[x:y, {->}:3+a]; a = @@[@[a,b] : @[a(a+b),d]];} ";
		parseCorrectInput(input);
	} 	
	
	@Test
	public void factorKeyValSize()throws SyntaxException {
		System.out.println("factorKeyValSize");
		String input = "class A  { s=key({->}); d=value(a); s=size(a(10)); } ";
		parseCorrectInput(input);
	} 	
	
}
