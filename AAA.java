package cop5555sp15;

import java.util.ArrayList;
import java.util.List;


public class AAA {
	List<Integer> places;
	
	
	int a;
	void foo() {
		places = new ArrayList<Integer>();
		places.add(1);
		//places.add(2);places.add(3);
		places.get(0).toString();
//		places.add("b");
//		places.add("C");
		places.add(1, 2);
		
//		System.out.println(places.get(0).intValue());
		
	}
	public static void main(String[] args) {

	}

}
