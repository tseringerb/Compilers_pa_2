package boris.tserinher.TypeChecking;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import boris.tserinher.MiniJavaGrammarBaseVisitor;
import boris.tserinher.MiniJavaGrammarParser.AssignmentStatementContext;
import boris.tserinher.MiniJavaGrammarParser.BoolTypeExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.BreakeStatementContext;
import boris.tserinher.MiniJavaGrammarParser.ClassDeclarationContext;
import boris.tserinher.MiniJavaGrammarParser.ContinueStatementContext;
import boris.tserinher.MiniJavaGrammarParser.DivExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.DoWhileStatementContext;
import boris.tserinher.MiniJavaGrammarParser.FieldContext;
import boris.tserinher.MiniJavaGrammarParser.IDExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.LessExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MainClassContext;
import boris.tserinher.MiniJavaGrammarParser.MainMethodContext;
import boris.tserinher.MiniJavaGrammarParser.MethodBodyContext;
import boris.tserinher.MiniJavaGrammarParser.MethodCallExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MethodContext;
import boris.tserinher.MiniJavaGrammarParser.MethodInvocationContext;
import boris.tserinher.MiniJavaGrammarParser.MinusExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MultExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.NewObjectExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.PlusExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.PrePlusMinusIntegerExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.RBExprContext;
import boris.tserinher.MiniJavaGrammarParser.ReturnStatementContext;
import boris.tserinher.MiniJavaGrammarParser.StartContext;
import boris.tserinher.MiniJavaGrammarParser.StatementContext;
import boris.tserinher.MiniJavaGrammarParser.ThisExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.WhileStatementContext;
import boris.tserinher.Records.ClassRecord;
import boris.tserinher.Records.MethodRecord;
import boris.tserinher.Records.Record;
import boris.tserinher.Records.VarRecord;
import boris.tserinher.SymbolTable.MiniJavaSymbolTable;

public class TypeCheckingVisitor extends MiniJavaGrammarBaseVisitor<Record> {

	private MiniJavaSymbolTable mjSymbolTable;
	private String currentScope; // for debug
	private String notDeclareError = "Not decalre identifire";
	private static int ErrorCounter = 0;

	public int getErrorCounter() {
		return ErrorCounter;
	}

	public TypeCheckingVisitor(MiniJavaSymbolTable mjSymbolTable) {
		// TODO может передавать таблицу не в конструктором, а сетером
		this.mjSymbolTable = mjSymbolTable;
	}

	private void printError(ParserRuleContext ctx, String message) {
		System.out.printf("----> In line %s: %s\n", ctx.getStart().getLine(), message);
		ErrorCounter++;
	}

