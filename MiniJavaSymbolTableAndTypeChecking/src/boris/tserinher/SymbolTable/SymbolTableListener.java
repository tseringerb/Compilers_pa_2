package boris.tserinher.SymbolTable;

import boris.tserinher.MiniJavaGrammarBaseListener;
import boris.tserinher.MiniJavaGrammarParser.ClassDeclarationContext;
import boris.tserinher.MiniJavaGrammarParser.FieldContext;
import boris.tserinher.MiniJavaGrammarParser.MainClassContext;
import boris.tserinher.MiniJavaGrammarParser.MainMethodContext;
import boris.tserinher.MiniJavaGrammarParser.MethodContext;
import boris.tserinher.MiniJavaGrammarParser.ParameterContext;
import boris.tserinher.MiniJavaGrammarParser.ProgramContext;
import boris.tserinher.MiniJavaGrammarParser.StartContext;
import boris.tserinher.Records.ClassRecord;
import boris.tserinher.Records.MethodRecord;
import boris.tserinher.Records.VarRecord;

public class SymbolTableListener extends MiniJavaGrammarBaseListener {
	
	//TODO ¬сЄ нужно сместить на один Scope выше, Class - должен быть на уровне Csope Program
	// и т.д
	
	private MiniJavaSymbolTable symbolTable;
	private ClassRecord currentClass;
	private MethodRecord currentMethod;
	
	public SymbolTableListener() {
		super();
	}

	public SymbolTableListener(SymbolTable symbolTable) {
		super();
		this.symbolTable = (MiniJavaSymbolTable) symbolTable;
	}

	@Override
	public void enterStart(StartContext ctx) {
		// TODO Auto-generated method stub
		//symbolTable.enterScope();
		System.out.println("START");
		super.enterStart(ctx);
	}

	@Override
	public void exitStart(StartContext ctx) {
		// TODO Auto-generated method stub
		super.exitStart(ctx);
	}

	@Override
	public void enterProgram(ProgramContext ctx) {
		
		symbolTable.enterScope();
		symbolTable.setCurrentScopeType("program");
		symbolTable.setCurrentScopeName("program");
		
		//System.out.println(symbolTable.getCurrentScopeObj());
		
		System.out.println("ENTER PROGRAM " + symbolTable.getCurrentScopeName());
		
		super.enterProgram(ctx);
	}
	
	@Override
	public void exitProgram(ProgramContext ctx) {
		System.out.println("EXIT");
		symbolTable.exitScope();
		super.exitProgram(ctx);
	}

	@Override
	public void enterMainClass(MainClassContext ctx) {
		//TODO
		String id = ctx.getChild(1).toString();
		String type = ctx.getChild(0).toString();
		
		System.out.println(type + " " + id + " " + symbolTable.getCurrentScopeType());
		
		
		currentClass = new ClassRecord(id, type);
		//symbolTable.enterScope();
		symbolTable.putRecord(id, currentClass); //что бы класс лежал на уровне скопа программы
												// его запись необходимо добавл€ть до ентер—копе
		symbolTable.enterScope();
		symbolTable.setCurrentScopeType(type);
		symbolTable.setCurrentScopeName(id);
		
		
		super.enterMainClass(ctx);
	}

	@Override
	public void exitMainClass(MainClassContext ctx) {
		symbolTable.exitScope();
		super.exitMainClass(ctx);
	}
	
	@Override
	public void enterClassDeclaration(ClassDeclarationContext ctx) {
		//TODO
		String id = ctx.getChild(1).toString();
		String type = ctx.getChild(0).toString();
		
		//System.out.println("CLASS DECL LISTENER " + symbolTable.getCurrentScopeName());
		System.out.println(type + " " + id + " " + symbolTable.getCurrentScopeType());
		currentClass = new ClassRecord(id, type);
		
		//symbolTable.enterScope();
		
		symbolTable.putRecord(id, currentClass);
		symbolTable.enterScope();
		symbolTable.setCurrentScopeType(type);
		symbolTable.setCurrentScopeName(id);
		super.enterClassDeclaration(ctx);
	}

	@Override
	public void exitClassDeclaration(ClassDeclarationContext ctx) {
		symbolTable.exitScope();
		super.exitClassDeclaration(ctx);
	}
	
	@Override
	public void enterMainMethod(MainMethodContext ctx) {
		//TODO
		String id = ctx.getChild(3).toString();
		String type = ctx.getChild(2).toString();
		
		System.out.println(type + " " + id + " " + symbolTable.getCurrentScopeType());
		
		//symbolTable.enterScope();
		//symbolTable.setCurrentScopeType("method");
		
		currentMethod = new MethodRecord(id, type);
		currentClass.putMethodRecord(currentMethod);
		symbolTable.putRecord(id, currentMethod);
		//symbolTable.setCurrentScopeName(id);
		
		symbolTable.enterScope();
		symbolTable.setCurrentScopeType("method");
		symbolTable.setCurrentScopeName(id);
		
		super.enterMainMethod(ctx);
	}

	@Override
	public void exitMainMethod(MainMethodContext ctx) {
		symbolTable.exitScope();
		super.exitMainMethod(ctx);
	}	

	@Override
	public void enterMethod(MethodContext ctx) {
		String id = ctx.getChild(1).toString();
		String type = ctx.getChild(0).getChild(0).getChild(0).toString();
		
		System.out.println("METHOD ENTER " + type + " " + id + " " + symbolTable.getCurrentScopeName() + " " + currentClass);
		
		currentMethod = new MethodRecord(id, type);
		
		currentMethod.setContainingClass(currentClass.getId());
		
		currentClass.putMethodRecord(currentMethod);
		
		symbolTable.putRecord(id, currentMethod	);

		symbolTable.enterScope();
		symbolTable.setCurrentScopeType("method");;
		symbolTable.setCurrentScopeName(id);
		super.enterMethod(ctx);
	}

	@Override
	public void exitMethod(MethodContext ctx) {
		symbolTable.exitScope();
		super.exitMethod(ctx);
	}

	@Override
	public void enterField(FieldContext ctx) {
		String id = ctx.getChild(1).toString();
		String type = ctx.getChild(0).getChild(0).getChild(0).toString();
		
		System.out.println("TABLE" + symbolTable.getCurrentScopeName());
				
		VarRecord fieldRecord = new VarRecord(id, type);
		symbolTable.putRecord(id, fieldRecord);
		currentClass.putFieldsRecord(fieldRecord);
		
		super.enterField(ctx);
	}

	@Override
	public void enterParameter(ParameterContext ctx) {
		String id = ctx.getChild(1).toString();
		String type = ctx.getChild(0).getChild(0).getChild(0).toString();
		
		VarRecord parameterRecord = new VarRecord(id, type);		
		symbolTable.putRecord(id, parameterRecord);
		currentMethod.putParameterRecord(parameterRecord);

		super.enterParameter(ctx);
	}
	
	
	
	

}
