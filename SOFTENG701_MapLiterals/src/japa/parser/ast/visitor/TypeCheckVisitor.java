package japa.parser.ast.visitor;

import japa.parser.ast.Node;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MapInitializationExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import se701.A2SemanticsException;
import symboltable.BuiltInTypeSymbol;
import symboltable.ClassSymbol;
import symboltable.MethodSymbol;
import symboltable.Scope;
import symboltable.Symbol;
import symboltable.Type;
import symboltable.VariableSymbol;

public class TypeCheckVisitor extends TravelVisitor {
	
	protected void checkType(Type target, Type resolved, Node n){
		String t = target.getName();
		String r =resolved.getName();
		if (!t.equals(r)) {
			throw new A2SemanticsException("Error occurs on line "
					+ n.getBeginLine() + " : "
					+ "type " + r
					+ " and type "
					+ t + " are not matched on line "
					+ n.getBeginLine() + ".");
		}
	}
	
	protected void checkType(Symbol target, Symbol resolved, Node n)
	{
		String t = target.getName();
		String r =resolved.getName();
		if (!t.equals(r)) {
			throw new A2SemanticsException("Error eccours on line "
					+ n.getBeginLine() + " : " 
					+ r + " defined on line " 
					+ resolved.getDefinedLine()
					+ " does not have a valid type of "
					+ t + " defined on line "
					+ target.getDefinedLine() + ".");
		}
	}
	
	private void argHandle(LiteralExpr n, Object arg){
		if(arg instanceof MethodSymbol){
			arg = ((MethodSymbol)arg).getType();
		}
		if(arg instanceof Type){
			checkType((Type)arg, n.getType().castType(), n);
		}
	}
	
	protected void argHandle(MethodCallExpr n, Object arg){
		if(arg instanceof Symbol){
			checkType((Symbol)arg, currentScope.resolve(n.getName()), n);
		}
	}
	
	protected void argHandle(MapInitializationExpr n, Object arg){	
		if(arg instanceof MethodSymbol){
			arg = ((MethodSymbol)arg).getType();
		}
		if(arg instanceof Type){
			if(((Type)arg).getName().equals("Map")){
				arg = new BuiltInTypeSymbol("HashMap");
			}
			if(!((Type)arg).getName().equals("HashMap")){
				throw new A2SemanticsException("Error eccours on line "
						+ n.getBeginLine() + " :" 
						+ " can not apply types other than Map or HashMap.");
			}
			checkType((Type)arg, n.getType().castType(), n);
		}
	}

	public TypeCheckVisitor() {
		super();
	}
	
	@Override
	public void visit(AssignExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getTarget(), arg);
		if(n.getTarget() instanceof NameExpr){
			arg = currentScope.resolve(((NameExpr)n.getTarget()).getName());
			if(((Symbol)arg)!=null){
				arg = ((Symbol)arg).getType();
			}
		}
		this.travel(n.getValue(), arg);
	}
	
	@Override
	public void visit(NameExpr n, Object arg) {
		super.visit(n, arg);	
		Symbol s = currentScope.resolve(n.getName());
		if(s instanceof VariableSymbol){
			if(s.getType() instanceof ClassSymbol){
				s = (ClassSymbol)s.getType();
				currentScope = ((Scope) s);
			}		
		}
	}
	
	@Override
	public void visit(StringLiteralExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(IntegerLiteralExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(LongLiteralExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(CharLiteralExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(DoubleLiteralExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(BooleanLiteralExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}

	@Override
	public void visit(NullLiteralExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}
	
	@Override
	public void visit(MethodCallExpr n, Object arg) {
		super.visit(n, arg);
		Symbol resolved = currentScope.resolve(n.getName());
		if(!(resolved instanceof MethodSymbol)){
			resolved = null;
		}
		checkExistance(resolved, n.getName(), n);
		argHandle(n, arg);	
	}
	
	@Override
	public void visit(MapInitializationExpr n, Object arg) {
		super.visit(n, arg);
		argHandle(n, arg);	
	}
}