	private Record checkMathExpr(ParserRuleContext ctx, String errMsg) {

		System.out.println("MATH " + ctx.getText());
		Record first = visit(ctx.getChild(0));
		String firstType = first.getType(); // Get first type
		String firstId = first.getId();
		int numChildren;

		System.out.println("FIRST " + firstId + " " + firstType);
		numChildren = ctx.getChildCount();
		for (int i = 2; i < numChildren; i += 2) {
			Record rec = visit(ctx.getChild(i));
			/*
			 * if (rec==null) {
			 * 
			 * System.out.println("Nulltype"); }
			 */
			String type = rec.getType();
			if (!type.equals(firstType)) {
				printError(ctx, errMsg);
			}
		}

		// String firstType = ""; // Get first type
		// String firstId = "";

		Record mathRecord = new Record(firstType, firstType);
		System.out.println("MATH RECORD RET " + mathRecord);
		return mathRecord;// firstType;
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

	/*
	 * @Override public Record visitProgram(ProgramContext ctx) { // TODO
	 * Auto-generated method stub mjSymbolTable.enterScope();
	 * //System.out.println("program " + mjSymbolTable.getCurrentScopeObj());
	 * System.out.println(ctx.getChildCount()); int programClassesCounter =
	 * ctx.getChildCount(); //mjSymbolTable.putRecord(id, record); for(int i = 0; i
	 * < programClassesCounter; i++){ visit(ctx.getChild(i)); } currentScope =
	 * mjSymbolTable.getCurrentScopeName(); System.out.println("Current scope " +
	 * currentScope); return null; //super.visitProgram(ctx); }
	 */

	@Override
	public Record visitMainClass(MainClassContext ctx) {
		// TODO Auto-generated method stub
		mjSymbolTable.enterScope();
		System.out.println(
				"ENTER MAIN CLASS " + mjSymbolTable.getCurrentScopeName() + " " + mjSymbolTable.getCurrentScopeType());
		visit(ctx.getChild(3));
		mjSymbolTable.exitScope();

		return null;
	}

	@Override
	public Record visitMainMethod(MainMethodContext ctx) {
		// TODO Auto-generated method stub
		mjSymbolTable.enterScope();
		System.out.println(
				"MAIN METHOD " + mjSymbolTable.getCurrentScopeName() + " " + mjSymbolTable.getCurrentScopeType());
		visit(ctx.getChild(11));
		mjSymbolTable.exitScope();

		return null;
	}

	@Override
	public Record visitClassDeclaration(ClassDeclarationContext ctx) {
		// TODO Auto-generated method stub
		System.out.println("Class " + ctx.getChild(1).getText() + " " + mjSymbolTable.getCurrentScopeName() + " "
				+ mjSymbolTable.getCurrentScopeType());
		mjSymbolTable.enterScope();
		visit(ctx.getChild(3));
		mjSymbolTable.exitScope();
		return null; // super.visitClassDeclaration(ctx);
	}

	@Override
	public Record visitField(FieldContext ctx) {
		// TODO Auto-generated method stub
		// System.out.println("FIELD " + ctx.getText() + " " +
		// mjSymbolTable.getCurrentScopeName() + " " +
		// mjSymbolTable.getCurrentScopeType());
		return (Record) super.visitField(ctx);
	}

	@Override
	public Record visitPlusExpression(PlusExpressionContext ctx) {
		// TODO Auto-generated method stub
		// System.out.println("PLUS " + ctx.getText());
		String errMsg = "Wrong type in Additative Expression";

		Record first = visit(ctx.getChild(0));
		String firstType = first.getType(); // Get first type
		String firstId = first.getId();
		// System.out.println("FIRST " + firstId);
		/*
		 * f(!firstId.equals("int")){ //по идее нужно проверить на все зарезервировынне
		 * слова для типов if(mjSymbolTable.lookup(firstId) == null){ printError(ctx,
		 * notDeclareError); } } //System.out.println("TYPE " + first.toString()); int
		 * numChildren = ctx.getChildCount(); //System.out.println("LINE " +
		 * ctx.getStart().getLine() + " " + ctx.getStart().getCharPositionInLine()); for
		 * (int i=2; i<numChildren; i+=2) { String type =
		 * visit(ctx.getChild(i)).getType(); if (!type.equals(firstType)){
		 * printError(ctx, errMsg); } } Record plusRecord = new Record(firstType,
		 * firstType);
		 */
		if (firstType.equals("String") || firstType.equals("Char")) {
			int numChildren = ctx.getChildCount();
			// System.out.println("LINE " + ctx.getStart().getLine() + " " +
			// ctx.getStart().getCharPositionInLine());
			for (int i = 2; i < numChildren; i += 2) {
				String type = visit(ctx.getChild(i)).getType();
				if (!type.equals(firstType)) {
					printError(ctx, errMsg);
				}
			}
		} else
			return checkMathExpr(ctx, errMsg);// firstType;

		return super.visitPlusExpression(ctx);
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
	public Record visitPrePlusMinusIntegerExpression(PrePlusMinusIntegerExpressionContext ctx) {
		// TODO Auto-generated method stub
		System.out.println("INT EXPRESSION " + ctx.getText());
		Record intRecord = new Record(ctx.getText(), "int");
		return intRecord;
	}

	@Override
	public Record visitIDExpression(IDExpressionContext ctx) {
		// TODO Auto-generated method stub
		// System.out.println("ID EXPRESSION " + ctx.getText());
		String errMesg = "Not declare identifire";
		String id = ctx.getText();

//		mjSymbolTable.printTable();
//		System.out.println("look for "+ id);

		Record idRecord = mjSymbolTable.lookup(id);

		if (idRecord == null) {
			printError(ctx, errMesg);
			System.out.println(id);
		}
		return idRecord;
	}

	@Override
	public Record visitRBExpr(RBExprContext ctx) {
		System.out.println("INSIDE RBE");
		Record result = visit(ctx.getChild(1));
		return result;
	}

	@Override
	public Record visitBoolTypeExpression(BoolTypeExpressionContext ctx) {
		System.out.println("INSIDE BOOLTYPE");
		// TODO Auto-generated method stub
		return super.visitBoolTypeExpression(ctx);
	}

	@Override
	public Record visitStatement(StatementContext ctx) {
		System.out.println("VISITING STMT");
		// TODO Auto-generated method stub
		return super.visitStatement(ctx);
	}

	@Override
	public Record visitThisExpression(ThisExpressionContext ctx) {
		// mjSymbolTable.getCurrentScopeObj();
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
			} else if (!whileArg.getType().equalsIgnoreCase("boolean")) {
				System.out.println("While stmt not bool, it is " + whileArg.getType());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.visitWhileStatement(ctx);
	}

	public Record visitLessExpression(LessExpressionContext ctx) {
		System.out.println("VISITING LESS EXPR");
		Record arg1 = visit(ctx.getChild(0));
		Record arg2 = visit(ctx.getChild(2));
		if (arg1 == null || arg2 == null) {
			printError(ctx, "NPE on less expression");
			return null;
		} else if (!arg1.getType().equalsIgnoreCase("int") || !arg1.getType().equalsIgnoreCase("int")) {
			printError(ctx, "args of less node must be int");
			return null;
		}
		return new Record(arg1.getId() + "<" + arg2.getId(), "boolean");
	}

	@Override
	public Record visitAssignmentStatement(AssignmentStatementContext ctx) {
		// TODO Auto-generated method stub

		String errMsg = "Wrong type in Assignment Statement";
		System.out.println("VISIT ASSIGN " + ctx.getText());
		System.out.println(ctx.getChild(0).getText());
		String firstValId = ctx.getChild(0).getText();
		String secondValId = null;
		// visit(ctx.getChild(0));
		Record firstRec = mjSymbolTable.lookup(firstValId);
		Record secondRec = visit(ctx.getChild(2));
		if (firstRec != null && secondRec != null) {
			firstValId = firstRec.getType();
			secondValId = secondRec.getType();
		}

		if (!firstValId.equals(secondValId)) {
			printError(ctx, errMsg);
		}
		return null;
	}

	@Override
	public Record visitBreakeStatement(BreakeStatementContext ctx) {
		if (!checkBreakContinue(ctx)) {
			printError(ctx, "breake is used incorrectly");
		}
		return super.visitBreakeStatement(ctx);
	}

	@Override
	public Record visitContinueStatement(ContinueStatementContext ctx) {
		if (!checkBreakContinue(ctx)) {
			printError(ctx, "continue is used incorrectly");
		}
		return super.visitContinueStatement(ctx);
	}

	private boolean checkBreakContinue(ParserRuleContext parserRuleContext) {
		if (parserRuleContext.getParent() instanceof MethodBodyContext) {
			return false;
		}
		if ((parserRuleContext.getParent() instanceof WhileStatementContext)
				|| (parserRuleContext.getParent() instanceof DoWhileStatementContext)) {
			return true;
		}
		return checkBreakContinue(parserRuleContext.getParent());
	}

	/*
	 * @Override public Record visitAssignmentStatement(AssignmentStatementContext
	 * ctx) { System.out.println("VISIT ASSIGN " + ctx.getText()); int args =
	 * ctx.getChildCount(); if (args == 5) { String type =
	 * visit(ctx.getChild(0)).toString(); String typeSt =
	 * visit(ctx.getChild(3)).getType(); if (!type.equalsIgnoreCase(typeSt)) {
	 * System.out.println("Error on assign"); return null; } } else if (args == 4) {
	 * Record variable = visit(ctx.getChild(0)); String variableAssign =
	 * visit(ctx.getChild(2)).getType(); if (variable == null ||
	 * !variable.getType().equalsIgnoreCase(variableAssign)) {
	 * System.out.println("Error on assign"); return null; } } return null; }
	 */

	@Override
	public Record visitMethodCallExpression(MethodCallExpressionContext ctx) {
		/*
		 * int count = ctx.getChildCount(); for (int i = 0; i < count; i++) { Record rec
		 * = visit(ctx.getChild(i)); if (rec != null) { //
		 * System.out.println("--------in method was found " + rec.getId()); // } else {
		 * // System.out.println("--------not found " + ctx.getChild(i).toString()); //
		 * } // }
		 */
//		
		String ident = ctx.getChild(2).toString();
		Record record = mjSymbolTable.lookup(ident);
		MethodRecord methodRecord = null;

		if (record == null) {
			try {
				ClassRecord callingObject = (ClassRecord) visit(ctx.getChild(0));
				methodRecord = callingObject.getMethodRecord(ident);
			} catch (Exception e) {
				printError(ctx, "It seems that method does not exist");
				return super.visitMethodCallExpression(ctx);
			}
		} else {
			methodRecord = (MethodRecord) record;
		}
		System.out.println("CALLING METHOD " + ident + " OF TYPE " + methodRecord.getType());
		MethodRecord method = (MethodRecord) methodRecord;
		List<Record> methodArgs = method.getParametersList();

		if (ctx.getChildCount() >= 5) {
			MethodRecord args = (MethodRecord) visit(ctx.getChild(4));

			List<Record> args1 = args.getParametersList();

			if (args1.size() != methodArgs.size()) {
				printError(ctx,
						"Wrong amount of args, we have " + args1.size() + " but there should be " + methodArgs.size());
			} else {
				for (int i = 0; i < methodArgs.size(); i++) {
					if (!methodArgs.get(i).getType().equalsIgnoreCase(args1.get(i).getType())) {
						printError(ctx, "Wrong argument type");
					}
				}
			}
		} else if (!methodArgs.isEmpty()) {
			printError(ctx, "Wrong amount of args for empty stmt");
		}
		return methodRecord;
	}

//	@Override
//	  public Record visitMethodCallExpression(MethodCallExpressionContext ctx) {
//		// TODO Auto-generated method stub
//		System.out.println("METHOD CALL " + ctx.getChild(2).getText() + " " + ctx.getChildCount());
//		String callMethodName = ctx.getChild(2).getText();
//		MethodRecord callMethod = (MethodRecord) mjSymbolTable.lookup(callMethodName);
//		MethodRecord currentMethod;
//		System.out.println("CALL METHOD " + callMethod);
//		// Object object = new String();
//
//		if (ctx.getChildCount() == 4) {
//		} else {
//			currentMethod = (MethodRecord) visit(ctx.getChild(4));
//			System.out.println("~~~~~~~~~~~~~~~~~~~~");
//			// String newString = (String)object;
//			System.out.println("PARAM LIST " + currentMethod.getParametersList().size());
//			currentMethod.printParametersList();
//			callMethod.printParametersList();
//
//			if (callMethod.getParametersList().size() != currentMethod.getParametersList().size()) {
//				printError(ctx, "Wrong quantity of parametrs");
//			} else {
//
//			}
//
//		}
//		// System.out.println("METHOD PARAM" + ctx.getChild(4).getText());
//
//	    return null;//super.visitMethodCallExpression(ctx);
//	  }

	@Override
	public Record visitNewObjectExpression(NewObjectExpressionContext ctx) {
		String objectType = ctx.getChild(1).toString();
		Record rec = mjSymbolTable.lookup(objectType);
		return rec;
	}

	@Override
	public Record visitMethod(MethodContext ctx) {
		// TODO Auto-generated method stub
		mjSymbolTable.enterScope();
		String errMessage = "Wrong return type in method";
		String returnType;
		String methodType = ctx.getChild(0).getText();
		int returnPosition;
		if (ctx.getChildCount() == 6) {
			returnPosition = ctx.getChild(4).getChildCount() - 1;
			visit(ctx.getChild(4));
			returnType = visit(ctx.getChild(4).getChild(returnPosition)).getType();
		} else {
			returnPosition = ctx.getChild(6).getChildCount() - 1;
			visit(ctx.getChild(3));
			visit(ctx.getChild(6));
			returnType = visit(ctx.getChild(6).getChild(returnPosition)).getType();
		}

		if (!returnType.equals(methodType)) {
			printError(ctx, errMessage);
		}

		mjSymbolTable.exitScope();

		return null;
	}

	@Override
	public Record visitReturnStatement(ReturnStatementContext ctx) {
		Record returnRec = visit(ctx.getChild(1));
		return returnRec;
	}

	@Override
	public Record visitMethodInvocation(MethodInvocationContext ctx) {
		// TODO Auto-generated method stub
		System.out.println("METHOD INVOK PARAM " + ctx.getText() + " " + ctx.getChildCount());
		int paramCounter = ctx.getChildCount();
		Record parameterRecord = null;
		MethodRecord returnMethodRec = new MethodRecord("", "");
		for (int i = 0; i < paramCounter; i += 2) {
			parameterRecord = visit(ctx.getChild(i));
			returnMethodRec.putParameterRecord(parameterRecord);
		}

		return returnMethodRec;
	}
}
