package cop5555sp15.symbolTable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import cop5555sp15.ast.Declaration;
import cop5555sp15.symbolTable.SymbolTable.SymbolTableEntry;

public class SymbolTable {
	
	static class SymbolTableEntry{
		public SymbolTableEntry(int scope, Declaration dec,
				SymbolTableEntry next) {
			super();
			this.scope = scope;
			this.dec = dec;
			this.next = next;
		}
		int scope;
		Declaration dec;
		SymbolTableEntry next;
	}

	HashMap<String, SymbolTableEntry> entries;
	LinkedList<Integer> scopeStack;
	int currentScope;
	
	
	/** 
	 * to be called when block entered
	 * @return size of stack after block is entered.
	 */
	public int enterScope(){
		scopeStack.addFirst(++currentScope);
		return scopeStack.size();
	}
	
	
	/**
	 * leaves scope
	 * @returns size of scope stack at entrance to this method
	 * 
	 * For a block with correctly nested scopes, the value returned from enterScope at the
	 * beginning of the block should match the value returned at the end of the scope.
	 */
	public int leaveScope(){
		int size = scopeStack.size();
		if(size > 0) scopeStack.removeFirst();
		return size;
	}
	
	public boolean insert(String ident, Declaration dec){
		//check for existing entry with same scope
		SymbolTableEntry entry = entries.get(ident);		
		while (entry != null){
			if (entry.scope == currentScope){ return false;}
			entry = entry.next;	
		}
		entries.put(ident, new SymbolTableEntry(currentScope, dec, entries.get(ident)));
		return true;
	}
	
	public Declaration lookup(String ident){
		SymbolTableEntry entry = entries.get(ident);
		if (entry == null) return null;
		SymbolTableEntry tmpEntry;
		for (int i = 0; i < scopeStack.size(); ++i){
			tmpEntry = entry;
			int scope = scopeStack.get(i);
			while (tmpEntry != null && tmpEntry.scope != scope){
				tmpEntry = tmpEntry.next;
			}
			if (tmpEntry != null) return tmpEntry.dec;
		}
		return null;
	}
	
	public SymbolTable() {
		currentScope = 0;
		entries = new HashMap<String, SymbolTableEntry>();
		scopeStack = new LinkedList<Integer>();
		scopeStack.addFirst(currentScope); //push
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("scope stack: (top first)\n");
		for (int scopeNum: scopeStack){
			sb.append(scopeNum).append('\n');
		}
		sb.append("entries:\n");
		Set<Entry<String, SymbolTableEntry>>  mapEntrySet = entries.entrySet();
		for (Entry<String, SymbolTableEntry> mapEntry: mapEntrySet){
			sb.append(mapEntry.getKey()).append(':');
			SymbolTableEntry entry = mapEntry.getValue();
			while (entry != null){
				sb.append('[').append(entry.scope).append(',').append(entry.dec.toString()).append("] ");
				entry = entry.next;
			}
			sb.append('\n');
		}
		return sb.toString();
	}

}
