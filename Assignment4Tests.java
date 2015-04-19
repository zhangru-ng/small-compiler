package cop5555sp15;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
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

public class Assignment4Tests {
	
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
		if (ast == null) {
			System.out.println("errors " + parser.getErrors());
		}
		assertNotNull(ast);
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
	
    public void executeByteCode(String name, byte[] bytecode) throws InstantiationException, IllegalAccessException{
        DynamicClassLoader loader = new DynamicClassLoader(Thread
                .currentThread().getContextClassLoader());
        Class<?> testClass = loader.define(name, bytecode);
        Codelet codelet = (Codelet) testClass.newInstance();
        codelet.execute();
    }
   


	
@Test
/**
 * generates and executes code to prints an int literal
 * The TypeCheckVisitor should set the type of the IntLitExpression to intType
 * intType is a constant defined in TypeConstants.java
 * 
 * This test should pass in the assignment as givin. 
 * 
 * @throws Exception
 * 
 * output: 
 * 3
 */
public void printIntLiteral() throws Exception{
	System.out.println("***********printIntLiteral");
	String input = "class A {\n print 3; \n}";
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
 * generates and executes code to print a boolean literal
 * The TypeCheckVisitor should set the type of the IntLitExpression to booleanType
 * booleanType is a constant defined in TypeConstants.java
 * @throws Exception
 * 
 * output:
 * true  
 */
public void printBooleanLiteral() throws Exception{
	System.out.println("***********printIntLiteral");
	String input = "class A {\n print true; \n}";
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
 * generates and executes code to print a String literal
 * The TypeCheckVisitor should set the type of the StringLitExpression to stringType
 * stringType is a constant defined in TypeConstants.java
 * @throws Exception
 * 
 * output:
 * go gators
 */
public void printStringLiteral() throws Exception{
	System.out.println("***********printStringLiteral");
	String input = "class A {\n print \"go gators\" ; \n}";
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
 * The following set of tests have to do with binary expressions.  The type checker
 * should ensure that both operand expressions are the same type, and set the type
 * of the result expression appropriately.  It is convenient to use the JVM type
 * as the representation for types.  It is also convenient for the visit method of
 * Expressions (and subclasses) to return the type.
 * 
 * Legal combinations of types  and operators and the 
 * result types are given below.  List and map types will be treated later.
 * 
 *      int (+ | - | * | /) int 				-> int
 *      string + string         				-> string
 *      int (== | != | < | <= | >= | >) int     -> boolean
 *      string (== | !=) string       			-> boolean
 *      
 */



@Test
/**
 * Generates and executes code to print the value of several integer expressions
 * The type checker should set the type of each expression to intType
 * @throws Exception
 * 
 * output:
 * 8
 * 14
 * 1
 * 3
 */
public void intBinaryOps() throws Exception{
	System.out.println("***********intBinaryOps");
	String input = "class A {\n print 3+5;\n print 7*2;\n  print 5-4;\n  print 6/2;\n}";
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
 * Generates and executes code to print the value of several integer expressions with nested expressions
 * @throws Exception
 * 
 * output:
 * 4
 * 16
 * -5
 * 2
 */
public void intBinaryOps2() throws Exception{
	System.out.println("***********intBinaryOps2");
	String input = "class A {\n print (3+5)/2;\n print (7*2)+(6/3);\n  print 5-4-6;\n  print 6/(2+1);\n}";
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
 * Generates and executes code to concatenate two strings and print the result
 * The type checker should set the type of each expression to stringType.  Note
 * that plus is overloaded.  You code generator needs to check the type to know
 * what kind of code to generate.
 * 
 * @throws Exception
 * 
 * output:
 * go**gators
 * 
 * Hint: One way to do this is to generate the calls to
 * create a StringBuilder object, invoke append, and then toString
 * The other is to write a little java method to concatenate
 * two strings and return the result and invoke that.  You
 * might want to create a class called RuntimeSupport or similar
 * to hold all the static methods you use this way.
*/
public void stringConcat() throws Exception{
	System.out.println("***********stringConcat");
	String input = "class A {\n print \"go\" + \"***\" + \"gators\";\n}";
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
 * boolean binary op  & 
 * 
 * The type checker should ensure that both arguments are boolean and
 * set the result type to boolean.
 * 
 * These are equivalent to && and || in c and java.  Probably the
 * easiest way to do this is to invoke a static Java method, but I'll
 * go ahead and explain how to use asmifier to figure this out.
 * 
 * I wrote a little program containing 		boolean e = x && y;
 * where x and y are static booleans. The corresponding asmified bytecode
 * is as follows:
 *
mv.visitFieldInsn(GETSTATIC, "asm_tests/TestClass", "x", "Z");
Label l1 = new Label();
mv.visitJumpInsn(IFEQ, l1);
mv.visitFieldInsn(GETSTATIC, "asm_tests/TestClass", "y", "Z");
mv.visitJumpInsn(IFEQ, l1);
mv.visitInsn(ICONST_1);
Label l2 = new Label();
mv.visitJumpInsn(GOTO, l2);
mv.visitLabel(l1);
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
mv.visitInsn(ICONST_0);
mv.visitLabel(l2);
mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {Opcodes.INTEGER});
mv.visitVarInsn(ISTORE, 1);
 * 
 * The non-asmified code is
    GETSTATIC asm_tests/EmptyClass.x : Z
    IFEQ L1
    GETSTATIC asm_tests/EmptyClass.y : Z
    IFEQ L1
    ICONST_1
    GOTO L2
   L1
   FRAME SAME
    ICONST_0
   L2
   FRAME SAME1 I
    ISTORE 1
    
    
 * In this code, the mv.visitFieldInsn instructions load the value
 * of the arguments on the stack.  In our code generation we will
 * visit the expressions to generate the necessary code to leave the
 * value of the expressions on the stack.  The ISTORE 1 at the
 * end implements the assignment.  Since our goal is to generate code
 * to leave the value on top of the stack, we will omit this, too.
 * 
 *  ASM makes it convenient to handle labels. 
 *  Before you refer to a label, create a label object
 *  Label l1 = new Label();
 *  Now you can use that in instructions.  For example
 *  mv.visitJumpInsn(IFEQ, l1);
 *  Immediately before the instruction where the label should go, 
 *  visit the label
 *  mv.visitLabel(l1);
 * 
 * Make sure to create a new Label object for each label in your code.
 * 
 * The FRAME SAME, FRAME SAME1 I  instructions provide information to
 * the JVM about what should be on the stack at the target of a jump.
 * Although you see visitFrame instructions in the asmified code, you
 * can omit these from your code generation.  Because we created our
 * ClassWriter with the COMPUTE_FRAMES flag, asm will automatically
 * handle this for us.
 * 
 * Getting back to the problem:  We see that the implementation of &&
 * in java is
 *  evaluate the left expression and leave its value on the stack
    IFEQ L1
    evaluate the right expression and leave its value on the stack
    IFEQ L1
    ICONST_1
    GOTO L2
   L1
    ICONST_0
   L2
   .....
   
 *  So what happens is evaluate left expression.  If it is  0, jump
 *  to L1 which loads 0 onto the stack.  Otherwise, evaluate the
 *  right expression.  If it is 0, jump to L1, which loads 0 onto the
 *  stack.  Otherwise load 1 onto the stack and jump to L2, which is
 *  past the load 0 instruction.
 *  
 *  In your code generator, you will want something like
 *  
 *  	public Object visitBinaryExpression(BinaryExpression binaryExpression,
			Object arg) throws Exception {
		MethodVisitor mv = ((Argument) arg).mv;
		Kind op = binaryExpression.op.kind;
		switch (op) {
		case AND: {
			binaryExpression.expression0.visit(this, arg);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			binaryExpression.expression1.visit(this,arg);
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitInsn(ICONST_1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			mv.visitLabel(l1);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l2);
		}
		break;
		.....
		
		
*
*  output:
*  false
*  false
*  false
*  true
 */
public void and() throws Exception{
	System.out.println("***********and");
	String input = "class A {\n print false & false;\n print false & true;\n  print true & false;\n  print true & true;\n}";
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
 * boolean binary op  | 
 * 
 * Similar to previous test.  The type checking rules are the same.
 * 
 * output:
 * false
 * true
 * true
 * true 
 * 
 */
public void or() throws Exception{
	System.out.println("***********or");
	String input = "class A {\n print false | false;\n print false | true;\n  print true | false;\n  print true | true;\n}";
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
 * The next set of tests involves relational expressions.
 * 
 * This tests == for ints.  The type checker should ensure that both operands are ints
 * and set the result to boolean.
 * 
 * output:
 * true
 * false
 */
public void intEqual() throws Exception{
	System.out.println("***********intEqual");
	String input = "class A {\n print 3 == (1+2); print 3 == 4; \n}";
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
 *
 * This tests != for ints.  The type checker should ensure that both operands are ints
 * and set the result to boolean.
 * 
 * output:
 * false
 * true
 */
public void intNotEqual() throws Exception{
	System.out.println("***********intEqual");
	String input = "class A {\n print 3 != (1+2); print 3 != 4; \n}";
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
/** equal and notequal for booleans.  
 * 
 * output:
 * false
 * true
 * false
 * true
 */
public void booleanRelational() throws Exception{
	System.out.println("***********booleanRelational");
	String input = "class A {\n print false != false; print false != true; print false == true; print true == true; \n}";
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
/** equal and not equal for strings.
 * This requires using the equals method rather than comparing values
 * 
 * output:
 * false
 * true
 * true
 * false
 */
public void stringRelational() throws Exception{
	System.out.println("***********stringRelational");
	String input = "class A {\n print \"hello\" != \"hello\";\n   print \"hello\" != \"goodbye\";\n   print \"good\" == \"good\";\n    print \"bad\" == \"good\"; \n }" ;
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
/** relational expressions on ints
 * The easiest way is to write a little java program and use
 * asmifier to see how to do this.  But note that if you try to
 * compiler 3<3, the compiler will optimize that out and just
 * store 0.    
 * 
 * output:
 * false
 * true
 * false
 * true
 */
public void intRelational1() throws Exception{
	System.out.println("***********intRelational1");
	String input = "class A {\n print 3 < 3;\n   print 3 <= 3;\n   print 3>3;\n    print 3>=3; \n }" ;
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
 * output:
 * true
 * false
 * true
 * false
 */
public void intRelational2() throws Exception{
	System.out.println("***********intRelational2");
	String input = "class A {\n print 2 < 3;\n   print 4 <= 3;\n   print 4>3;\n    print 2>=3; \n }" ;
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);
}

/** The next (incomplete) set of tests have typing errors and
 * are expected to throw a type check error.  They should work
 * without extra coding if you have properly implemented type
 * checking for the features of the previous cases.
 */

@Test
/** This test is expected to throw a TypeCheckException
 * 
 * @throws Exception
 */
public void binaryExpressionFail1() throws Exception{
	System.out.println("***********binaryExpressionFail1");
	String input = "class A {\n  print true + 1;}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	typeCheckIncorrectAST(program);	
}
@Test
/** This test is expected to throw a TypeCheckException
 * 
 * @throws Exception
 */
public void binaryExpressionFail2() throws Exception{
	System.out.println("***********binaryExpressionFail2");
	String input = "class A {\n  print true + \"hello\";}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	typeCheckIncorrectAST(program);	
}

@Test
/** This test is expected to throw a TypeCheckException
 * 
 * @throws Exception
 */
public void binaryExpressionFail3() throws Exception{
	System.out.println("***********binaryExpressionFail2");
	String input = "class A {\n  print 2 & 3;}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	typeCheckIncorrectAST(program);	
}



@Test
/** outer scope variables for simple types mapped into fields
 * with assignments.  
 * 
 * Type checking:
 * 
 * Declaration:  insert variable and its declaration in to the  symbol
 *    table making sure to check that it has not already been defined
 *    in this scope.
 *    
 * Use:  lookup variable in symbol table, make sure it has been declared
 *    in the current scope, and get its type.
 * 
 * When an expression is visited, determine its type and store the
 * type in the field in the Expression
 * 
 * In assignment statement, ensure that both sides have compatible types.
 * 
 * Code generation:
 * 
 * Declaration:  
 *   For now, we will only declare variables in the outer scope and
 *   implement them as fields.
 *   
 *   call cw.visitField to add a field for the variable to the class
 *   fv = cw.visitField(0, varName, type, null, null);
 *     fv.visitEnd();
 * 
 * 
 * Assignment Statement
 * 	 only do the simple types for now (lValue instance of IdentLValue)
 *       gen code to load "this" reference on the stack
 *       visit the expression (which gens code to put value on stack)
 *       gen putfield
 *   
 *   If the expression is an IdentExpression
 *       gen code to load "this" reference onto stack
 *       gen getfield 
 *   this code leaves the value of the ident on the stack
 *   
 *   
 *   output:
 *   3
 */
public void intVariable() throws Exception{
	System.out.println("***********intVariable");
	String input = "class A {def x: int; \n x = 3;\n  print x;}";
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
/*  similar to above except with a boolean.  
 * 
 * output:
 * false
 */
public void booleanVariable() throws Exception{
	System.out.println("***********intVariable");
	String input = "class A {def x: boolean; \n x = false;\n  print x;}";
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
/* A more complicated program.  This should pass if the previous
 * steps have been done correctly.
 * 
 * output:
x=
43
b=
true
 */
public void expressonsAndVars() throws Exception{
	System.out.println("***********expressonsAndVars");
	String input = "class A {def x: int; \n def b: boolean;";
	input = input + "x = 42;\n   x = x + 1;\n   b = (x > 100) | (x == 43);\n  ";
			input = input + "print \"x=\";\n  print x;\n print \"b=\"; print b;}";
	System.out.println(input);
	Program program = (Program) parseCorrectInput(input);
	assertNotNull(program);
	typeCheckCorrectAST(program);
	byte[] bytecode = generateByteCode(program);
	assertNotNull(bytecode);
	System.out.println("\nexecuting bytecode:");
	executeByteCode(program.JVMName, bytecode);
}

}

