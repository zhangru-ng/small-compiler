package cop5555sp15;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import cop5555sp15.Parser.SyntaxException;
import cop5555sp15.ast.ASTNode;
import cop5555sp15.ast.CodeGenVisitor;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.TypeCheckVisitor;
import cop5555sp15.ast.TypeCheckVisitor.TypeCheckException;
import cop5555sp15.symbolTable.SymbolTable;

public class TestCodeGenerationAssignment5 {
	
    public static class DynamicClassLoader extends ClassLoader {
        public DynamicClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> define(String className, byte[] bytecode) {
            return super.defineClass(className, bytecode, 0, bytecode.length);
        }
    };    

 public void dumpBytecode(byte[] bytecode){   
    int flags = ClassReader.SKIP_DEBUG;
    ClassReader cr;
    cr = new ClassReader(bytecode); 
    cr.accept(new TraceClassVisitor(new PrintWriter(System.out)), flags);
}
	
	private ASTNode parseCorrectInput(String input) throws SyntaxException {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		System.out.println();
		ASTNode ast = parser.parse();
		assertNotNull(ast);
		List<SyntaxException> exceptions = parser.getExceptionList();
		for(SyntaxException e: exceptions){
			System.out.println(e.getMessage());
		}
//		if (ast == null) {
//			System.out.println("errors " + parser.getErrors());
//		}
//		assertNotNull(ast);
		return ast;
	}

