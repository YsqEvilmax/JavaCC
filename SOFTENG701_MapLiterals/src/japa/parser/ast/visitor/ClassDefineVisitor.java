package japa.parser.ast.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import symboltable.ClassSymbol;
import symboltable.InterfaceSymbol;
import symboltable.ScopedSymbol;
import symboltable.Type;

public class ClassDefineVisitor extends TravelVisitor {

	public ClassDefineVisitor() {
		super();
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {	
		currentScope = n.getEnclosingScope();
		currentScope.getEnclosingScope().define((ScopedSymbol)currentScope);
			
		super.visit(n, arg);
	}
}
