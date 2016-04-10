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
import se701.A2SemanticsException;
import symboltable.Scope;
import symboltable.Symbol;

public class TravelVisitor implements VoidVisitor<Object> {
	protected Scope currentScope;
	
	protected void travel(Node n, Object arg){
		if(n != null){
			n.accept(this, arg);
		}
	}
	
	protected <T extends Node>void travelMany(Iterable<T> many, Object arg){
		if(many != null){
			for(T t : many){
				travel(t, arg);
			}
		}
	}
	
	protected void checkExistance(Symbol resloved, String name, Node n){
		if (resloved == null) {
			throw new A2SemanticsException("Error occurs on line "
					+ n.getBeginLine() + " : "
					+ "name " + name
					+ " on line " + n.getBeginLine()
					+ " cannot be successfully resloved in current scope: "
					+ this.currentScope.getScopeName() + "!");
		}
	}
	
	protected void checkExistance(String name, Node n){
		if(this.currentScope != null){
			Symbol resolved = this.currentScope.resolve(name);
			checkExistance(resolved, name, n);
		}
	}
	
	protected void checkBeforeDeclaraion(String name, Node n){
		if(this.currentScope != null){
			Symbol target = this.currentScope.resolve(name);
			if (target != null) {
				if(this.currentScope.isLocal(target) && target.getDefinedLine() > n.getBeginLine())
				throw new A2SemanticsException("Error occurs on line " 
						+ n.getBeginLine() + " :"
						+ " name " + name
						+ " on line " + n.getBeginLine()
						+ " is used before its declaration on line "
						+ target.getDefinedLine() + "!");
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
		this.travel(n.getPakage(), arg);
		this.travelMany(n.getImports(), arg);
		this.travelMany(n.getTypes(), arg);
	}

	@Override
	public void visit(PackageDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getName(), arg);	
	}

	@Override
	public void visit(ImportDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getName(), arg);
	}

	@Override
	public void visit(TypeParameter n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travelMany(n.getTypeBound(), arg);
	}

	@Override
	public void visit(LineComment n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(BlockComment n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travelMany(n.getTypeParameters(), arg);
		this.travelMany(n.getExtends(), arg);
		this.travelMany(n.getImplements(), arg);
		this.travelMany(n.getMembers(), n.getName());
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(EnumDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getJavaDoc(), arg);
		this.travelMany(n.getImplements(), arg); 
		this.travelMany(n.getEntries(), arg); 
		this.travelMany(n.getMembers(), arg); 
	}

	@Override
	public void visit(EmptyTypeDeclaration n, Object arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EnumConstantDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getJavaDoc(), arg);
		this.travelMany(n.getArgs(), arg); 
		this.travelMany(n.getClassBody(), arg);
	}

	@Override
	public void visit(AnnotationDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(AnnotationMemberDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getJavaDoc(), arg);
		this.travel(n.getType(), arg);
		this.travelMany(n.getVariables(), n.getType().castType());
	}

