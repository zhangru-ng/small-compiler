package cop5555sp15;

import java.util.List;

/** This class illustrates calling execute twice, initial 
 * int, list, string variables inside the program, and 
 * use "get" methods in CodeletBuilder to get their value
 * and print out  
 * The expected output is
first time
1
0
second time
2
3
*/

public class Example2 {
	
	public static void main(String[] args) throws Exception{		
		String source = "class Example2{\n"
				+ "def i2: int;\n"
				+ "def b2: boolean;\n"
				+ "def s2: string;\n"
				+ "def l2: @[int];\n"				
				+ "if (b2 == false){\n"
				+ "s2 = \"first time\";\n"
				+ "i2 = 1;\n"
				+ "l2 = @[];\n"
				+ "}\n"
				+ "else {\n"
				+ "s2 = \"second time\";\n"
				+ "i2 = 2;\n"
				+ "l2 = @[1, 2, 3];\n"
				+ "};\n"
				+ "}";
		Codelet codelet = CodeletBuilder.newInstance(source);		
		CodeletBuilder.setBoolean(codelet, "b2", false);
		codelet.execute();
		System.out.println(CodeletBuilder.getString(codelet, "s2"));
		System.out.println(CodeletBuilder.getInt(codelet, "i2"));
		@SuppressWarnings("rawtypes")
		List l2 = CodeletBuilder.getList(codelet, "l2");
		System.out.println(l2.size());
		
		CodeletBuilder.setBoolean(codelet, "b2", true);
		codelet.execute();
		System.out.println(CodeletBuilder.getString(codelet, "s2"));
		System.out.println(CodeletBuilder.getInt(codelet, "i2"));		
		l2 = CodeletBuilder.getList(codelet, "l2");
		System.out.println(l2.size());
			
	}
	
}