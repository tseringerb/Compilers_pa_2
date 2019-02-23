package boris.tserinher.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import boris.tserinher.Records.Record;

public class MiniJavaSymbolTable implements SymbolTable {
	
	private Scope rootScope;
	private Scope currentScope;
	
	public MiniJavaSymbolTable() {
		super();
		this.rootScope = new Scope(null);
		this.currentScope = rootScope;
	}

	public void setCurrentScopeType(String scopeType){
		currentScope.setScopeType(scopeType);
	}
	
	public void setCurrentScopeName(String scopeName){
		currentScope.setScopeName(scopeName);
	}
	
	public String getRootScopeName(){
		return rootScope.getScopeName();
	}
	
	public String getCurrentScopeName(){
		return currentScope.getScopeName();
	}
	
	public String getCurrentScopeType(){
		return currentScope.getScopeType();
	}
	
	public Scope getCurrentScopeObj(){
		return currentScope;
		//TODO
	}
	
	@Override
	public void enterScope() {
		 currentScope = currentScope.nextChild();
	}
		
	@Override
	public void exitScope() {
		currentScope = currentScope.getParentScope();

	}

	@Override
	public void printTable() {
		System.out.println("----------------------------Symbol Table------------------------------");
		System.out.printf("%10s%25s%25s%n", "Id", "Type", "Scope");
		System.out.println("----------------------------------------------------------------------");
		rootScope.printScope();
		System.out.println("-------------------------------END------------------------------------");
	}

	@Override
	public void putRecord(String id, Record record) {
		currentScope.putRecord(id, record);
	}

	@Override
	public Record lookup(String id) {		
		return currentScope.lookup(id);
	}
	
	public void printScope(){
		currentScope.printScope();
	}
	
	@Override
	public void resetTable() {
		rootScope.resetScope();
	}
	
	private class Scope{
		
		private String scopeType;
		private String scopeName;
		private int next = 0; //Next child to visit
		private Scope parentScope; //Parent scope
		private ArrayList<Scope> scopeChildrenList = new ArrayList<>(); //Children scopes
		private LinkedHashMap<String, Record> records = new LinkedHashMap<>();
		
		public Scope(Scope parentScope) {
			this.parentScope = parentScope;
		}
		
		public String getScopeType() {
			return scopeType;
		}

		public void setScopeType(String scopeType) {
			this.scopeType = scopeType;
		}
		
		public String getScopeName() {
			return scopeName;
		}

		public void setScopeName(String scopeName) {
			this.scopeName = scopeName;
		}

		public Scope nextChild() { //Creates new children on demand
			Scope nextChild;
			if (next >= scopeChildrenList.size()) { //Child does not exist
			nextChild = new Scope(this); // ==> create new Scope
			scopeChildrenList.add(nextChild);
			}
			else //Child exists
			nextChild = (Scope) scopeChildrenList.get(next); // ==> visit child
			next++;
			return nextChild;
		}
		
		
		
		public Record lookup(String id) {
			if (records.containsKey(id)) //Check if in current scope
				return (Record) records.get(id);
			else { //Move to enclosing/parent scope
			if (parentScope == null){
					return null; // Identifier not in table
			}
				else
					return parentScope.lookup(id); // Delegate request to enclosing scope
			}
		}
		
		public void resetScope() { // Must be called after each traversal
			next = 0;
			for (int i=0;i < scopeChildrenList.size();i++)
			((Scope)scopeChildrenList.get(i)).resetScope();
		}
		
		public void putRecord(String id, Record record){
			records.put(id, record);
		}

		public Scope getParentScope() {
			return this.parentScope;
		}
		
		public void printScope(){
			records.forEach((id, record)->{
				System.out.printf("%10s%25s%25s%n", id, record.getType(), scopeName + "(" + scopeType +")");
			});
			
			scopeChildrenList.forEach((scope)->{
				scope.printScope();
			});
			
		}
	}
}
