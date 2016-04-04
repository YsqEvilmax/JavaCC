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

import java.util.Iterator;
import java.util.List;

import se701.A2SemanticsException;
import symboltable.ClassSymbol;
import symboltable.InterfaceSymbol;
import symboltable.MethodSymbol;
import symboltable.Scope;
import symboltable.Symbol;
import symboltable.Type;

public class MethodVisitor implements VoidVisitor<Object> {

	private Scope currentScope;

	@Override
	public void visit(Node n, Object arg) {
		throw new IllegalStateException(n.getClass().getName());
	}

	@Override
	public void visit(CompilationUnit n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (n.getTypes() != null) {
			for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i
					.hasNext();) {
				i.next().accept(this, arg);
			}
		}
	}

	@Override
	public void visit(PackageDeclaration n, Object arg) {
	}

	@Override
	public void visit(ImportDeclaration n, Object arg) {
	}

	@Override
	public void visit(TypeParameter n, Object arg) {
	}

	@Override
	public void visit(LineComment n, Object arg) {
	}

	@Override
	public void visit(BlockComment n, Object arg) {
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();

		if (currentScope instanceof ClassSymbol) {
			ClassSymbol classSymbol = (ClassSymbol) currentScope;

			if (classSymbol.getInterfaces() != null) {

				for (InterfaceSymbol i : classSymbol.getInterfaces()) {
					
					if (i.getAllSymbols() != null) {
						for (Symbol s: i.getAllSymbols()) {
							if (s instanceof MethodSymbol) {
								MethodSymbol m = (MethodSymbol) s;

								Symbol resolved = currentScope.resolve(m
										.getName());

								if (resolved != null
										&& resolved instanceof MethodSymbol) {

								} else {
									throw new A2SemanticsException("Class "
											+ n.getName()
											+ " must declare a method named "
											+ m.getName() + " (from interface "
											+ i.getName() + ").");
								}
							}
						}
					}
				}
			}
		}

		if (n.getMembers() != null) {
			for (BodyDeclaration b : n.getMembers()) {
				b.accept(this, arg);
			}
		}

		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void visit(EnumDeclaration n, Object arg) {
	}

	@Override
	public void visit(EmptyTypeDeclaration n, Object arg) {
	}

	@Override
	public void visit(EnumConstantDeclaration n, Object arg) {
	}

	@Override
	public void visit(AnnotationDeclaration n, Object arg) {
	}

	@Override
	public void visit(AnnotationMemberDeclaration n, Object arg) {
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
	}

	@Override
	public void visit(VariableDeclarator n, Object arg) {
		if (n.getInit() instanceof MethodCallExpr) {
			n.getInit().accept(this, arg);
		}
	}

	@Override
	public void visit(VariableDeclaratorId n, Object arg) {
	}

	@Override
	public void visit(ConstructorDeclaration n, Object arg) {
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		currentScope = n.getEnclosingScope();

		MethodSymbol theMethod = ((MethodSymbol) currentScope.resolve(n.getName()));
		boolean hasReturn = false;

		if (theMethod.getReturnType().getName() == "void") {
			hasReturn = true;
		}

		String t = theMethod.getReturnType().getName();

		if (n.getBody() != null) {
			BlockStmt body = n.getBody();
			if (body.getStmts() != null) {
				for (Statement s : body.getStmts()) {
					if (s instanceof ReturnStmt) {
						ReturnStmt r = (ReturnStmt) s;
						Expression expr = r.getExpr();

						if (expr instanceof BinaryExpr) {
							expr = ((BinaryExpr) expr).getLeft();
						}

						if (expr instanceof NullLiteralExpr) {
							hasReturn = true;
							if (t == "int" || t == "byte" || t == "short"
									|| t == "long" || t == "double"
									|| t == "float" || t == "boolean"
									|| t == "char") {
								throw new A2SemanticsException("Method "
										+ theMethod.getName() + " ("
										+ theMethod.getReturnType().getName()
										+ ") cannot return null! (On line "
										+ n.getBeginLine() + ".)");
							}
						} else if (expr instanceof CharLiteralExpr) {
							hasReturn = true;
							if (t != "char") {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						} else if (expr instanceof DoubleLiteralExpr) {
							hasReturn = true;
							if (t != "double") {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						} else if (expr instanceof IntegerLiteralExpr) {
							hasReturn = true;
							if (t != "int") {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						} else if (expr instanceof LongLiteralExpr) {
							hasReturn = true;
							if (t != "long") {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						} else if (expr instanceof StringLiteralExpr) {
							hasReturn = true;
							if (t != "String") {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						} else if (expr instanceof BooleanLiteralExpr) {
							hasReturn = true;
							if (t != "boolean") {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						} else if (expr instanceof MethodCallExpr) {
							hasReturn = true;
							Symbol resolvedSymbol = currentScope
									.resolve(((MethodCallExpr) expr).getName()
											.toString());

							if (resolvedSymbol instanceof MethodSymbol) {
								MethodSymbol m = (MethodSymbol) resolvedSymbol;
								String returnType = m.getReturnType().getName();
								if (t != returnType) {
									throw new A2SemanticsException(
											"The method "
													+ theMethod.getName()
													+ " must return a value of type "
													+ theMethod.getReturnType()
															.getName()
													+ "(On line "
													+ n.getBeginLine() + ".)");
								}
							} else {
								throw new A2SemanticsException(
										"Retrieved Symbol "
												+ resolvedSymbol.getName()
												+ " ("
												+ resolvedSymbol.getClass()
														.getSimpleName()
												+ ") from Scope "
												+ currentScope.getScopeName()
												+ " (expected MethodSymbol).");
							}
						} else if (expr instanceof ObjectCreationExpr) {
							hasReturn = true;
							Symbol resolvedSymbol = currentScope
									.resolve(((ObjectCreationExpr) expr)
											.getType().getName());

							if (resolvedSymbol != theMethod.getReturnType()) {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						} else if (expr instanceof NameExpr) {
							hasReturn = true;
							Symbol resolvedSymbol = currentScope
									.resolve(((NameExpr) expr).getName());

							if (resolvedSymbol == null
									|| resolvedSymbol.getType() != theMethod
									.getReturnType()) {
								throw new A2SemanticsException("The method "
										+ theMethod.getName()
										+ " must return a value of type "
										+ theMethod.getReturnType().getName()
										+ "(On line " + n.getBeginLine() + ".)");
							}
						}
					}
				}
			}

			body.accept(this, arg);

			if (!hasReturn) {
				throw new A2SemanticsException("Method " + n.getName()
						+ " (on line " + n.getBeginLine()
						+ ") does not return a value of type "
						+ theMethod.getReturnType().getName());
			}
		}
	}

	@Override
	public void visit(Parameter n, Object arg) {
	}

	@Override
	public void visit(EmptyMemberDeclaration n, Object arg) {
	}

	@Override
	public void visit(InitializerDeclaration n, Object arg) {
	}

	@Override
	public void visit(JavadocComment n, Object arg) {
	}

	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
	}

	@Override
	public void visit(PrimitiveType n, Object arg) {
	}

	@Override
	public void visit(ReferenceType n, Object arg) {
	}

	@Override
	public void visit(VoidType n, Object arg) {
	}

	@Override
	public void visit(WildcardType n, Object arg) {
	}

	@Override
	public void visit(ArrayAccessExpr n, Object arg) {
	}

	@Override
	public void visit(ArrayCreationExpr n, Object arg) {
	}

	@Override
	public void visit(ArrayInitializerExpr n, Object arg) {
	}

	@Override
	public void visit(AssignExpr n, Object arg) {
		if (n.getValue() instanceof MethodCallExpr) {
			n.getValue().accept(this, arg);
		}
	}

	@Override
	public void visit(BinaryExpr n, Object arg) {
	}

	@Override
	public void visit(CastExpr n, Object arg) {
	}

	@Override
	public void visit(ClassExpr n, Object arg) {
	}

	@Override
	public void visit(ConditionalExpr n, Object arg) {
	}

	@Override
	public void visit(EnclosedExpr n, Object arg) {
	}

	@Override
	public void visit(FieldAccessExpr n, Object arg) {
	}

	@Override
	public void visit(InstanceOfExpr n, Object arg) {
	}

	@Override
	public void visit(StringLiteralExpr n, Object arg) {
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
	public void visit(CharLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(DoubleLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(BooleanLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(NullLiteralExpr n, Object arg) {
	}

	@Override
	public void visit(MethodCallExpr n, Object arg) {
		currentScope = n.getEnclosingScope();

		Symbol resolved = currentScope.resolve(n.getName());

		if (resolved instanceof MethodSymbol) {
			MethodSymbol m = (MethodSymbol) resolved;

			List<Type> expected = m.getParameters();
			List<Expression> received = n.getArgs();

			if (expected != null) {
				for (int i = 0; i < expected.size(); i++) {
					Expression expr = received.get(i);
					Type type = expected.get(i);
					String t = type.getName();

					if (expr instanceof BinaryExpr) {
						expr = ((BinaryExpr) expr).getLeft();
					}

					if (expr instanceof NullLiteralExpr) {
						if (t == "int" || t == "byte" || t == "short"
								|| t == "long" || t == "double" || t == "float"
								|| t == "boolean" || t == "char") {
							throw new A2SemanticsException(
									"Primitive parameter of method "
											+ m.getName()
											+ " cannot be null! (On line "
											+ n.getBeginLine() + ".)");
						}
					} else if (expr instanceof CharLiteralExpr) {
						if (t != "char") {
							throw new A2SemanticsException(
									"Method "
											+ m.getName()
											+ " does not have a parameter of type char (expected "
											+ t + ") (On line "
											+ n.getBeginLine() + ".)");
						}
					} else if (expr instanceof DoubleLiteralExpr) {
						if (t != "double") {
							throw new A2SemanticsException(
									"Method "
											+ m.getName()
											+ " does not have a parameter of type double (expected "
											+ t + ") (On line "
											+ n.getBeginLine() + ".)");
						}
					} else if (expr instanceof IntegerLiteralExpr) {
						if (t != "int") {
							throw new A2SemanticsException(
									"Method "
											+ m.getName()
											+ " does not have a parameter of type int (expected "
											+ t + ") (On line "
											+ n.getBeginLine() + ".)");
						}
					} else if (expr instanceof LongLiteralExpr) {
						if (t != "long") {
							throw new A2SemanticsException(
									"Method "
											+ m.getName()
											+ " does not have a parameter of type long (expected "
											+ t + ") (On line "
											+ n.getBeginLine() + ".)");
						}
					} else if (expr instanceof StringLiteralExpr) {
						if (t != "String") {
							throw new A2SemanticsException(
									"Method "
											+ m.getName()
											+ " does not have a parameter of type String (expected "
											+ t + ") (On line "
											+ n.getBeginLine() + ".)");
						}
					} else if (expr instanceof BooleanLiteralExpr) {
						if (t != "boolean") {
							throw new A2SemanticsException(
									"Method "
											+ m.getName()
											+ " does not have a parameter of type boolean (expected "
											+ t + ") (On line "
											+ n.getBeginLine() + ".)");
						}
					} else if (expr instanceof MethodCallExpr) {
						Symbol resolvedSymbol = currentScope
								.resolve(((MethodCallExpr) expr).getName()
										.toString());

						if (resolvedSymbol instanceof MethodSymbol) {
							MethodSymbol method = (MethodSymbol) resolvedSymbol;
							String returnType = method.getReturnType()
									.getName();
							if (t != returnType) {
								throw new A2SemanticsException("Method "
										+ m.getName()
										+ " does not have a parameter of type "
										+ returnType + " (expected " + t
										+ ") (On line " + n.getBeginLine()
										+ ".)");
							}
						} else {
							throw new A2SemanticsException("Retrieved Symbol "
									+ resolvedSymbol.getName() + " ("
									+ resolvedSymbol.getClass().getSimpleName()
									+ ") from Scope "
									+ currentScope.getScopeName()
									+ " (expected MethodSymbol).");
						}
					} else if (expr instanceof ObjectCreationExpr) {
						Symbol resolvedSymbol = currentScope
								.resolve(((ObjectCreationExpr) expr).getType()
										.getName());

						if (resolvedSymbol.getType().getName() != t) {
							throw new A2SemanticsException("Method "
									+ m.getName()
									+ " does not have a parameter of type "
									+ resolvedSymbol.getType().getName()
									+ " (expected " + t
									+ ") (On line " + n.getBeginLine() + ".)");
						}
					} else if (expr instanceof NameExpr) {
						Symbol resolvedSymbol = currentScope
								.resolve(((NameExpr) expr).getName());

						if (resolvedSymbol.getType().getName() != t) {
							throw new A2SemanticsException("Method "
									+ m.getName()
									+ " does not have a parameter of type "
									+ resolvedSymbol.getType().getName()
									+ " (expected " + t + ") (On line "
									+ n.getBeginLine() + ".)");
						}
					}
				}
			}
		} else {
			throw new A2SemanticsException(
					"Did not resolve expected MethodSymbol " + n.getName()
							+ "! (On line " + n.getBeginLine() + ")");
		}
	}

	@Override
	public void visit(NameExpr n, Object arg) {
	}

	@Override
	public void visit(ObjectCreationExpr n, Object arg) {
	}

	@Override
	public void visit(QualifiedNameExpr n, Object arg) {
	}

	@Override
	public void visit(SuperMemberAccessExpr n, Object arg) {
	}

	@Override
	public void visit(ThisExpr n, Object arg) {
	}

	@Override
	public void visit(SuperExpr n, Object arg) {
	}

	@Override
	public void visit(UnaryExpr n, Object arg) {
	}

	@Override
	public void visit(VariableDeclarationExpr n, Object arg) {
		if (n.getVars() != null) {
			for (VariableDeclarator v : n.getVars()) {
				v.accept(this, arg);
			}
		}
	}

	@Override
	public void visit(MarkerAnnotationExpr n, Object arg) {
	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, Object arg) {
	}

	@Override
	public void visit(NormalAnnotationExpr n, Object arg) {
	}

	@Override
	public void visit(MemberValuePair n, Object arg) {
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
	}

	@Override
	public void visit(TypeDeclarationStmt n, Object arg) {
	}

	@Override
	public void visit(AssertStmt n, Object arg) {
	}

	@Override
	public void visit(BlockStmt n, Object arg) {
		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
			}
		}
	}

	@Override
	public void visit(LabeledStmt n, Object arg) {
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
	}

	@Override
	public void visit(SwitchEntryStmt n, Object arg) {
	}

	@Override
	public void visit(BreakStmt n, Object arg) {
	}

	@Override
	public void visit(ReturnStmt n, Object arg) {
	}

	@Override
	public void visit(IfStmt n, Object arg) {
	}

	@Override
	public void visit(WhileStmt n, Object arg) {
	}

	@Override
	public void visit(ContinueStmt n, Object arg) {
	}

	@Override
	public void visit(DoStmt n, Object arg) {
	}

	@Override
	public void visit(ForeachStmt n, Object arg) {
	}

	@Override
	public void visit(ForStmt n, Object arg) {
	}

	@Override
	public void visit(ThrowStmt n, Object arg) {
	}

	@Override
	public void visit(SynchronizedStmt n, Object arg) {
	}

	@Override
	public void visit(TryStmt n, Object arg) {
	}

	@Override
	public void visit(CatchClause n, Object arg) {
	}

	@Override
	public void visit(MapInitializationExpr n, Object arg) {
		// TODO Auto-generated method stub
		
	}
}