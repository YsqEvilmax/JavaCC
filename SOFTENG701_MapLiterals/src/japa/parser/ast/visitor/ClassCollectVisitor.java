package japa.parser.ast.visitor;

import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import symboltable.ScopedSymbol;

public class ClassCollectVisitor extends TravelVisitor {

	public ClassCollectVisitor() {
		super();
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {	
		currentScope = n.getEnclosingScope();
		currentScope.getEnclosingScope().define((ScopedSymbol)currentScope);
		currentScope = currentScope.getEnclosingScope();
		super.visit(n, arg);
	}
}
