package japa.parser.ast.visitor;

import japa.parser.ast.Node;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import se701.A2SemanticsException;
import symboltable.ClassSymbol;
import symboltable.MethodSymbol;
import symboltable.Scope;
import symboltable.Symbol;
import symboltable.VariableSymbol;

public class ClassCheckVisitor extends TypeCheckVisitor {
	
	private void checkField(String name, Node n){
		if(!(currentScope instanceof ClassSymbol)){
			currentScope = currentScope.find();
		}
		if(!currentScope.isLocal(name)){
			throw new A2SemanticsException("Error occurs on line "
					+ n.getBeginLine() + " : Filed " 
					+ name
				    + " is not accessable in current scope: "
				    + currentScope.getScopeName());
		}
	}

	public ClassCheckVisitor() {
		super();
	}
	
	@Override
	public void visit(FieldAccessExpr n, Object arg) {
		super.visit(n, arg);
		checkField(n.getField(), n);
	}
	
	@Override
	public void visit(MethodCallExpr n, Object arg) {
		super.visit(n, arg);
		checkField(n.getName(), n);	
	}
}
