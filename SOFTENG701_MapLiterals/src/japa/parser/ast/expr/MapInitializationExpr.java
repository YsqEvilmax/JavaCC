package japa.parser.ast.expr;

import java.util.LinkedList;
import java.util.List;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

public final class MapInitializationExpr extends Expression {
	private final Expression scope;
	private final ClassOrInterfaceType type;
	private final List<Type> typeArgs;
	private final List<Expression> keys;
	private final List<Expression> values;

	public MapInitializationExpr(int line, int column, Expression scope, ClassOrInterfaceType type, List<Type> typeArgs, List<Expression> keys, List<Expression> values) {
		super(line, column);
		this.scope = scope;
		this.type = type;
		this.keys = keys;
		this.values = values;
		this.typeArgs = typeArgs;
	}

    public Expression getScope() {
        return scope;
    }
    
	public ClassOrInterfaceType getType()
	{
		return type;
	}
	
	public List<Type> getTypeArgs()
	{
		return typeArgs;
	}
	
	public void setTypeArgs(Type keyT, Type valueT)
	{
		typeArgs.add(keyT);
		typeArgs.add(valueT);
	}
	
	public List<Expression> getKeys()
	{
		return keys;
	}
	
	public List<Expression> getValues()
	{
		return values;
	}
	
    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
    
}
