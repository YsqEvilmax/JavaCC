package japa.parser.ast.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import japa.parser.ast.Node;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import se701.A2SemanticsException;
import symboltable.ClassSymbol;
import symboltable.InterfaceSymbol;
import symboltable.MethodSymbol;
import symboltable.Scope;
import symboltable.ScopedSymbol;
import symboltable.Symbol;
import symboltable.Type;
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
	
	private void dealClass(ClassOrInterfaceDeclaration n, Object arg){
		currentScope = n.getEnclosingScope();
		ClassSymbol symbol = (ClassSymbol)currentScope.resolveType(n.getName());
		List<Type> parameters = new ArrayList<Type>();
		if (n.getTypeParameters() != null) {
			for (Iterator<TypeParameter> i = n.getTypeParameters().iterator(); i
					.hasNext();) {
				TypeParameter p = i.next();		
				parameters.add(currentScope.resolveType(p.getName()));
				p.accept(this, arg);
			}
		}
		symbol.setParameterTypes(parameters);
		
		List<ClassSymbol> superCs = new ArrayList<ClassSymbol>();
		if (n.getExtends() != null) {
			for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType p = i.next();		
				superCs.add((ClassSymbol) currentScope.resolveType(p.getName()));
				p.accept(this, arg);
			}
		}
		symbol.setSuperclasses(superCs);
		
		List<InterfaceSymbol> supersIs = new ArrayList<InterfaceSymbol>();
		if (n.getImplements() != null) {
			for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType p = i.next();		
				supersIs.add((InterfaceSymbol) currentScope.resolveType(p.getName()));
				p.accept(this, arg);
			}
		}
		symbol.setInterfaces(supersIs);	
	}
	
	private void dealInterface(ClassOrInterfaceDeclaration n, Object arg){
		currentScope = n.getEnclosingScope();
		InterfaceSymbol symbol = (InterfaceSymbol)currentScope.resolveType(n.getName());
		List<Type> parameters = new ArrayList<Type>();
		if (n.getTypeParameters() != null) {
			for (Iterator<TypeParameter> i = n.getTypeParameters().iterator(); i
					.hasNext();) {
				TypeParameter p = i.next();		
				parameters.add(currentScope.resolveType(p.getName()));
				p.accept(this, arg);
			}
		}
		symbol.setParameterTypes(parameters);
		
		
		List<InterfaceSymbol> supersIs = new ArrayList<InterfaceSymbol>();
		if (n.getImplements() != null) {
			for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType p = i.next();		
				supersIs.add((InterfaceSymbol) currentScope.resolveType(p.getName()));
				p.accept(this, arg);
			}
		}
		symbol.setSuperinterfaces(supersIs);	
	}

	public ClassCheckVisitor() {
		super();
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {		
		currentScope = n.getEnclosingScope();
		if(n.isInterface()){
			dealInterface(n, arg);
		}else{
			dealClass(n, arg);
		}
		super.visit(n, arg);
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
