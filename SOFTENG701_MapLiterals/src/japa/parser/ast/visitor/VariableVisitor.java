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
import japa.parser.ast.expr.LiteralExpr;
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

import java.util.Iterator;
import java.util.List;

import se701.A2SemanticsException;
import symboltable.MethodSymbol;
import symboltable.Scope;
import symboltable.Symbol;
import symboltable.Type;
import symboltable.VariableSymbol;

public class VariableVisitor implements VoidVisitor<Object> {

	private Scope currentScope;

	private void visitMembers(List<BodyDeclaration> members, Object arg) {
		for (BodyDeclaration member : members) {
			member.accept(this, arg);
		}
	}

	private void visitTypeParameters(List<TypeParameter> args, Object arg) {
		if (args != null) {
			for (Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
				TypeParameter t = i.next();
				t.accept(this, arg);
			}
		}
	}

	@Override
	public void visit(Node n, Object arg) {
		throw new IllegalStateException(n.getClass().getName());
	}

	@Override
	public void visit(CompilationUnit n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (n.getPakage() != null) {
			n.getPakage().accept(this, arg);
		}
		if (n.getImports() != null) {
			for (ImportDeclaration i : n.getImports()) {
				i.accept(this, arg);
			}
		}
		if (n.getTypes() != null) {
			for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i
					.hasNext();) {
				i.next().accept(this, arg);
			}
		}
	}

	@Override
	public void visit(PackageDeclaration n, Object arg) {
		n.getName().accept(this, arg);
	}

	@Override
	public void visit(NameExpr n, Object arg) {
	}

	@Override
	public void visit(QualifiedNameExpr n, Object arg) {
		n.getQualifier().accept(this, arg);
	}

	@Override
	public void visit(ImportDeclaration n, Object arg) {
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}

		visitTypeParameters(n.getTypeParameters(), arg);

		if (n.getExtends() != null) {
			for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
			}
		}

		if (n.getImplements() != null) {
			for (Iterator<ClassOrInterfaceType> i = n.getImplements()
					.iterator(); i.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
			}
		}
		if (n.getMembers() != null) {
			visitMembers(n.getMembers(), arg);
		}

		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(EmptyTypeDeclaration n, Object arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
	}

	@Override
	public void visit(JavadocComment n, Object arg) {
	}

	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}
	}

	@Override
	public void visit(TypeParameter n, Object arg) {
		if (n.getTypeBound() != null) {
			for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
			}
		}
	}

	@Override
	public void visit(ReferenceType n, Object arg) {
		n.getType().accept(this, arg);
	}

	@Override
	public void visit(WildcardType n, Object arg) {
		if (n.getExtends() != null) {
			n.getExtends().accept(this, arg);
		}
		if (n.getSuper() != null) {
			n.getSuper().accept(this, arg);
		}
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();

		for (VariableDeclarator v : n.getVariables()) {
			Symbol resolved = currentScope.resolve(n.getType().toString());
			Type type = null;

			if (resolved instanceof Type) {
				type = (Type) resolved;
			} else {
				throw new A2SemanticsException(resolved.getName()
						+ " is not a type! Is a "
						+ resolved.getClass().getName());
			}

			String name = v.getId().toString();
			VariableSymbol symbol = new VariableSymbol(name, type);
			symbol.setDefinedLine(v.getBeginLine());
			currentScope.define(symbol);
		}

		n.getType().accept(this, arg);
	}

	@Override
	public void visit(VariableDeclarator n, Object arg) {
		n.getId().accept(this, arg);
		if (n.getInit() != null) {
			n.getInit().accept(this, arg);
		}
	}

	@Override
	public void visit(VariableDeclaratorId n, Object arg) {
		for (int i = 0; i < n.getArrayCount(); i++) {
		}
	}

	@Override
	public void visit(ArrayInitializerExpr n, Object arg) {
		if (n.getValues() != null) {
			for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
				Expression expr = i.next();
				expr.accept(this, arg);
			}
		}
	}

	@Override
	public void visit(VoidType n, Object arg) {
	}

	@Override
	public void visit(ArrayAccessExpr n, Object arg) {
		n.getName().accept(this, arg);
		n.getIndex().accept(this, arg);
	}

	@Override
	public void visit(ArrayCreationExpr n, Object arg) {
		n.getType().accept(this, arg);

		if (n.getDimensions() != null) {
			for (Expression dim : n.getDimensions()) {
				dim.accept(this, arg);
			}
			for (int i = 0; i < n.getArrayCount(); i++) {
			}
		} else {
			for (int i = 0; i < n.getArrayCount(); i++) {
			}
			n.getInitializer().accept(this, arg);
		}
	}

	@Override
	public void visit(AssignExpr n, Object arg) {
		n.getTarget().accept(this, arg);

		n.getValue().accept(this, arg);
	}

	@Override
	public void visit(BinaryExpr n, Object arg) {
		n.getLeft().accept(this, arg);

		n.getRight().accept(this, arg);
	}

	@Override
	public void visit(CastExpr n, Object arg) {
		n.getType().accept(this, arg);
		n.getExpr().accept(this, arg);
	}

	@Override
	public void visit(ClassExpr n, Object arg) {
		n.getType().accept(this, arg);
	}

	@Override
	public void visit(ConditionalExpr n, Object arg) {
		n.getCondition().accept(this, arg);
		n.getThenExpr().accept(this, arg);
		n.getElseExpr().accept(this, arg);
	}

	@Override
	public void visit(EnclosedExpr n, Object arg) {
		n.getInner().accept(this, arg);
	}

	@Override
	public void visit(FieldAccessExpr n, Object arg) {
		n.getScope().accept(this, arg);
	}

	@Override
	public void visit(InstanceOfExpr n, Object arg) {
		n.getExpr().accept(this, arg);
		n.getType().accept(this, arg);
	}

	@Override
	public void visit(CharLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(DoubleLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(IntegerLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(LongLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, Object arg) {
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, Object arg) {
	}

	@Override
	public void visit(StringLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(BooleanLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(NullLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(ThisExpr n, Object arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
		}
	}

	@Override
	public void visit(SuperExpr n, Object arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
		}
	}

	@Override
	public void visit(MethodCallExpr n, Object arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}

		if (n.getArgs() != null) {
			for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
			}
		}
	}

	@Override
	public void visit(ObjectCreationExpr n, Object arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}

		n.getType().accept(this, arg);

		if (n.getArgs() != null) {
			for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
			}
		}

		if (n.getAnonymousClassBody() != null) {
			visitMembers(n.getAnonymousClassBody(), arg);
		}
	}

	@Override
	public void visit(SuperMemberAccessExpr n, Object arg) {
	}

	@Override
	public void visit(UnaryExpr n, Object arg) {
		n.getExpr().accept(this, arg);
	}

	@Override
	public void visit(ConstructorDeclaration n, Object arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}

		visitTypeParameters(n.getTypeParameters(), arg);

		if (n.getParameters() != null) {
			for (Iterator<Parameter> i = n.getParameters().iterator(); i
					.hasNext();) {
				Parameter p = i.next();
				p.accept(this, arg);
			}
		}

		if (n.getThrows() != null) {
			for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				NameExpr name = i.next();
				name.accept(this, arg);
			}
		}
		n.getBlock().accept(this, arg);
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (currentScope instanceof MethodSymbol) {
			if (n.getBody() != null) {
				n.getBody().accept(this, arg);
			}
		} else {
			throw new A2SemanticsException(
					"Scope of MethodDeclaration not a MethodSymbol! (is a "
							+ currentScope.getClass().getName() + ")");
		}

		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				Type type = currentScope.resolveType(p.getType().toString());

				String name = p.getId().getName();
				VariableSymbol symbol = new VariableSymbol(name, type);
				symbol.setDefinedLine(p.getBeginLine());
				currentScope.define(symbol);
			}
		}

		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(Parameter n, Object arg) {
		n.getType().accept(this, arg);
		n.getId().accept(this, arg);
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
		if (n.isThis()) {
		} else {
			if (n.getExpr() != null) {
				n.getExpr().accept(this, arg);
			}
		}
		if (n.getArgs() != null) {
			for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
			}
		}
	}

	@Override
	public void visit(VariableDeclarationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();

		for (VariableDeclarator v : n.getVars()) {
			Symbol resolved = currentScope.resolve(n.getType().toString());
			Type type = null;

			if (resolved instanceof Type) {
				type = (Type) resolved;
			} else {
				throw new A2SemanticsException(resolved.getName()
						+ " is not a type! Is a "
						+ resolved.getClass().getName());
			}

			String name = v.getId().toString();
			VariableSymbol symbol = new VariableSymbol(name, type);
			symbol.setDefinedLine(v.getBeginLine());
			currentScope.define(symbol);
		}

		n.getType().accept(this, arg);

		for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i
				.hasNext();) {
			VariableDeclarator v = i.next();
			v.accept(this, arg);
		}
	}

	@Override
	public void visit(TypeDeclarationStmt n, Object arg) {
		n.getTypeDeclaration().accept(this, arg);
	}

	@Override
	public void visit(AssertStmt n, Object arg) {
		n.getCheck().accept(this, arg);
		if (n.getMessage() != null) {
			n.getMessage().accept(this, arg);
		}
	}

	@Override
	public void visit(BlockStmt n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
			}
		}

		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(LabeledStmt n, Object arg) {
		n.getStmt().accept(this, arg);
	}

	@Override
	public void visit(EmptyStmt n, Object arg) {
	}

	@Override
	public void visit(ExpressionStmt n, Object arg) {
		n.getExpression().accept(this, arg);
	}

	@Override
	public void visit(SwitchStmt n, Object arg) {
		n.getSelector().accept(this, arg);
		if (n.getEntries() != null) {
			for (SwitchEntryStmt e : n.getEntries()) {
				e.accept(this, arg);
			}
		}

	}

	@Override
	public void visit(SwitchEntryStmt n, Object arg) {
		if (n.getLabel() != null) {
			n.getLabel().accept(this, arg);
		}

		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
			}
		}

	}

	@Override
	public void visit(BreakStmt n, Object arg) {
	}

	@Override
	public void visit(ReturnStmt n, Object arg) {
		if (n.getExpr() != null) {
			n.getExpr().accept(this, arg);
		}
	}

	@Override
	public void visit(EnumDeclaration n, Object arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}

		if (n.getImplements() != null) {
			for (Iterator<ClassOrInterfaceType> i = n.getImplements()
					.iterator(); i.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
			}
		}

		if (n.getEntries() != null) {
			for (Iterator<EnumConstantDeclaration> i = n.getEntries()
					.iterator(); i.hasNext();) {
				EnumConstantDeclaration e = i.next();
				e.accept(this, arg);
			}
		}
		if (n.getMembers() != null) {
			visitMembers(n.getMembers(), arg);
		}
	}

	@Override
	public void visit(EnumConstantDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}

		if (n.getArgs() != null) {
			for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
			}
		}

		if (n.getClassBody() != null) {
			visitMembers(n.getClassBody(), arg);
		}

		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(EmptyMemberDeclaration n, Object arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
	}

	@Override
	public void visit(InitializerDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		n.getBlock().accept(this, arg);

		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(IfStmt n, Object arg) {
		currentScope = n.getEnclosingScope();

		n.getCondition().accept(this, arg);
		n.getThenStmt().accept(this, arg);
		if (n.getElseStmt() != null) {
			n.getElseStmt().accept(this, arg);
		}
	}

	@Override
	public void visit(WhileStmt n, Object arg) {
		currentScope = n.getEnclosingScope();

		n.getCondition().accept(this, arg);
		n.getBody().accept(this, arg);
	}

	@Override
	public void visit(ContinueStmt n, Object arg) {
	}

	@Override
	public void visit(DoStmt n, Object arg) {
		currentScope = n.getEnclosingScope();

		n.getBody().accept(this, arg);
		n.getCondition().accept(this, arg);
	}

	@Override
	public void visit(ForeachStmt n, Object arg) {
		currentScope = n.getEnclosingScope();

		n.getVariable().accept(this, arg);
		n.getIterable().accept(this, arg);
		n.getBody().accept(this, arg);
	}

	@Override
	public void visit(ForStmt n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (n.getInit() != null) {
			for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
			}
		}
		if (n.getCompare() != null) {
			n.getCompare().accept(this, arg);
		}
		if (n.getUpdate() != null) {
			for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
			}
		}

		n.getBody().accept(this, arg);
	}

	@Override
	public void visit(ThrowStmt n, Object arg) {
		n.getExpr().accept(this, arg);
	}

	@Override
	public void visit(SynchronizedStmt n, Object arg) {
		n.getExpr().accept(this, arg);
		n.getBlock().accept(this, arg);
	}

	@Override
	public void visit(TryStmt n, Object arg) {
		currentScope = n.getEnclosingScope();

		n.getTryBlock().accept(this, arg);
		if (n.getCatchs() != null) {
			for (CatchClause c : n.getCatchs()) {
				c.accept(this, arg);
			}
		}
		if (n.getFinallyBlock() != null) {
			n.getFinallyBlock().accept(this, arg);
		}
	}

	@Override
	public void visit(CatchClause n, Object arg) {
		currentScope = n.getEnclosingScope();

		n.getExcept().accept(this, arg);
		n.getCatchBlock().accept(this, arg);
	}

	@Override
	public void visit(AnnotationDeclaration n, Object arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}

		if (n.getMembers() != null) {
			visitMembers(n.getMembers(), arg);
		}
	}

	@Override
	public void visit(AnnotationMemberDeclaration n, Object arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}

		n.getType().accept(this, arg);
		if (n.getDefaultValue() != null) {
			n.getDefaultValue().accept(this, arg);
		}
	}

	@Override
	public void visit(MarkerAnnotationExpr n, Object arg) {
		n.getName().accept(this, arg);
	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, Object arg) {
		n.getName().accept(this, arg);
		n.getMemberValue().accept(this, arg);
	}

	@Override
	public void visit(NormalAnnotationExpr n, Object arg) {
		n.getName().accept(this, arg);
		for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i.hasNext();) {
			MemberValuePair m = i.next();
			m.accept(this, arg);
		}
	}

	@Override
	public void visit(MemberValuePair n, Object arg) {
		n.getValue().accept(this, arg);
	}

	@Override
	public void visit(LineComment n, Object arg) {
	}

	@Override
	public void visit(BlockComment n, Object arg) {
	}

	@Override
	public void visit(PrimitiveType n, Object arg) {
	}

	@Override
	public void visit(MapInitializationExpr n, Object arg) {
		if(n.getKeys().size() != n.getValues().size())
		{
			throw new A2SemanticsException("The keys and values are not in pairs at "
					+ n.getBeginLine() + "!");
		}
		
		ClassOrInterfaceType lastKeysType, lastValuesType;
        if(n.getTypeArgs()== null || n.getTypeArgs().isEmpty())
        {
    		lastKeysType = n.getKeys().get(0).getType(); 		
    		lastValuesType = n.getValues().get(0).getType();		
    		n.setTypeArgs(lastKeysType, lastValuesType);
     	}
        else if(n.getTypeArgs().size() == 2)
        {
        	lastKeysType = (ClassOrInterfaceType)n.getTypeArgs().get(0);
        	lastValuesType = (ClassOrInterfaceType)n.getTypeArgs().get(1);
        }
        else
        {
        	throw new A2SemanticsException("There should be only 2 columns in Maps, but at " + n.getBeginLine() 
        	+ "there are " + n.getTypeArgs().size());
        }

    	for(LiteralExpr e: n.getKeys())
    	{
    		if(!lastKeysType.getName().equals(e.getType().getName()))
    		{
    			throw new A2SemanticsException("The items in keys are not in the same type!");
    		}
    	}
        
		for(LiteralExpr e: n.getValues())
		{
			if(!lastValuesType.getName().equals(e.getType().getName()))
			{
				throw new A2SemanticsException("The items in values are not in the same type!");
			}
		}
	}
}