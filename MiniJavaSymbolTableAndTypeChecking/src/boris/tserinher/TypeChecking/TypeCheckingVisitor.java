package boris.tserinher.TypeChecking;

import org.antlr.v4.runtime.ParserRuleContext;

import boris.tserinher.MiniJavaGrammarBaseVisitor;
import boris.tserinher.MiniJavaGrammarParser.ClassDeclarationContext;
import boris.tserinher.MiniJavaGrammarParser.DivExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.FieldContext;
import boris.tserinher.MiniJavaGrammarParser.IDExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.LessExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MainClassContext;
import boris.tserinher.MiniJavaGrammarParser.MainMethodContext;
import boris.tserinher.MiniJavaGrammarParser.MethodContext;
import boris.tserinher.MiniJavaGrammarParser.MinusExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MultExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.PlusExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.PrePlusMinusIntegerExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.StartContext;
import boris.tserinher.MiniJavaGrammarParser.ThisExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.WhileStatementContext;
import boris.tserinher.Records.Record;
import boris.tserinher.SymbolTable.MiniJavaSymbolTable;

public class TypeCheckingVisitor extends MiniJavaGrammarBaseVisitor<Record> {
	
	private MiniJavaSymbolTable mjSymbolTable;
	private String currentScope; //for debug
	private String notDeclareError = "Not decalre identifire";
	private static int ErrorCounter = 0;
	
	public int getErrorCounter() {
		return ErrorCounter;
	}
	
	public TypeCheckingVisitor(MiniJavaSymbolTable mjSymbolTable) {
		//TODO может передавать таблицу не в конструктором, а сетером
		this.mjSymbolTable = mjSymbolTable;
	}
	
	private void printError(ParserRuleContext ctx, String message){
		System.out.printf("----> In line %s: %s\n", ctx.getStart().getLine(), message);
		ErrorCounter++;
	}
	
	private Record checkMathExpr(ParserRuleContext ctx, String errMsg){
		
		/*System.out.println("MATH " + ctx.getText());
		Record first = visit(ctx.getChild(0));
		String firstType = first.getType(); // Get first type
		String firstId = first.getId();
		System.out.println("FIRST " + firstId + " " + firstType);
		int numChildren = ctx.getChildCount();
		//System.out.println("LINE " + ctx.getStart().getLine() + " " + ctx.getStart().getCharPositionInLine());
		for (int i=2; i<numChildren; i+=2) {
		String type = visit(ctx.getChild(i)).getType();
		if (!type.equals(firstType)){
			printError(ctx, errMsg);
			}
		}*/
		
		String firstType = ""; // Get first type
		String firstId = "";
		 
		Record mathRecord = new Record(firstType, firstType);
		
		return mathRecord;//firstType;
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
		//System.out.println("FIELD " + ctx.getText() + " " + mjSymbolTable.getCurrentScopeName() + " " + mjSymbolTable.getCurrentScopeType());
		return (Record) super.visitField(ctx);
	}

	@Override
	public Record visitPlusExpression(PlusExpressionContext ctx) {
		// TODO Auto-generated method stub
		//System.out.println("PLUS " + ctx.getText());
		String errMsg = "Wrong type in Additative Expression";
		
		/*Record first = (Record)visit(ctx.getChild(0));
		String firstType = first.getType(); // Get first type
		String firstId = first.getId();
		//System.out.println("FIRST " + firstId);
		if(!firstId.equals("int")){ //по идее нужно проверить на все зарезервировынне слова для типов
			if(mjSymbolTable.lookup(firstId) == null){
				printError(ctx, notDeclareError);
				} 
			}
			//System.out.println("TYPE " + first.toString());
		int numChildren = ctx.getChildCount();
		//System.out.println("LINE " + ctx.getStart().getLine() + " " + ctx.getStart().getCharPositionInLine());
		for (int i=2; i<numChildren; i+=2) {
		String type = visit(ctx.getChild(i)).getType();
		if (!type.equals(firstType)){
			printError(ctx, errMsg);
			}
		}
		Record plusRecord = new Record(firstType, firstType);*/
		
		return checkMathExpr(ctx, errMsg);//firstType;
		
		//return super.visitPlusExpression(ctx);
	}
	
	

	@Override
	public Record visitMinusExpression(MinusExpressionContext ctx) {
		String errMsg = "Wrong type in Subtuction Expression";
		// TODO Auto-generated method stub
		return checkMathExpr(ctx, errMsg);
	}

	@Override
	public Record visitDivExpression(DivExpressionContext ctx) {
		String errMsg = "Wrong type in Division Expression";
		
		// TODO Auto-generated method stub
		return checkMathExpr(ctx, errMsg);
	}

	@Override
	public Record visitMultExpression(MultExpressionContext ctx) {
		String errMsg = "Wrong type in Multiplication Expression";
		// TODO Auto-generated method stub
		return checkMathExpr(ctx, errMsg);
	}

	@Override
	public Record visitPrePlusMinusIntegerExpression(
			PrePlusMinusIntegerExpressionContext ctx) {
		// TODO Auto-generated method stub
		//System.out.println("INT EXPRESSION " + ctx.getText());
		Record intRecord = new Record("int", "int");
		return intRecord;
	}

	@Override
	public Record visitIDExpression(IDExpressionContext ctx) {
		// TODO Auto-generated method stub
		//System.out.println("ID EXPRESSION " + ctx.getText());
		String errMesg = "Not declare identifire";
		String id = ctx.getText();
		Record idRecord = mjSymbolTable.lookup(id);
		
		
		
		if(idRecord == null){
			printError(ctx, errMesg);
			System.out.println(id);
		}
		return idRecord;
	}

	@Override
	public Record visitThisExpression(ThisExpressionContext ctx) {
		// TODO Auto-generated method stub
		//System.out.println("THIS");
		return super.visitThisExpression(ctx);
	}
	
	@Override
	 public Record visitWhileStatement(WhileStatementContext ctx) {
	        System.out.println("INSIDE WHILE STM");
	        try {
	      Record whileArg = visit(ctx.getChild(1));
	      System.out.println("whileArg " + ctx.getChild(1).getText());
	      if (whileArg == null) { 
	       System.out.println("NPE on while");
	      } else if(!whileArg.getType().equalsIgnoreCase("boolean")) {
	       System.out.println("While stmt not bool, it is " + whileArg.getType());
	      }
	      
	        } catch (Exception e) {
	         e.printStackTrace();
	        }
	  return null;
	 }
	
	public Record visitLessExpression(LessExpressionContext ctx) {
		  System.out.println("VISITING LESS EXPR");
		  Record arg1 = visit(ctx.getChild(0));
		  Record arg2 = visit(ctx.getChild(2));
		  if (arg1 == null || arg2 == null) {
		   System.out.println("NPE on less expression");
		   return null;
		  }
		  return new Record(arg1.getId() + "<" + arg2.getId(), "boolean");
		 }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
