package cop5555sp15.symbolTable;

import static org.junit.Assert.*;

import org.junit.Test;

import cop5555sp15.ast.Declaration;
import cop5555sp15.ast.DummyDec;
import cop5555sp15.ast.VarDec;

public class TestSymbolTable {

	@Test
	public void emptyTable() {
		SymbolTable symtab = new SymbolTable();
		Declaration dec = symtab.lookup("dummy");
		assertNull(dec);
	}

	@Test
	public void oneDecIdent() {
		SymbolTable symtab = new SymbolTable();
		assertTrue(symtab.insert("ident", new DummyDec("ident","ident")));
		Declaration dec =  symtab.lookup("dummy");
		assertNull(dec);
		dec= symtab.lookup("ident");
		assertNotNull(dec);
		assertEquals("ident", dec.toString());
	}
	
	@Test 
	public void scope1(){
		SymbolTable symtab = new SymbolTable();
		symtab.enterScope();
		assertTrue(symtab.insert("ident",new DummyDec("ident","ident")));
		symtab.leaveScope();
		Declaration dec = symtab.lookup("ident");
		assertNull(dec);
	}		
	
	@Test 
	public void scope2(){
		SymbolTable symtab = new SymbolTable();
		symtab.enterScope();
		assertTrue(symtab.insert("ident",new DummyDec("ident","ident")));
		Declaration dec = symtab.lookup("ident");
		assertNotNull(dec);
		assertEquals("ident", dec.toString());
		symtab.leaveScope();
		Declaration dec2 = symtab.lookup("ident");
		assertNull(dec2);
	}	
	
	@Test 
	public void scope3(){
		SymbolTable symtab = new SymbolTable();
		symtab.enterScope();
		assertTrue(symtab.insert("ident",new DummyDec("ident","ident")));
		//attempt to insert identifier twice in same scope
		assertFalse(symtab.insert("ident",new DummyDec("ident","ident_duplicate")));
		Declaration dec = symtab.lookup("ident");
		assertNotNull(dec);
		assertEquals("ident", dec.toString());
		symtab.leaveScope();
		Declaration dec2 = symtab.lookup("ident");
		assertNull(dec2);
	}	
	
	@Test //insert identifier twice in different scopes
	public void scope4(){
		SymbolTable symtab = new SymbolTable();
		symtab.enterScope();
		assertTrue(symtab.insert("ident", new DummyDec("ident","ident_tok1")));
		symtab.enterScope();
		assertTrue(symtab.insert("ident", new DummyDec("ident","ident_tok2")));
		System.out.println("symbol table in inner scope");
		System.out.println(symtab);
		Declaration dec2 = symtab.lookup("ident");
		assertNotNull(dec2);
		assertEquals("ident_tok2", dec2.toString());
		symtab.leaveScope();
		System.out.println("symbol table in outer scope");
		System.out.println(symtab);
		Declaration dec1 = symtab.lookup("ident");
		assertNotNull(dec1);
		assertEquals("ident_tok1", dec1.toString());
		symtab.leaveScope();
		System.out.println("symbol table in global scope");
		System.out.println(symtab);
		Declaration dec3= symtab.lookup("ident");
		assertNull(dec3);		
	}	
	
	@Test //insert identifier twice in different scopes
	public void scope5(){
		SymbolTable symtab = new SymbolTable();
		symtab.enterScope();//1 0
		assertTrue(symtab.insert("ident", new DummyDec("ident","ident_tok1")));
		symtab.enterScope();//2 1 0
		assertTrue(symtab.insert("ident", new DummyDec("ident","ident_tok2")));
		System.out.println("symbol table in inner scope");
		System.out.println(symtab);
		Declaration dec2 = symtab.lookup("ident");
		assertNotNull(dec2);
		assertEquals("ident_tok2", dec2.toString());
		symtab.leaveScope();//1 0
		System.out.println("symbol table in outer scope");
		System.out.println(symtab);
		Declaration dec1 = symtab.lookup("ident");
		assertNotNull(dec1);
		assertEquals("ident_tok1", dec1.toString());
		symtab.enterScope(); //3 1 0
		assertTrue(symtab.insert("ident2", new DummyDec("ident","ident2_tok1")));
		symtab.enterScope(); // 4 3 1 0
		Declaration dec3 = symtab.lookup("ident2");	
		assertNotNull(dec3);
		assertEquals("ident2_tok1", dec3.toString());		
		symtab.leaveScope(); //3 1 0
		symtab.leaveScope(); //1 0
		symtab.leaveScope(); //0
		System.out.println(symtab);
		System.out.println("symbol table in global scope");
		System.out.println(symtab);
		Declaration dec4= symtab.lookup("ident");
		assertNull(dec4);	
		Declaration dec5= symtab.lookup("ident2");
		assertNull(dec5);			
	}		
	
}