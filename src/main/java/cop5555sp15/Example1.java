package cop5555sp15;

/** This class illustrates calling execute twice after modifying
 * int, boolean, string, variables in the codelet, and print
 * different element in a list
 * The expected output is
first time
10
1
sencond time
20
2
*/
public class Example1 {

	public static void main(String[] args) throws Exception{
		String source = "class Example1{\n"
						+ "def i1: int;\n"
						+ "def b1: boolean;\n"
						+ "def s1: string;\n"
						+ "def l1: @[int];\n"
						+ "l1 = @[1, 2];\n"
						+ "if (b1 == false){\n"
						+ "print s1;\n"
						+ "print i1;\n"
						+ "print l1[0];\n"
						+ "}\n"
						+ "else {\n"
						+ "print s1;\n"
						+ "print i1;\n"
						+ "print l1[1];\n"
						+ "};\n"
						+ "}";

		Codelet codelet = CodeletBuilder.newInstance(source);
		CodeletBuilder.setInt(codelet, "i1", 10);
		CodeletBuilder.setBoolean(codelet, "b1", false);
		CodeletBuilder.setString(codelet, "s1", "first time");
		codelet.execute();
		CodeletBuilder.setInt(codelet, "i1", 20);
		CodeletBuilder.setBoolean(codelet, "b1", true);
		CodeletBuilder.setString(codelet, "s1", "sencond time");
		codelet.execute();
	}

}