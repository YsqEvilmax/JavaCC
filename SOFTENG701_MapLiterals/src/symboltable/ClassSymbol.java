package symboltable;

import java.util.List;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;

public class ClassSymbol extends ScopedSymbol implements Type {

	private List<Type> parameters;
	private List<InterfaceSymbol> interfaces;
	private List<ClassSymbol> superclasses;

	public ClassSymbol(String name) {
		super(name, null);
	}
	
	public void setParameterTypes(List<Type> parameters) {
		this.parameters = parameters;
	}
	
	public List<Type> getParameterTypes() {
		return this.parameters;
	}

	public List<InterfaceSymbol> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<InterfaceSymbol> interfaces) {
		this.interfaces = interfaces;
	}

	public List<ClassSymbol> getSuperclasses() {
		return superclasses;
	}

	public void setSuperclasses(List<ClassSymbol> superclasses) {
		this.superclasses = superclasses;
	}
	
	@Override
	public boolean isLocal(String s){
		boolean x = super.isLocal(s);
		if(superclasses != null){
			for(ClassSymbol cs : superclasses){
				x = (x || cs.isLocal(s));
			}
		}
		return x;
	}

	@Override
	public Symbol resolve(String name) {
		// if the symbol exists in the current scope, return it
		Symbol s = symbols.get(name);
		if (s != null)
			return s;

		// otherwise look in the superclass, if there is one
		if (superclasses != null)
		{
			for(ClassSymbol superclass : superclasses){
				Symbol x = (Symbol) superclass.resolve(name);
				if(x != null)
					return x;
			}
		}
			
		// otherwise look in the enclosing scope, if there is one
		if (enclosingScope != null)
			return enclosingScope.resolve(name);


		// otherwise it doesn't exist
		return null;
	}

	@Override
	public Type resolveType(String name) {
		// if the symbol exists in the current scope, return it
		Symbol s = symbols.get(name);
		if (s != null) {
			if (s instanceof Type) {
				return (Type) s;
			}
		}

		// otherwise look in the superclass, if there is one
		if (superclasses != null)
		{
			for(ClassSymbol superclass : superclasses){
				Type x = (Type) superclass.resolve(name);
				if(x != null)
					return x;
			}
		}
		
		// otherwise look in the enclosing scope, if there is one
		if (enclosingScope != null)
			return enclosingScope.resolveType(name);

		// otherwise it doesn't exist
		return null;
	}

	@Override
	public japa.parser.ast.type.Type castType() {
		ClassOrInterfaceType t = new ClassOrInterfaceType(this.getDefinedLine(), this.getDefinedLine(), null, name, null);
		return t;
	}
}