	@Override
	public void visit(VariableDeclarator n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getId(), arg);
		this.travel(n.getInit(), arg);
	}

	@Override
	public void visit(VariableDeclaratorId n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ConstructorDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travelMany(n.getParameters(), arg);
		this.travelMany(n.getThrows(), arg);
		this.travel(n.getBlock(), currentScope.resolve(n.getName()));
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {		
		currentScope = n.getEnclosingScope();
		this.travelMany(n.getParameters(), arg);
		this.travelMany(n.getThrows(), arg);
		this.travel(n.getBody(), currentScope.resolve(n.getName()));
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(Parameter n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getType(), arg);
		this.travel(n.getId(), n.getType().castType());
	}

	@Override
	public void visit(EmptyMemberDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(InitializerDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getBlock(), arg);
	}

	@Override
	public void visit(JavadocComment n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getScope(), arg);
	}

	@Override
	public void visit(PrimitiveType n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ReferenceType n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getType(), arg);
	}

	@Override
	public void visit(VoidType n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(WildcardType n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getExtends(), arg);
		this.travel(n.getSuper(), arg);
	}

	@Override
	public void visit(ArrayAccessExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getName(), arg);
		this.travel(n.getIndex(), arg);
	}

	@Override
	public void visit(ArrayCreationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getType(), arg);
		this.travelMany(n.getDimensions(), arg);
		this.travel(n.getInitializer(), arg);
	}

	@Override
	public void visit(ArrayInitializerExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travelMany(n.getValues(), arg);
	}

	@Override
	public void visit(AssignExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getTarget(), arg);
		this.travel(n.getValue(), arg);
	}

	@Override
	public void visit(BinaryExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getLeft(), arg);
		this.travel(n.getRight(), arg);
	}

	@Override
	public void visit(CastExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getType(), arg);
		this.travel(n.getExpr(), arg);
	}

	@Override
	public void visit(ClassExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getType(), arg);
	}

	@Override
	public void visit(ConditionalExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getCondition(), arg);
		this.travel(n.getThenExpr(), arg);
		this.travel(n.getElseExpr(), arg);
	}

	@Override
	public void visit(EnclosedExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getInner(), arg);
	}

	@Override
	public void visit(FieldAccessExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getScope(), arg);
		this.travelMany(n.getTypeArgs(),arg);
	}

	@Override
	public void visit(InstanceOfExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getExpr(), arg);
		this.travel(n.getType(), arg);
	}

	@Override
	public void visit(StringLiteralExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(IntegerLiteralExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(LongLiteralExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(CharLiteralExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(DoubleLiteralExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(BooleanLiteralExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(NullLiteralExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(MethodCallExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getScope(), arg);
		this.travelMany(n.getArgs(), arg);
	}

	@Override
	public void visit(NameExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ObjectCreationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getScope(), arg);
		this.travel(n.getType(), arg);
		this.travelMany(n.getArgs(), arg);
		this.travelMany(n.getAnonymousClassBody(), arg);
	}

	@Override
	public void visit(QualifiedNameExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(SuperMemberAccessExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ThisExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getClassExpr(), arg);
	}

	@Override
	public void visit(SuperExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getClassExpr(), arg);
	}

	@Override
	public void visit(UnaryExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getExpr(), arg);
	}

	@Override
	public void visit(VariableDeclarationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getType(), arg);
		this.travelMany(n.getVars(), n.getType().castType());
	}

	@Override
	public void visit(MarkerAnnotationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(NormalAnnotationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(MemberValuePair n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getValue(), arg);
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		if (!(n.isThis())) {
			this.travel(n.getExpr(), arg);
		}
		this.travelMany(n.getArgs(), arg);
	}

	@Override
	public void visit(TypeDeclarationStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getTypeDeclaration(), arg);
	}

	@Override
	public void visit(AssertStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getCheck(), arg);
		this.travel(n.getMessage(), arg);
	}

	@Override
	public void visit(BlockStmt n, Object arg) {
//		currentScope = n.getEnclosingScope();
//		this.travelMany(n.getStmts(), arg);
//		currentScope = currentScope.getEnclosingScope();
		currentScope = n.getEnclosingScope();
		if(n.getStmts() != null){
			for(Statement t : n.getStmts()){
				if(!(t instanceof ReturnStmt)){
					arg = null;
				}
				travel(t, arg);
			}
		}
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(LabeledStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getStmt(), arg);
	}

	@Override
	public void visit(EmptyStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ExpressionStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getExpression(), arg);
	}

	@Override
	public void visit(SwitchStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getSelector(), arg);
		this.travelMany(n.getEntries(), arg);
	}

	@Override
	public void visit(SwitchEntryStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getLabel(), arg);
		this.travelMany(n.getStmts(), arg);
	}

	@Override
	public void visit(BreakStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(ReturnStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getExpr(), arg);
	}

	@Override
	public void visit(IfStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getCondition(), null);
		this.travel(n.getThenStmt(), arg);
		this.travel(n.getElseStmt(), arg);
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(WhileStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getCondition(), null);
		this.travel(n.getBody(), arg);
	}

	@Override
	public void visit(ContinueStmt n, Object arg) {
		currentScope = n.getEnclosingScope();
	}

	@Override
	public void visit(DoStmt n, Object arg) {
		currentScope = n.getEnclosingScope();	
		this.travel(n.getBody(), arg);
		this.travel(n.getCondition(), arg);
	}

	@Override
	public void visit(ForeachStmt n, Object arg) {
		currentScope = n.getEnclosingScope();	
		this.travel(n.getVariable(), arg);
		this.travel(n.getIterable(), arg);
		this.travel(n.getBody(), arg);
	}

	@Override
	public void visit(ForStmt n, Object arg) {
		currentScope = n.getEnclosingScope();	
		this.travelMany(n.getInit(), arg);
		this.travel(n.getCompare(), arg);
		this.travelMany(n.getUpdate(), arg);
		this.travel(n.getBody(), arg);
	}

	@Override
	public void visit(ThrowStmt n, Object arg) {
		currentScope = n.getEnclosingScope();	;
		this.travel(n.getExpr(), arg);
	}

	@Override
	public void visit(SynchronizedStmt n, Object arg) {
		currentScope = n.getEnclosingScope();	;
		this.travel(n.getExpr(), arg);
		this.travel(n.getBlock(), arg);
	}

	@Override
	public void visit(TryStmt n, Object arg) {
		currentScope = n.getEnclosingScope();	;
		this.travel(n.getTryBlock(), arg);
		this.travelMany(n.getCatchs(), arg);
		this.travel(n.getFinallyBlock(), arg);
	}

	@Override
	public void visit(CatchClause n, Object arg) {
		currentScope = n.getEnclosingScope();	;
		this.travel(n.getExcept(), arg);
		this.travel(n.getCatchBlock(), arg);
	}

	@Override
	public void visit(MapInitializationExpr n, Object arg) {
		currentScope = n.getEnclosingScope();
		this.travel(n.getScope(), arg);
		this.travelMany(n.getKeys(), null);
		this.travelMany(n.getValues(), null);
	}

}
