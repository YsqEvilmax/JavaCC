package japa.parser.ast.visitor;

import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MapInitializationExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.SuperMemberAccessExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se701.A2SemanticsException;
import symboltable.ClassSymbol;
import symboltable.EnumSymbol;
import symboltable.GlobalScope;
import symboltable.InterfaceSymbol;
import symboltable.LocalScope;
import symboltable.MethodSymbol;
import symboltable.Scope;
import symboltable.ScopedSymbol;
import symboltable.Symbol;
import symboltable.Type;
import symboltable.VariableSymbol;

public class ScopeCreateVisitor extends TravelVisitor {
	@Override
	public void visit(Node n, Object arg) {
		throw new IllegalStateException(n.getClass().getName());
	}

	@Override
	public void visit(CompilationUnit n, Object arg) {
		GlobalScope globalScope = new GlobalScope();
		currentScope = globalScope;
		n.setEnclosingScope(currentScope);

		super.visit(n, arg);
	}

	@Override
	public void visit(PackageDeclaration n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ImportDeclaration n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(TypeParameter n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(LineComment n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(BlockComment n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		ScopedSymbol symbol = null;
		if (n.isInterface()) {
			symbol = new InterfaceSymbol(n.getName());
			
		} else {
			symbol =  new ClassSymbol(n.getName());
		}
		symbol.setDefinedLine(n.getBeginLine());
		symbol.setEnclosingScope(currentScope);
		currentScope = symbol;
		n.setEnclosingScope(currentScope);
		
//		List<Type> parameters = new ArrayList<Type>();
//		if (n.getTypeParameters() != null) {
//			for (Iterator<TypeParameter> i = n.getTypeParameters().iterator(); i
//					.hasNext();) {
//				TypeParameter p = i.next();		
//				parameters.add(currentScope.resolveType(p.getName()));
//				p.accept(this, arg);
//			}
//		}
//		if (n.isInterface()) {
//			((InterfaceSymbol)symbol).setParameterTypes(parameters);
//			
//		} else {
//			((ClassSymbol)symbol).setParameterTypes(parameters);
//		}
//		
//		List<ClassSymbol> superCs = new ArrayList<ClassSymbol>();
//		if (n.getExtends() != null) {
//			for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i
//					.hasNext();) {
//				ClassOrInterfaceType p = i.next();		
//				superCs.add((ClassSymbol) currentScope.resolveType(p.getName()));
//				p.accept(this, arg);
//			}
//		}
//		if (!n.isInterface()){
//			((ClassSymbol)symbol).setSuperclasses(superCs);
//		}
//		
//		List<InterfaceSymbol> supersIs = new ArrayList<InterfaceSymbol>();
//		if (n.getImplements() != null) {
//			for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i
//					.hasNext();) {
//				ClassOrInterfaceType p = i.next();		
//				supersIs.add((InterfaceSymbol) currentScope.resolveType(p.getName()));
//				p.accept(this, arg);
//			}
//		}
//		if (n.isInterface()){
//			((InterfaceSymbol)symbol).setSuperinterfaces(supersIs);
//		}else{
//			((ClassSymbol)symbol).setInterfaces(supersIs);
//		}

		super.visit(n, symbol);
	}

	@Override
	public void visit(EnumDeclaration n, Object arg) {
		EnumSymbol enumSymbol = new EnumSymbol(n.getName());
		enumSymbol.setEnclosingScope(currentScope);

		currentScope = enumSymbol;
		n.setEnclosingScope(currentScope);

		super.visit(n, arg);
	}

	@Override
	public void visit(EmptyTypeDeclaration n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(EnumConstantDeclaration n, Object arg) {
		LocalScope localScope = new LocalScope("enum");
		localScope.setEnclosingScope(currentScope);

		currentScope = localScope;
		n.setEnclosingScope(currentScope);

		super.visit(n, arg);	
	}

	@Override
	public void visit(AnnotationDeclaration n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(AnnotationMemberDeclaration n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(VariableDeclarator n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(VariableDeclaratorId n, Object arg) {
		n.setEnclosingScope(currentScope);	
		super.visit(n, arg);
	}

	@Override
	public void visit(ConstructorDeclaration n, Object arg) {
		MethodSymbol methodSymbol = new MethodSymbol(n.getName());
		methodSymbol.setDefinedLine(n.getBeginLine());		
		methodSymbol.setEnclosingScope(currentScope);
		currentScope = methodSymbol;
		
		n.setEnclosingScope(currentScope);
	
		List<Type> parameters = new ArrayList<Type>();
		if (n.getParameters() != null) {
			for (Iterator<Parameter> i = n.getParameters().iterator(); i
					.hasNext();) {
				Parameter p = i.next();		
				parameters.add(currentScope.resolveType(p.getType().toString()));
				p.accept(this, arg);
			}
		}
		methodSymbol.setParameters(parameters);

		if (n.getThrows() != null) {
			for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				NameExpr name = i.next();
				name.accept(this, arg);
			}
		}

		n.getBlock().accept(this, methodSymbol);	
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		MethodSymbol methodSymbol = new MethodSymbol(n.getName());
		methodSymbol.setDefinedLine(n.getBeginLine());
		methodSymbol.setEnclosingScope(currentScope);
		currentScope = methodSymbol;
		
		n.setEnclosingScope(currentScope);

		List<Type> parameters = new ArrayList<Type>();

		if (n.getParameters() != null) {
			for (Iterator<Parameter> i = n.getParameters().iterator(); i
					.hasNext();) {
				Parameter p = i.next();		
				parameters.add(currentScope.resolveType(p.getType().toString()));
				p.accept(this, arg);
			}
		}
		methodSymbol.setParameters(parameters);

		if (n.getThrows() != null) {
			for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				NameExpr name = i.next();
				name.accept(this, arg);
			}
		}

		if (n.getBody() != null) {
		n.getBody().accept(this, methodSymbol);
		}
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(Parameter n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(EmptyMemberDeclaration n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(InitializerDeclaration n, Object arg) {
		LocalScope localScope = new LocalScope("initializer");
		localScope.setEnclosingScope(currentScope);
		
		currentScope = localScope;
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(JavadocComment n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(PrimitiveType n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ReferenceType n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(VoidType n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(WildcardType n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ArrayAccessExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ArrayCreationExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ArrayInitializerExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(AssignExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(BinaryExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(CastExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ClassExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ConditionalExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(EnclosedExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(FieldAccessExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(InstanceOfExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(StringLiteralExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(IntegerLiteralExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(LongLiteralExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(CharLiteralExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(DoubleLiteralExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(BooleanLiteralExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(NullLiteralExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(MethodCallExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(NameExpr n, Object arg) {
		n.setEnclosingScope(currentScope);	
		super.visit(n, arg);
	}

	@Override
	public void visit(ObjectCreationExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(QualifiedNameExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(SuperMemberAccessExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ThisExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(SuperExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(UnaryExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(VariableDeclarationExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(MarkerAnnotationExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(NormalAnnotationExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(MemberValuePair n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(TypeDeclarationStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(AssertStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(BlockStmt n, Object arg) {
		if(arg == null){
			LocalScope scope = new LocalScope("block");
			scope.setEnclosingScope(currentScope);
			currentScope = scope;	
		}

		n.setEnclosingScope(currentScope);

		super.visit(n, arg);	
	}

	@Override
	public void visit(LabeledStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(EmptyStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ExpressionStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(SwitchStmt n, Object arg) {
		LocalScope	scope = new LocalScope("switch");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);
		super.visit(n, scope);
	}

	@Override
	public void visit(SwitchEntryStmt n, Object arg) {

		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(BreakStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(ReturnStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(IfStmt n, Object arg) {
		LocalScope	scope = new LocalScope("if");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);
		super.visit(n, scope);
	}

	@Override
	public void visit(WhileStmt n, Object arg) {
		LocalScope	scope = new LocalScope("while");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);

		super.visit(n, scope);
	}

	@Override
	public void visit(ContinueStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(DoStmt n, Object arg) {
		LocalScope	scope = new LocalScope("do");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);

		super.visit(n, scope);
	}

	@Override
	public void visit(ForeachStmt n, Object arg) {
		LocalScope	scope = new LocalScope("foreach");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);

		super.visit(n, scope);	
	}

	@Override
	public void visit(ForStmt n, Object arg) {
		LocalScope	scope = new LocalScope("for");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);

		super.visit(n, scope);
	}

	@Override
	public void visit(ThrowStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(SynchronizedStmt n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}

	@Override
	public void visit(TryStmt n, Object arg) {
		LocalScope	scope = new LocalScope("try");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);
		super.visit(n, scope);	
	}

	@Override
	public void visit(CatchClause n, Object arg) {
		LocalScope	scope = new LocalScope("catch");
		scope.setEnclosingScope(currentScope);
		currentScope = scope;
		n.setEnclosingScope(currentScope);
		super.visit(n, scope);
	}

	@Override
	public void visit(MapInitializationExpr n, Object arg) {
		n.setEnclosingScope(currentScope);
		super.visit(n, arg);
	}
}