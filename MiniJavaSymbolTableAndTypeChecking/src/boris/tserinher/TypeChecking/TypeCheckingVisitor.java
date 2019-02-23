package boris.tserinher.TypeChecking;

import boris.tserinher.MiniJavaGrammarBaseVisitor;
import boris.tserinher.MiniJavaGrammarParser.ClassDeclarationContext;
import boris.tserinher.MiniJavaGrammarParser.FieldContext;
import boris.tserinher.MiniJavaGrammarParser.MainClassContext;
import boris.tserinher.MiniJavaGrammarParser.MainMethodContext;
import boris.tserinher.MiniJavaGrammarParser.MethodContext;
import boris.tserinher.MiniJavaGrammarParser.PlusExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.StartContext;
import boris.tserinher.Records.Record;
import boris.tserinher.SymbolTable.MiniJavaSymbolTable;

public class TypeCheckingVisitor extends MiniJavaGrammarBaseVisitor<Record> {
	
	private MiniJavaSymbolTable mjSymbolTable;
	private String currentScope; //for debug
	
	public TypeCheckingVisitor(MiniJavaSymbolTable mjSymbolTable) {
		//TODO может передавать таблицу не в конструктором, а сетером
		this.mjSymbolTable = mjSymbolTable;
	}
	
	@Override
	public Record visitStart(StartContext ctx) {
		// TODO Auto-generated method stub
		mjSymbolTable.enterScope(); // Enter scope "Program", here storing all classes
		currentScope = mjSymbolTable.getCurrentScopeName();
		System.out.println("Current scope START " + currentScope);
		visit(ctx.getChild(0));
		mjSymbolTable.exitScope();
		
		return null; 
	}

	/*@Override
	public Record visitProgram(ProgramContext ctx) {
		// TODO Auto-generated method stub
		mjSymbolTable.enterScope();
		//System.out.println("program " + mjSymbolTable.getCurrentScopeObj());
		System.out.println(ctx.getChildCount());
		int programClassesCounter = ctx.getChildCount();
		//mjSymbolTable.putRecord(id, record);
		for(int i = 0; i < programClassesCounter; i++){
			visit(ctx.getChild(i));
		}	
		currentScope = mjSymbolTable.getCurrentScopeName();
		System.out.println("Current scope " + currentScope);
		return null; //super.visitProgram(ctx);
	}*/

	@Override
	public Record visitMainClass(MainClassContext ctx) {
		// TODO Auto-generated method stub
		mjSymbolTable.enterScope();
		System.out.println("ENTER MAIN CLASS " + mjSymbolTable.getCurrentScopeName() + " " + mjSymbolTable.getCurrentScopeType());
		visit(ctx.getChild(3));
		mjSymbolTable.exitScope();
		
		return null; 
	}

	@Override
	public Record visitMainMethod(MainMethodContext ctx) {
		// TODO Auto-generated method stub
		mjSymbolTable.enterScope();
		System.out.println("MAIN METHOD " + mjSymbolTable.getCurrentScopeName() + " " + mjSymbolTable.getCurrentScopeType());
		visit(ctx.getChild(11));
		mjSymbolTable.exitScope();
		
		return null;
	}

	@Override
	public Record visitClassDeclaration(ClassDeclarationContext ctx) {
		// TODO Auto-generated method stub
		System.out.println("Class " + ctx.getChild(1).getText() + " " + mjSymbolTable.getCurrentScopeName() + " " + mjSymbolTable.getCurrentScopeType());
		mjSymbolTable.enterScope();
		visit(ctx.getChild(3));
		mjSymbolTable.exitScope();
		return null; //super.visitClassDeclaration(ctx);
	}
	
	@Override
	public Record visitMethod(MethodContext ctx) {
		// TODO Auto-generated method stub
		
		mjSymbolTable.enterScope();
		System.out.println("MEHTOD " + ctx.getChild(1).getText() + " " + mjSymbolTable.getCurrentScopeType() + " " + mjSymbolTable.getCurrentScopeName());
		
		if(ctx.getChildCount() != 6){
		visit(ctx.getChild(3));
		visit(ctx.getChild(6));
		} else {
			visit(ctx.getChild(4));
			System.out.println("ENTER METHOD BODY " + ctx.getChild(4).getText());
		}
		
		//System.out.println("ENTER METHOD PARAMETRS " + ctx.getChild(3).getText() + " " +  ctx.getChildCount());
		//System.out.println("ENTER METHOD BODY " + ctx.getChild(6).getText());
		
		//visit(ctx.getChild(3));
		//visit(ctx.getChild(6));
		
		mjSymbolTable.exitScope();
		
		return null;
	}

	@Override
	public Record visitField(FieldContext ctx) {
		// TODO Auto-generated method stub
		System.out.println("FIELD " + ctx.getText() + " " + mjSymbolTable.getCurrentScopeName() + " " + mjSymbolTable.getCurrentScopeType());
		return super.visitField(ctx);
	}

	@Override
	public Record visitPlusExpression(PlusExpressionContext ctx) {
		// TODO Auto-generated method stub
		/*System.out.println("PLUS " + ctx.getChild(0).getText());
		String errMsg = "Wrong type in Additative Expression";
		System.out.println("FIRST");
		Record first = visit(ctx.getChild(0));
		System.out.println("TYPE " + first.toString());
		String firstType = visit(ctx.getChild(0)).toString(); // Get first type
		int numChildren = ctx.getChildCount();
		for (int i=2; i<numChildren; i+=2) {
		String type = visit(ctx.getChild(i)).toString();
		if (!type.equals(firstType)){
		//print(errMsg);
			}
		}*/
		return null;//firstType;
		
		//return super.visitPlusExpression(ctx);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
