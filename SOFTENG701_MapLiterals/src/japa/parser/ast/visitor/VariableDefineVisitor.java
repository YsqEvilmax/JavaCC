package japa.parser.ast.visitor;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclaratorId;
import symboltable.Symbol;
import symboltable.Type;
import symboltable.VariableSymbol;

public class VariableDefineVisitor extends TravelVisitor {

	public VariableDefineVisitor() {
		super();
	}

	@Override
	public void visit(VariableDeclaratorId n, Object arg) {
		currentScope = n.getEnclosingScope();
		if(arg instanceof symboltable.Type)
		{
			symboltable.Type t = (symboltable.Type)arg;
			t = currentScope.resolveType(t.getName());
			Symbol s = new VariableSymbol(n.getName(), t);
			s.setDefinedLine(n.getBeginLine());
		    currentScope.define(s);	
		}
		currentScope = currentScope.getEnclosingScope();
		super.visit(n, arg);
	}
}
