package boris.tserinher.SymbolTable;

import boris.tserinher.Records.Record;

public interface SymbolTable {
	
	public void enterScope();
	
	public void exitScope();
	
	public void putRecord(String key, Record record);
	
	public Record lookup(String key);
		
	public void printTable(); // Diagnostics
	
	public void resetTable();// After each traversal
		

}
