grammar MiniJavaGrammar;
@header {  
    package boris.tserinher;
}

// Syntax Specification ==> Context-free Grammar
start : program ;
     
program : mainClass (classDeclaration)*;

mainClass : CLASS ID '{' mainMethod '}';

classDeclaration : CLASS ID '{' classBody '}'; 

classBody : field* method*;

field : type ID SC;

method : (type|methodType) ID ('('parametersList')' | '()')  '{'methodBody'}';

//access : 'public'?; 

methodBody : field* statement* returnStatement;

methodInvocation: expression(',' expression)*;

parametersList : parameter (',' parameter)*;

parameter : type ID;

mainMethod : 'public' 'static' 'void' 'main' '(' 'String'('...' | '[' ']') ID ')' '{' mainMethodBody'}';

mainMethodBody : statement;

statement:   variableDeclarationStatement
	| assignmentStatement 
	| ifStatment 
	| whileStatement 
	| doWhileStatement 
	| printStatement 
	| codeBlockStatement
	| arrayAssignStatements
	| breakeStatement
	| continueStatement;

variableDeclarationStatement: type ID SC; //type ID ('=' expression)? SC;

assignmentStatement: type? ID '=' expression SC;

ifStatment: 'if' rBExpr statement ('else' statement )? ;

whileStatement: 'while' rBExpr statement;

doWhileStatement: 'do' statement 'while' rBExpr SC;

printStatement: 'System.out.println' rBExpr SC;

returnStatement: ('return' expression SC)?;

codeBlockStatement: '{' statement* '}';

arrayAssignStatements: ID'[' expression ']' '=' expression SC;

breakeStatement: BREAK;

continueStatement: CONTINUE;
     
type : intType| booleanType | charType | stringType | intArrayType | identifierType;
methodType: voidType;
intType : 'int';
booleanType: 'boolean';
charType: 'char';
stringType: 'String';
intArrayType: 'int[]';
identifierType: ID;
voidType: 'void';


expression :  //rBExpr #roundBracketxpression
 'this' #thisExpression
| rBExpr #roundBracketxpression
| expression '.' ID ('('methodInvocation?')' | '()')* #methodCallExpression
| '!' expression #notExpression
| expression MULT expression #multExpression
| expression DIV expression  #divExpression
| expression PLUS expression #plusExpression
| expression MINUS expression #minusExpression
| expression '<' expression  #lessExpression
| expression '==' expression #equalExpression
| expression '&&' expression #andExpression
| expression '||' expression #orExpression
| (PLUS|MINUS)? INT #prePlusMinusIntegerExpression
| ID #IDExpression
| 'new' ID '()' #newObjectExpression
| BOOLEAN #boolTypeExpression
| expression (('[' expression ']'('.length')?) | '.length') #arrayExpression
| 'new' 'int' '[' expression ']'('.length')? #newArrayExpression
| STRING #stringExpression
| CHAR #charExpression
| NULL #nullExpression;

rBExpr : LRB expression* RRB ;   // Round bracket expression

// Lexer Specification ==> Regulat Expressions
CLASS : 'class';
PLUS : '+' ;
MINUS : '-' ;
MULT : '*' ;
DIV : '/' ;
LRB : '(' ;           
RRB : ')' ;
INT : ('0'..'9')+ ;
BOOLEAN	: 'true' 
| 'false';
CHAR: '\'' .  '\'';
STRING: '"' .*? '"';
THIS : 'this';
ID  : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;



BREAK: 'break;';
CONTINUE: 'continue;'; 
SC : ';' ;
LINE_COMMENT : '//' ~[\r\n]* -> skip;
COMMENT : '/*' .*? '*/' -> skip;
WS : [ \t\r\n]+ -> skip ;
NULL : 'null';
//SIGN : (PLUS|MINUS)?;