	private ASTNode typeCheckCorrectAST(ASTNode ast) throws Exception {
		SymbolTable symbolTable = new SymbolTable();
		TypeCheckVisitor v = new TypeCheckVisitor(symbolTable);
		try {
			ast.visit(v, null);
		} catch (TypeCheckException e) {
			System.out.println(e.getMessage());
			fail("no errors expected");
		}
		return ast;
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
	
	private byte[] generateByteCode(ASTNode ast) throws Exception {
		CodeGenVisitor v = new CodeGenVisitor();
			byte[] bytecode = (byte[]) ast.visit(v, null);
			dumpBytecode(bytecode);
			return bytecode;
	}
	
    public void executeByteCode(String name, byte[] bytecode) throws InstantiationException, IllegalAccessException, MalformedURLException, ClassNotFoundException{
        DynamicClassLoader loader = new DynamicClassLoader(Thread
                .currentThread().getContextClassLoader());
        Class<?> testClass = loader.define(name, bytecode);
        Codelet codelet = (Codelet) testClass.newInstance();

        codelet.execute();
    }
   


/** In this assignment, we add support for 
 * unary expressions (-, !) 
 * if statement
 * ifelse statements
 * while loops.  
 * support for lists.
 */

 
    
/**  
 * Unary expression -
 * 
 *    Type checking:
 *    	- only applies to int
 *   output:
 *   -4
 *   1 
 *    
 */
    @Test
    public void unaryMinus() throws Exception{
    	System.out.println("***********unaryMinus");
    	String input = "class B {\n  print -4; print -3 + 4;  \n}";
    	System.out.println(input);
    	Program program = (Program) parseCorrectInput(input);
    	assertNotNull(program);
    	typeCheckCorrectAST(program);
    	byte[] bytecode = generateByteCode(program);
    	assertNotNull(bytecode);
    	System.out.println("\nexecuting bytecode:");
    	executeByteCode(program.JVMName, bytecode);
    }  
    
    @Test
    /**
     * Unary !
     * 
     * Note that there is no not operation in bytecode.  
     * Instead you need to compare with zero and branch.
     * 
     * output:
     * false
     * true
     * true
     * true
     */
    public void unaryNot() throws Exception{
    	System.out.println("***********unaryNot");
    	String input = "class B {\n print (!true); print (!!true); print !false; print !!!false; \n}";
    	System.out.println(input);
    	Program program = (Program) parseCorrectInput(input);
    	assertNotNull(program);
    	typeCheckCorrectAST(program);
    	byte[] bytecode = generateByteCode(program);
    	assertNotNull(bytecode);
    	System.out.println("\nexecuting bytecode:");
    	executeByteCode(program.JVMName, bytecode);
    }    
/**
 * If statement
 * 
 * Type checking:  ensure that the guard has type boolean.
 * 
 * Code generation:  
 *   evaluate guard
 *   IFEQ L1
 *   statements in block
 *   L1:  
 *     
 *     
 *  output:
 *  true
 *  done  
 */
    @Test
    public void if1() throws Exception{
    	System.out.println("***********if1");
    	String input = "class B {\n  if (true) {\n    print \"true\";\n};\n   print \"done\";  \n}";
    	System.out.println(input);
    	Program program = (Program) parseCorrectInput(input);
    	assertNotNull(program);
    	typeCheckCorrectAST(program);
    	byte[] bytecode = generateByteCode(program);
    	assertNotNull(bytecode);
    	System.out.println("\nexecuting bytecode:");
    	executeByteCode(program.JVMName, bytecode);
    }    
 
/**
 * Test with false guard
 * 
 * output:
 * done
 * 
 */
    @Test
    public void if2() throws Exception{
    	System.out.println("***********if2");
    	String input = "class B {\n  if (false) {\n    print \"false\";\n};\n   print \"done\";  \n}";
    	System.out.println(input);
    	Program program = (Program) parseCorrectInput(input);
    	assertNotNull(program);
    	typeCheckCorrectAST(program);
    	byte[] bytecode = generateByteCode(program);
    	assertNotNull(bytecode);
    	System.out.println("\nexecuting bytecode:");
    	executeByteCode(program.JVMName, bytecode);
    }    
    
/**
 * The type checker should throw an exception because the guard, 42, is not boolean
 * 
 */
    @Test
    public void ifFail() throws Exception{
    	System.out.println("***********ifFail");
    	String input = "class B {\n  if (42) {\n    print 42;\n};\n   print \"done\";  \n}";
    	System.out.println(input);
    	Program program = (Program) parseCorrectInput(input);
    	typeCheckIncorrectAST(program);	
    }       
    
    
/**
* if else statement
* 
* Type checking:  the guard must be boolean
* 
* output:
* if branch
* done
*/
@Test
    public void ifelse1() throws Exception{
    	System.out.println("***********ifelse1");
    	String input = "class B {\n  if (true) {\n    print \"if branch\";\n} else {\n    print \"else branch \";\n};\n   print \"done\";  \n}";
    	System.out.println(input);
    	Program program = (Program) parseCorrectInput(input);
    	assertNotNull(program);
    	typeCheckCorrectAST(program);
    	byte[] bytecode = generateByteCode(program);
    	assertNotNull(bytecode);
    	System.out.println("\nexecuting bytecode:");
    	executeByteCode(program.JVMName, bytecode);
    }   
    
    /**
     * output:
     * else branch
     * done
     */
     @Test
     public void ifelse2() throws Exception{
     	System.out.println("***********ifelse2");
     	String input = "class B {\n  if (false) {\n    print \"if branch\";\n} else {\n    print \"else branch \";\n};\n   print \"done\";  \n}";
     	System.out.println(input);
     	Program program = (Program) parseCorrectInput(input);
     	assertNotNull(program);
     	typeCheckCorrectAST(program);
     	byte[] bytecode = generateByteCode(program);
     	assertNotNull(bytecode);
     	System.out.println("\nexecuting bytecode:");
     	executeByteCode(program.JVMName, bytecode);
     }   
     
     /**
      * The type checker should throw an exception because the guard, 42, is not boolean
      */
     @Test
     public void ifelseFail() throws Exception{
     	System.out.println("***********ifelseFail");
     	String input = "class B {\n  if (42) {\n    print 42;\n} else {\n   print \"not 42\";};  \n}";
     	System.out.println(input);
     	Program program = (Program) parseCorrectInput(input);
     	typeCheckIncorrectAST(program);	
     }  

     /**
      * A more complicated example with nested if else statements
      *  output:
         in if branch
         a>0
         done
     */
     @Test
     public void ifelse3() throws Exception{
  	System.out.println("***********ifelse3");
  	String ifbranch = "    print \"in if branch\";\n     if (a > 0) {print \"a>0\";}\n     else {print \"a <= 0\";};";
  	String elsebranch = 	"    print \"in else branch\";\n    if (b > 0) {print \"b>0\";}\n     else {print \"b <= 0\";\n    };";	
  	String input = "class B {\n   def a:int;\n   def b:int;\n   a = 3;\n   b = 4;\n"
  			+ " if (b>0) {\n" + ifbranch + "\n}\n else {\n  " + elsebranch + "\n}; \n print \"done\";  \n}";
  	System.out.println(input);
  	Program program = (Program) parseCorrectInput(input);
  	assertNotNull(program);
  	typeCheckCorrectAST(program);
  	byte[] bytecode = generateByteCode(program);
  	assertNotNull(bytecode);
  	System.out.println("\nexecuting bytecode:");
  	executeByteCode(program.JVMName, bytecode);
  }       

     
     /**
      * output:
      * in else branch
      * b <= 0
      * done
      * 
      */
     @Test
     public void ifelse4() throws Exception{
  	System.out.println("***********ifelse4");
  	String ifbranch = "    print \"in if branch\";\n     if (a > 0) {print \"a>0\";}\n     else {print \"a <= 0\";};";
  	String elsebranch = 	"    print \"in else branch\";\n    if (b > 0) {print \"b>0\";}\n     else {print \"b <= 0\";\n    };";	
  	String input = "class B {\n   def a:int;\n   def b:int;\n   a = 3;\n   b = -4;\n"
  			+ " if (b>0) {\n" + ifbranch + "\n}\n else {\n  " + elsebranch + "\n}; \n print \"done\";  \n}";
  	System.out.println(input);
  	Program program = (Program) parseCorrectInput(input);
  	assertNotNull(program);
  	typeCheckCorrectAST(program);
  	byte[] bytecode = generateByteCode(program);
  	assertNotNull(bytecode);
  	System.out.println("\nexecuting bytecode:");
  	executeByteCode(program.JVMName, bytecode);
  }  
     
     
/** While loops 
 * 
 *      Type checking:  guard is a boolean
 *      
 *      Code generation:
 *      goto L1
 *      L2: body
 *      L1: evaluate guard
 *      IFNE L2
 *      
 */
     
     /** 
      * output:
      * done
      */
     @Test
     public void noIterWhile() throws Exception{
    	 	System.out.println("***********noIterWhile");
    	  	String input = "class C{ while (false) { print 1; }; print \"done\";}";
    	  	System.out.println(input);
    	  	Program program = (Program) parseCorrectInput(input);
    	  	assertNotNull(program);
    	  	typeCheckCorrectAST(program);
    	  	byte[] bytecode = generateByteCode(program);
    	  	assertNotNull(bytecode);
    	  	System.out.println("\nexecuting bytecode:");
    	  	executeByteCode(program.JVMName, bytecode);   	 
     }
     
     
     /** 
      * output:
      * 1
      * done
      */
     @Test
     public void oneIterWhile() throws Exception{
    	 	System.out.println("***********oneIterWhile");
    	  	String input = "class C{ def b: boolean; b = true; while (b) { print 1; b = false;}; print \"done\";}";
    	  	System.out.println(input);
    	  	Program program = (Program) parseCorrectInput(input);
    	  	assertNotNull(program);
    	  	typeCheckCorrectAST(program);
    	  	byte[] bytecode = generateByteCode(program);
    	  	assertNotNull(bytecode);
    	  	System.out.println("\nexecuting bytecode:");
    	  	executeByteCode(program.JVMName, bytecode);   	 
     }

     /** 
      * output:
      * 4
      * 3
      * 2
      * 1
      * done
      */
     @Test
     public void fourIterWhile() throws Exception{
    	 	System.out.println("***********fourIterWhile");
    	  	String input = "class C{ def k: int; k = 4; while (k>0) { print k; k=k-1;}; print \"done\";}";
    	  	System.out.println(input);
    	  	Program program = (Program) parseCorrectInput(input);
    	  	assertNotNull(program);
    	  	typeCheckCorrectAST(program);
    	  	byte[] bytecode = generateByteCode(program);
    	  	assertNotNull(bytecode);
    	  	System.out.println("\nexecuting bytecode:");
    	  	executeByteCode(program.JVMName, bytecode);   	 
     }
     
     /**
      * The type checker should throw an exception because the guard, 42, is not boolean
      */
     @Test
     public void whileFail() throws Exception{
     	System.out.println("***********ifelseFail");
   	  	String input = "class C{ def k: int; k = 4; while (k) { print k; k=k-1;}; print \"done\";}";
     	System.out.println(input);
     	Program program = (Program) parseCorrectInput(input);
     	typeCheckIncorrectAST(program);	
     }  
/**
 * Now add functionality to support the list type.  A list declaration looks like
 *     def l1: @[int]   (where int could be replaced by any type, including another list)
 *     
 *     We can assign values to l1:  l1 = @[1,2,3];
 *     We can print a list:
 *     We can get individual values:  l1[0], or example
 *     We can assign to individual values:  l1[0] = 3
 * 
 * 	   A list will be implemented using the java/util/List interface, 
 *     where the concrete implementation is a java/util/ArrayList.
 *     
 *     A consequence of this is that values of (Java's) primitive types must be 
 *     "boxed" before storing in a list;  i.e. rather than storing an int or 
 *     boolean, you need to convert to a java/lang/Integer or java/lang/Boolean 
 *     object, and vice versa.
 *     
 *     Use the valueOf and intValue methods in the Integer class, and the valueOf and
 *     booleanValue methods in the Boolean class.
 *
 *	   Also, the container classes actually hold instances of java/lang/Object. 
 *	   Thus when an item is removed, it must be cast to the correct type.
 *     Use the CLASSCAST instruction.
 *     
 *     In our language, a list must be explicitly initialized by assigning a list value
 *     to it.  
 *     
 *         def l1: @[int];
 *         l1 = @[5,6,7]
 *
 *		A list object is instantiated by the assignment.  Any previous contents of the
 *      list are lost.   Attempts to do anything with
 *      the list before that will fail (probably with null pointer exception).  This is
 *      convenient for us, but not a very satisfactory language design. I enourage you to
 *      think about alternatives.
 *      
 *   
 */

@Test
/** 
 *  In this test, we declare a list l1 which contains int values, and apply the size operator
 *  to the empty list.
 *  
 *  The size expression result type is int and it requires that its argument is a list or map.
 *  It is implemented by invoking the List size function.
 * 
 * output:
 * 0
 */
public void list1() throws Exception{
	System.out.println("***********list1");
	String input = "class B {\ndef l1 : @[int]; \n  l1 = @[];  print  size(l1); \n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);
}



/**
 * This is the same as above, except that the list is nonempty.
 * During type checking, we must ensure that the types of the items in this ListExpression
 * are all the same as the declared element type of the list.
 * 
 * output:
 * 3
 */
@Test
public void list2() throws Exception{
	System.out.println("***********list2");
	String input = "class B {\ndef l1 : @[int]; \n l1 = @[1,2,3];  print size(l1); \n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);
}


/**
 * This list introduces an ListOrMapElemExpression as the argument in the print statement.
 * 
 * For type checking, you need to determine that since l1 is a list, the argument (here 1)
 * must be an int.  The result type is the element type of the list.
 * 
 * output:
 * 2
 */
@Test
public void list3() throws Exception{
	System.out.println("***********list3");
	String input = "class B {\ndef l1 : @[int]; \n l1 = @[1,2,3];   print l1[1]; \n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);
}

/**
 * output:
 * 1
 * 2
 * 42
 */
@Test
public void list4() throws Exception{
	System.out.println("***********list4");
	String input = "class B {\ndef l1 : @[int]; \n l1 = @[1,2,3];  l1[2]=42; print l1[0]; print l1[1]; print l1[2];\n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);
}

/**
 * List contians a list
 * 
 * output
 * 6
 */
@Test
public void list5() throws Exception{
	System.out.println("***********list5");
	String input = "class B {\ndef ll1 : @[@[int]];\n   def l1 : @[int]; def l2 : @[int];"
			+ "def l3: @[int];" 
			+ "\n l1 = @[1,2,3];  " 
			+ "l2 = @[4,5,6]; l3=@[];\n  ll1 = @[l1,l2,l3];" 
			+ "l3 = ll1[1]; \n   print l3[2];"
			+ "\n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);	
}


/**
 * list contains strings
 * 
 * output:
 * go
 * gators
 * chomp!
 */
@Test
public void list6() throws Exception{
	System.out.println("***********list6");
	String input = "class B {\ndef l1 : @[string]; \n l1 = @[\"go\",\"gators\"]; "
			+ "\n l1[2]=\"chomp!\"; "
			+ "\n print l1[0];\n print l1[1];\n print l1[2];"
			+ "\n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);	
}


/**
 * List contains booleans
 * 
 * output:
 * true
 * false
 * true
 */
@Test
public void list7() throws Exception{
	System.out.println("***********list7");
	String input = "class B {\ndef l1 : @[boolean]; \n l1 = @[true, false]; "
			+ "\n l1[2]=true; "
			+ "\n print l1[0];\n print l1[1];\n print l1[2];"
			+ "\n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);	
}

/**
 * This program has a type error, the list elements are not the correct type
 * @throws Exception
 */
@Test
public void listFail1() throws Exception{
	System.out.println("***********listFail1");
	String input = "class B {\ndef l1 : @[boolean]; \n l1 = @[true, 1]; "
			+ "\n l1[2]=true; "
			+ "\n print l1[0];\n print l1[1];\n print l1[2];"
			+ "\n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
 	typeCheckIncorrectAST(program);	
}


/**
 * The print l1["one"] statement has a type error.
 * 
 */
@Test
public void listFail2() throws Exception{
	System.out.println("***********listFail2");
	String input = "class B {\ndef l1 : @[boolean]; \n l1 = @[true, false]; "
			+ "\n l1[2]=true; "
			+ "\n print l1[0];\n print l1[\"one\"];\n print l1[2];"
			+ "\n}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
 	typeCheckIncorrectAST(program);	
}
	
}



