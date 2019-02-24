package boris.tserinher.TypeChecking;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import boris.tserinher.MiniJavaGrammarBaseVisitor;
import boris.tserinher.MiniJavaGrammarParser.AndExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.ArrayAssignStatementsContext;
import boris.tserinher.MiniJavaGrammarParser.ArrayExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.AssignmentStatementContext;
import boris.tserinher.MiniJavaGrammarParser.BoolTypeExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.BreakeStatementContext;
import boris.tserinher.MiniJavaGrammarParser.CharExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.ClassDeclarationContext;
import boris.tserinher.MiniJavaGrammarParser.ContinueStatementContext;
import boris.tserinher.MiniJavaGrammarParser.DivExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.DoWhileStatementContext;
import boris.tserinher.MiniJavaGrammarParser.EqualExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.ExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.FieldContext;
import boris.tserinher.MiniJavaGrammarParser.IDExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.IdentifierTypeContext;
import boris.tserinher.MiniJavaGrammarParser.IfStatmentContext;
import boris.tserinher.MiniJavaGrammarParser.LessExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MainClassContext;
import boris.tserinher.MiniJavaGrammarParser.MainMethodContext;
import boris.tserinher.MiniJavaGrammarParser.MethodBodyContext;
import boris.tserinher.MiniJavaGrammarParser.MethodCallExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MethodContext;
import boris.tserinher.MiniJavaGrammarParser.MethodInvocationContext;
import boris.tserinher.MiniJavaGrammarParser.MinusExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.MultExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.NewArrayExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.NewObjectExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.NotExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.OrExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.PlusExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.PrePlusMinusIntegerExpressionContext;
import boris.tserinher.MiniJavaGrammarParser.RBExprContext;
import boris.tserinher.MiniJavaGrammarParser.ReturnStatementContext;
import boris.tserinher.MiniJavaGrammarParser.StartContext;
import boris.tserinher.MiniJavaGrammarParser.StatementContext;
import boris.tserinher.MiniJavaGrammarParser.StringExpressionContext;
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
	private MethodRecord currentMethod;

	public int getErrorCounter() {
		return ErrorCounter;
	}

	public TypeCheckingVisitor(MiniJavaSymbolTable mjSymbolTable) {
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

			String type = rec.getType();
			if (!type.equals(firstType)) {
				printError(ctx, errMsg);
			}
		}

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
		currentMethod = (MethodRecord) mjSymbolTable.lookup("main");
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
		return (Record) super.visitField(ctx);
	}

	@Override
	public Record visitPlusExpression(PlusExpressionContext ctx) {

		String errMsg = "Wrong type in Additative Expression";

		Record first = visit(ctx.getChild(0));
		String firstType = first.getType(); // Get first type
		String firstId = first.getId();

		Record result = null;
		if (firstType.equalsIgnoreCase("String") || firstType.equalsIgnoreCase("Char")) {
			int numChildren = ctx.getChildCount();

			for (int i = 2; i < numChildren; i += 2) {
				String type = visit(ctx.getChild(i)).getType();
				if (!type.equalsIgnoreCase("String") && !type.equalsIgnoreCase("char")) {
					printError(ctx, errMsg);
				}
			}
			return new Record("result", "string");
		} else
			return checkMathExpr(ctx, errMsg);// firstType;
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

		String errMesg = "Not declare identifire";
		String id = ctx.getText();

		Record idRecord = mjSymbolTable.lookup(id);

		// System.out.println("<<<<<IDREC" + idRecord);

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
		System.out.println("INSIDE BOOLTYPE " + ctx.getText());
		// TODO Auto-generated method stub
		Record record = new Record("boolean", "boolean");
		return record; // super.visitBoolTypeExpression(ctx);
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
		String thisType = currentMethod.getContainingClass();
		return new Record(thisType, thisType);// super.visitThisExpression(ctx);
	}

	@Override
	public Record visitWhileStatement(WhileStatementContext ctx) {
		System.out.println("INSIDE WHILE STM");
		try {
			Record whileArg = visit(ctx.getChild(1));
			if (whileArg == null) {
				printError(ctx, "NPE on while");
			} else if (!whileArg.getType().equalsIgnoreCase("boolean")) {
				printError(ctx, "While stmt not bool, it is " + whileArg.getType());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.visitWhileStatement(ctx);
	}

	@Override
	public Record visitDoWhileStatement(DoWhileStatementContext ctx) {
		try {
			Record whileArg = visit(ctx.getChild(3));
			if (whileArg == null) {
				printError(ctx, "NPE on do-while");
			} else if (!whileArg.getType().equalsIgnoreCase("boolean")) {
				printError(ctx, "Do-While stmt not bool, it is " + whileArg.getType());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.visitDoWhileStatement(ctx);
	}

	@Override
	public Record visitIfStatment(IfStatmentContext ctx) {
		try {
			Record ifArg = visit(ctx.getChild(1));
			if (ifArg == null) {
				printError(ctx, "NPE on if");
			} else if (!ifArg.getType().equalsIgnoreCase("boolean")) {
				printError(ctx, "If stmt not bool, it is " + ifArg.getType());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.visitIfStatment(ctx);
	}

	public Record visitLessExpression(LessExpressionContext ctx) {
		System.out.println("VISITING LESS EXPR");
		Record arg1 = visit(ctx.getChild(0));
		Record arg2 = visit(ctx.getChild(2));
		if (arg1 == null || arg2 == null) {
			printError(ctx, "NPE on less expression");
			return null;
		} else if (!arg1.getType().equalsIgnoreCase("int") && !arg2.getType().equalsIgnoreCase("int")
				&& !arg1.getType().equalsIgnoreCase("char") && !arg2.getType().equalsIgnoreCase("char")) {
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
		// mjSymbolTable.lookup(firstValId);
		// String firstValId = visit(ctx.getChild(0)).getType();
		// System.out.println("++++++" + ctx.getChild(0).toString());
		String secondValId = null;
		// visit(ctx.getChild(0));
		Record firstRec = mjSymbolTable.lookup(firstValId);
		Record secondRec = visit(ctx.getChild(2));
		if (firstRec != null && secondRec != null) {
			firstValId = firstRec.getType();
			secondValId = secondRec.getType();
			if ("class".equalsIgnoreCase(secondValId)) {
				secondValId = secondRec.getId();
			}
			if (!firstValId.equalsIgnoreCase(secondValId)) {
				printError(ctx, errMsg);
				System.out.println(
						"!!!!!!! _ >>> " + firstValId + " + " + secondValId + " -> " + ctx.getChild(2).getText());
			}
		} else {
//
//			System.out.println("!!!!!!! _ >>> " + firstRec + " + " + secondRec );
//			System.out.println("!!!!!!! _ >>> " + firstValId + " + " + secondValId + " -> " + ctx.getChild(2).getText());
			printError(ctx, "NPE on assignment");
		}

		/*
		 * mjSymbolTable.printTable(); System.out.println(firstValId +" != "+
		 * secondValId);
		 */

		return null;
	}

	@Override
	public Record visitArrayAssignStatements(ArrayAssignStatementsContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayAssignStatements(ctx);
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

	@Override
	public Record visitMethodCallExpression(MethodCallExpressionContext ctx) {
		String ident = ctx.getChild(2).toString();
		Record record = mjSymbolTable.lookup(ident);
		MethodRecord methodRecord = null;
		currentMethod = (MethodRecord) mjSymbolTable.lookup(ident);

		if (record == null) {
			try {
				Record rec = visit(ctx.getChild(0));
				if (rec instanceof VarRecord) {
					if ("string".equalsIgnoreCase(rec.getType()) && "length".equalsIgnoreCase(ident)) {
						return new MethodRecord(ident, "int");
					} else if ("string".equalsIgnoreCase(rec.getType()) && "charAt".equalsIgnoreCase(ident)) {
						return new MethodRecord(ident, "char");
					} else {
						rec = mjSymbolTable.lookup(rec.getType());
					}
				}
				ClassRecord callingObject = (ClassRecord) rec;
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

			if ((args1.size() != methodArgs.size() && !(ident.equalsIgnoreCase("charAt") && args1.size() == 1))) {
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

	@Override
	public Record visitNewObjectExpression(NewObjectExpressionContext ctx) {
		// System.out.println("~~~~~NEW " + ctx.getChild(1).getText());
		// mjSymbolTable.printTable();
		String objectType = ctx.getChild(1).toString();
		// Record rec = new Record(objectType, objectType);
		ClassRecord rec = (ClassRecord) mjSymbolTable.lookup(objectType);

		return rec;
	}

	@Override
	public Record visitMethod(MethodContext ctx) {
		mjSymbolTable.enterScope();
		String errMessage = "Wrong return type in method";
		String voidErrMessage = "Void methods cannot return a value";
		String emptyReturnErrMessage = "This method must return a result";
		String returnType = "";
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

		if (methodType.equals("void") && !returnType.equals("void")) {
			printError(ctx, voidErrMessage);
		} else if (!methodType.equals("void") && returnType.equals("void")) {
			printError(ctx, emptyReturnErrMessage);
		} else if (!returnType.equals(methodType)) {
			printError(ctx, errMessage);
		}

		mjSymbolTable.exitScope();

		return null;
	}

	@Override
	public Record visitReturnStatement(ReturnStatementContext ctx) {
		System.out.println("RETURN TYPE VISIT: " + ctx.getText());
		Record returnRec = null;
		if (ctx.getText().equals("")) {
			// System.out.println("RETURN VOID !@!!!!!!!!!!");
			returnRec = new Record("void", "void");
		} else {
			returnRec = visit(ctx.getChild(1));
		}
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

	@Override
	public Record visitNotExpression(NotExpressionContext ctx) {
		Record arg1 = visit(ctx.getChild(1));

		if (!checkBool(arg1, ctx)) {
			printError(ctx, "Only boolean maybe usd in ! stmt");
		}
		return new Record("notResult", "boolean");
	}

	@Override
	public Record visitEqualExpression(EqualExpressionContext ctx) {
		Record arg1 = visit(ctx.getChild(0));
		Record arg2 = visit(ctx.getChild(2));

		if (!arg1.getType().equalsIgnoreCase(arg2.getType())) {
			printError(ctx, "Different types cannot be used in == stmt");
		}
		return new Record("equalResult", "boolean");
	}

	@Override
	public Record visitOrExpression(OrExpressionContext ctx) {
		Record arg1 = visit(ctx.getChild(0));
		Record arg2 = visit(ctx.getChild(2));

		if (!checkBool(arg1, ctx) || !checkBool(arg1, ctx)) {
			printError(ctx, "Only boolean maybe usd in || stmt");
		}
		return new Record("orResult", "boolean");
	}

	@Override
	public Record visitAndExpression(AndExpressionContext ctx) {
		Record arg1 = visit(ctx.getChild(0));
		Record arg2 = visit(ctx.getChild(2));

		if (!checkBool(arg1, ctx) || !checkBool(arg1, ctx)) {
			printError(ctx, "Only boolean maybe usd in && stmt");
		}
		return new Record("andResult", "boolean");
	}

	private boolean checkBool(Record rec, ExpressionContext ctx) {
		if (rec == null) {
			printError(ctx, "NPE on bool");
		}
		return "boolean".equalsIgnoreCase(rec.getType());
	}

	@Override
	public Record visitArrayExpression(ArrayExpressionContext ctx) {
		return new Record("intArray[i]", "int");
	}

	@Override
	public Record visitNewArrayExpression(NewArrayExpressionContext ctx) {
		return new Record("intArray", "int[]");
	}

	@Override
	public Record visitIdentifierType(IdentifierTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitIdentifierType(ctx);
	}

	@Override
	public Record visitStringExpression(StringExpressionContext ctx) {
		// TODO Auto-generated method stub
		return new Record("string", "string");
	}

	@Override
	public Record visitCharExpression(CharExpressionContext ctx) {
		// TODO Auto-generated method stub
		return new Record("char", "char");
	}
}
