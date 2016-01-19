package cop5555sp15;
/** This class illustrates calling execute twice after modifying the int variable
* in the codelet. The expected output is
first time
0
2
second time
*/
public class CallExecuteTwice {
	
	public static void main(String[] args) throws Exception{
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
	
}