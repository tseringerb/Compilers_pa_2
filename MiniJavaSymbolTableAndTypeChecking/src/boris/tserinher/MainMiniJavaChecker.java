package boris.tserinher;

import java.io.IOException;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import boris.tserinher.SymbolTable.MiniJavaSymbolTable;
import boris.tserinher.SymbolTable.SymbolTable;
import boris.tserinher.SymbolTable.SymbolTableListener;
import boris.tserinher.TypeChecking.TypeCheckingVisitor;

public class MainMiniJavaChecker {

	public static void main(String[] args) {
		
		String testProgram = args[0];
		
		// Parse input program
		MiniJavaGrammarLexer lexer = null;
		try {
			lexer = new MiniJavaGrammarLexer( new ANTLRFileStream(testProgram) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MiniJavaGrammarParser parser = new MiniJavaGrammarParser(new BufferedTokenStream(lexer));		 
		MiniJavaGrammarParser.StartContext tree = parser.start();

		
		// Display tree
		Trees.inspect(tree, parser);
		
		// Indented tree print using a listener
		MiniJavaSymbolTable miniJavaSymbolTable = new MiniJavaSymbolTable();
		ParseTreeWalker walker = new ParseTreeWalker();
		SymbolTableListener listener = new SymbolTableListener(miniJavaSymbolTable);
		walker.walk(listener, tree);
		miniJavaSymbolTable.printTable();
		//System.out.println("Done!");
		
		miniJavaSymbolTable.resetTable();
		TypeCheckingVisitor checkingVisitor = new TypeCheckingVisitor(miniJavaSymbolTable);
		checkingVisitor.visit(tree);
		if(checkingVisitor.getErrorCounter() != 0){
			System.out.printf("Program faild with %d error\\s\n", checkingVisitor.getErrorCounter());
		} else {
			System.out.println("Programm success");
		}
		
	}

}
