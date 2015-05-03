package cop5555sp15;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import cop5555sp15.CodeletBuilder;

public class TestCodeGenerationAssignment6 {

	
	@Rule public TestName testname = new TestName();
	
	PrintStream oldPrintStream;
	PrintStream testStream;
	ByteArrayOutputStream baos;
	
	@Before public void before() throws FileNotFoundException{
		baos = new ByteArrayOutputStream();
		testStream = new PrintStream(baos);
		oldPrintStream = System.out;
		System.setOut(testStream);
	}
	
	@After public void after() {
		testStream.close();
		System.setOut(oldPrintStream);
		//System.out.println("solution.put(" + testname.getMethodName() + " , " +  baos.toString() +")");
	}
	
	@Test
	public void referenceTest() throws Exception {
		String source = "class CallExecuteTwice{\n"
				+ "def i1: int;\n"
				+ "if (i1 == 0){print \"first time\";}\n"
				+ "else {print \"second time\";};\n"
				+ "}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		codelet.execute();
		int i1 = CodeletBuilder.getInt(codelet, "i1");
		System.out.println(i1);
		CodeletBuilder.setInt(codelet, "i1", i1+2);
		System.out.println(CodeletBuilder.getInt(codelet, "i1"));
		codelet.execute();
	}
	
	@Test
	public void fourIterWhile() throws Exception {
		String source = "class fourIterWhile{ def k: int; while (k>0) { print k; k=k-1;}; print \"done\";}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setInt(codelet, "k", 4);
		codelet.execute();
		CodeletBuilder.setInt(codelet, "k", 6);
		codelet.execute();
	}
	
	@Test
	public void fourIterWhile2() throws Exception {
		String source = "class fourIterWhile{ def k: int; while (k>0) { print k; k=k-1;}; print \"done\";}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setInt(codelet, "k", 4);
		codelet.execute();
		int k = CodeletBuilder.getInt(codelet, "k");
		CodeletBuilder.setInt(codelet, "k", k+4);
		codelet.execute();
	}
	
	@Test
	public void if1() throws Exception {
		String source = "class if1 { def b: boolean; if (b) { print \"true\"; } else { print \"false\";} ; print \"done\"; }";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setBoolean(codelet, "b", true);
		codelet.execute();
		CodeletBuilder.setBoolean(codelet, "b", false);
		codelet.execute();
	}
	
	@Test
	public void if2() throws Exception {
		String source = "class if1 { def b: boolean; if (b) { print \"true\"; } else { print \"false\";} ; print \"done\"; }";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setBoolean(codelet, "b", true);
		codelet.execute();
		boolean b = CodeletBuilder.getBoolean(codelet, "b");
		CodeletBuilder.setBoolean(codelet, "b", !b);
		codelet.execute();
		CodeletBuilder.setBoolean(codelet, "b", b);
		codelet.execute();
	}
	
	@Test
	public void ifelse1() throws Exception {
		String source = "class ifelse3 { def a:int; def b:int; "
				+ "if (b>0) { "
				+ "print \"in if branch\"; "
				+ "if (a > 0) {print \"a>0\";}" 
				+  "else {print \"a <= 0\";}; "
				+ "}"
				+ "else {"
				+	"print \"in else branch\";"
				+	"if (b > 0) {print \"b>0\";}"
				+	"else {print \"b <= 0\"; };" 
				+ "};"
				+ "print \"done\";"
				+ "}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setInt(codelet, "a", 3);
		CodeletBuilder.setInt(codelet, "b", 4);
		codelet.execute();
		
		CodeletBuilder.setInt(codelet, "a", 4);
		CodeletBuilder.setInt(codelet, "b", -2);
		codelet.execute();
		
		CodeletBuilder.setInt(codelet, "a", -1);
		CodeletBuilder.setInt(codelet, "b", 3);
		codelet.execute();
		
		CodeletBuilder.setInt(codelet, "a", -1);
		CodeletBuilder.setInt(codelet, "b", -2);
		codelet.execute();
	}
	
	@Test
	public void ifelse2() throws Exception {
		String source = "class ifelse3 { def a:int; def b:int; "
				+ "if (b>0) { "
				+ "print \"in if branch\"; "
				+ "if (a > 0) {print \"a>0\";}" 
				+  "else {print \"a <= 0\";}; "
				+ "}"
				+ "else {"
				+	"print \"in else branch\";"
				+	"if (b > 0) {print \"b>0\";}"
				+	"else {print \"b <= 0\"; };" 
				+ "};"
				+ "print \"done\";"
				+ "}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setInt(codelet, "a", 3);
		CodeletBuilder.setInt(codelet, "b", 4);
		codelet.execute();
		
		int a = CodeletBuilder.getInt(codelet, "a");
		int b = CodeletBuilder.getInt(codelet, "b");

		CodeletBuilder.setInt(codelet, "a", a);
		CodeletBuilder.setInt(codelet, "b", -1*b);
		codelet.execute();
		
		CodeletBuilder.setInt(codelet, "a", -1*a);
		CodeletBuilder.setInt(codelet, "b", b);
		codelet.execute();
		
		CodeletBuilder.setInt(codelet, "a", -1*a);
		CodeletBuilder.setInt(codelet, "b", -1*b);
		codelet.execute();
	}
	
//	@Test
//	public void list2() throws Exception {
//		String source = "class list2 { def l1 : @[int]; l1 = @[1,2,3];  print size(l1);}";
//		Codelet codelet = CodeletBuilder.newInstance(source);
//		List l1 = CodeletBuilder.getList(codelet, "l1");
//		codelet.execute();
//		System.out.println(l1);
//	}
	
	@Test
	public void oneIterWhile() throws Exception {
		String source = "class oneIterWhile{ def b: boolean; while (b) { print 1; b = false;}; print \"done\";}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setBoolean(codelet, "b", true);
		codelet.execute();
		CodeletBuilder.setBoolean(codelet, "b", false);
		codelet.execute();
	}
	
	
	@Test
	public void testSetString() throws Exception {
		String source = "class oneIterWhile{ def s: string;  print s;}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setString(codelet, "s", "abc");
		codelet.execute();
	}
	
	@Test
	public void testGetString() throws Exception {
		String source = "class oneIterWhile{ def s: string;  print s;}";
		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setString(codelet, "s", "abc");
		codelet.execute();
		String s = 	CodeletBuilder.getString(codelet, "s");
		Assert.assertEquals("abc", s);

	}
	

}
