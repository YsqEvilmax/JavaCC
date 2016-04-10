package japa.parser.ast.visitor;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import se701.A2SemanticsException;
import symboltable.MethodSymbol;
import symboltable.Symbol;
import symboltable.Type;

public class MethodCheckVisitor extends TypeCheckVisitor {
	
	protected void checkReturn(MethodSymbol m){
		if(!m.getHasReturn()){
			throw new A2SemanticsException("Error occurs on line "
					+ m.getDefinedLine() + " : "
					+ "method " + m.getName()
					+ " (on line " + m.getDefinedLine()
					+ ") should return a vlaue of type " 
					+ m.getType().getName() + "!");
		}
	}
	
	protected void loadReturn(MethodSymbol m, Type returnType){
		if(returnType instanceof Type){
			m.setType(returnType);
		}
		else{
			throw new A2SemanticsException("Error occurs on line " 
					+ m.getDefinedLine() + " : "
					+ "method " + m.getName()
					+ " (on line " + m.getDefinedLine()
					+ ") should have a valid return type!");
		}
	}

	public MethodCheckVisitor() {
		super();
	}
	
	@Override
	public void visit(ConstructorDeclaration n, Object arg) {	
		currentScope = n.getEnclosingScope();
		MethodSymbol methodSymbol = (MethodSymbol)currentScope.resolve(n.getName());	
		Type returnType = currentScope.resolveType(n.getName());
		loadReturn(methodSymbol, returnType);
		methodSymbol.setHasReturn(true);//return is not necessary for constructor
		currentScope = currentScope.getEnclosingScope();
		super.visit(n, arg);
		currentScope = n.getEnclosingScope();
		checkReturn(methodSymbol);
		currentScope = currentScope.getEnclosingScope();
	}
	
	@Override
	public void visit(BlockStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		if(n.getStmts() != null){
			for(Statement t : n.getStmts()){
				if(!(t instanceof ReturnStmt)){
					arg = null;
				}
				travel(t, arg);
			}
		}
		currentScope = currentScope.getEnclosingScope();
	}
	
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();	
		MethodSymbol methodSymbol = (MethodSymbol)currentScope.resolve(n.getName());
		Type returnType = currentScope.resolveType(n.getType().toString());
		loadReturn(methodSymbol, returnType);
		if (methodSymbol.getType().getName() == "void") {		
			methodSymbol.setHasReturn(true);//return is not necessary for void
		}	
		currentScope = currentScope.getEnclosingScope();
		super.visit(n, arg);
		currentScope = n.getEnclosingScope();
		checkReturn(methodSymbol);
		currentScope = currentScope.getEnclosingScope();
	}
	
	@Override
	public void visit(ReturnStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		MethodSymbol theMethod = (MethodSymbol)arg;
		theMethod.setHasReturn(true);
			
		if (theMethod.getType().getName() == "void" && n.getExpr() != null) {
			throw new A2SemanticsException("Error occurs on line "
					+ n.getBeginLine() + " : "
					+ "method " + theMethod.getName()
					+ " (on line " + theMethod.getDefinedLine()
					+ ") should return void, check the return statement at "
					+ n.getBeginLine() + "!");
		}			
		super.visit(n, arg);
	}
	

	
	//check return  exist? how many, match?
	//

	//callexpr
	//check existance
	//check type match
	//check use before declaredgr
}
