package japa.parser.ast.visitor;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.MapInitializationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import se701.A2SemanticsException;

public class VariableCheckVisitor extends TypeCheckVisitor {

	
	public VariableCheckVisitor() {
		super();
	}
	
	@Override
	public void visit(NameExpr n, Object arg) {
		currentScope = n.getEnclosingScope();	
		checkExistance(n.getName(), n);
		checkBeforeDeclaraion(n.getName(), n);
		super.visit(n, arg);
	}
	
	//check existance
	//check type match
	//check use before declaredgr
}
