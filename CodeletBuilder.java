package cop5555sp15;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.List;

import cop5555sp15.TestCodeGenerationAssignment5.DynamicClassLoader;
import cop5555sp15.ast.ASTNode;
import cop5555sp15.ast.CodeGenVisitor;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.TypeCheckVisitor;
import cop5555sp15.symbolTable.SymbolTable;

public class CodeletBuilder {

	public static Codelet newInstance(String source) throws Exception{
		ASTNode ast = parseInput(source);
		checkType(ast);
    	byte[] bytecode = generateByteCode(ast);
		DynamicClassLoader loader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
	    Class<?> testClass = loader.define( ((Program)ast).JVMName, bytecode);
		return (Codelet) testClass.newInstance();
	}
	
	public static Codelet newInstance(File file) throws Exception {	
		FileReader fr = new FileReader(file);
		ASTNode ast = parseInput(fr);
		checkType(ast);
    	byte[] bytecode = generateByteCode(ast);
		DynamicClassLoader loader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
	    Class<?> testClass = loader.define( ((Program)ast).JVMName, bytecode);
		return (Codelet) testClass.newInstance();
	}

	private static ASTNode parseInput(String source) {
		TokenStream stream = new TokenStream(source);
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
	
	private static ASTNode parseInput(FileReader fr) {
		TokenStream stream = new TokenStream(fr);
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

	private static ASTNode checkType(ASTNode ast)  {
		SymbolTable symbolTable = new SymbolTable();
		TypeCheckVisitor v = new TypeCheckVisitor(symbolTable);
		try {
			ast.visit(v, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail("Type checking error");
		}
		return ast;
	}	
	
	private static byte[] generateByteCode(ASTNode ast) {
		CodeGenVisitor v = new CodeGenVisitor();
		byte[] bytecode = null;
		try {
			bytecode = (byte[]) ast.visit(v, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail("Code generation error");
		}
		return bytecode;
	}

	@SuppressWarnings("rawtypes")
	public static List getList(Codelet codelet, String name) throws Exception{
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field Field = codeletClass.getDeclaredField(name);
		Field.setAccessible(true);
		List l = (List) Field.get(codelet);
		return l;
	}
	
	public static int getInt(Codelet codelet, String name) throws Exception{
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field Field = codeletClass.getDeclaredField(name);
		Field.setAccessible(true);
		int i = (int) Field.get(codelet);
		return i;
	}
	
	public static void setInt(Codelet codelet, String name, int value) throws Exception{
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field Field = codeletClass.getDeclaredField(name);
		Field.setAccessible(true);
		Field.set(codelet, value);
	}
	
	public static String getString(Codelet codelet, String name) throws Exception{
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field Field = codeletClass.getDeclaredField(name);
		Field.setAccessible(true);
		String s = (String) Field.get(codelet);
		return s;
	}

	
	public static void setString(Codelet codelet, String name, String value) throws Exception{
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field Field = codeletClass.getDeclaredField(name);
		Field.setAccessible(true);
		Field.set(codelet, value);
	}
	
	public static boolean getBoolean(Codelet codelet, String name) throws Exception{
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field Field = codeletClass.getDeclaredField(name);
		Field.setAccessible(true);
		boolean b = (boolean) Field.get(codelet);
		return b;
	}
	
	public static void setBoolean(Codelet codelet, String name, boolean value) throws Exception{
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field Field = codeletClass.getDeclaredField(name);
		Field.setAccessible(true);
		Field.set(codelet, value);
	}
}