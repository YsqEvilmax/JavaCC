package japa.parser.ast.visitor;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import symboltable.LocalScope;
import symboltable.MethodSymbol;
import symboltable.Scope;
import symboltable.ScopedSymbol;

public class MethodDefineVisitor extends TravelVisitor {

	public MethodDefineVisitor() {
		super();
	}

	@Override
	public void visit(ConstructorDeclaration n, Object arg) {	
		currentScope = n.getEnclosingScope();
		currentScope.getEnclosingScope().define((ScopedSymbol)currentScope);	
		currentScope = currentScope.getEnclosingScope();
		super.visit(n, arg);
	}
	
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		currentScope.getEnclosingScope().define((ScopedSymbol)currentScope);	
		currentScope = currentScope.getEnclosingScope();
		super.visit(n, arg);
	}
}
