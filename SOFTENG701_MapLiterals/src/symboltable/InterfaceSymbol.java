package symboltable;

import java.util.List;

import japa.parser.ast.type.ClassOrInterfaceType;

public class InterfaceSymbol extends ScopedSymbol implements Type {

	private List<Type> parameters;
	private List<InterfaceSymbol> superinterfaces;

	public InterfaceSymbol(String name) {
		super(name, null);
	}
	
	public void setParameterTypes(List<Type> parameters) {
		this.parameters = parameters;
	}
	
	public List<Type> getParameterTypes() {
		return this.parameters;
	}

	public List<InterfaceSymbol> getSuperinterfaces() {
		return superinterfaces;
	}

	public void setSuperinterfaces(List<InterfaceSymbol> superinterfaces) {
		this.superinterfaces = superinterfaces;
	}
	
	@Override
	public boolean isLocal(String s){
		boolean x = super.isLocal(s);
		for(InterfaceSymbol cs : superinterfaces){
			x = (x || cs.isLocal(s));
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
		if (superinterfaces != null){
			for(InterfaceSymbol interfacesymbol : superinterfaces){
				InterfaceSymbol x = (InterfaceSymbol) interfacesymbol.resolve(name);
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
		if (superinterfaces != null){
			for(InterfaceSymbol interfacesymbol : superinterfaces){
				InterfaceSymbol x = (InterfaceSymbol) interfacesymbol.resolve(name);
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
//		if(this.name == "boolean"){
//			this.name = "Boolean";
//		}else if(this.name == "char"){
//			this.name = "Char";
//		}else if(this.name == "byte"){
//			this.name = "Byte";
//		}
//		else if(this.name == "short"){
//			this.name = "Short";
//		}
//		else if(this.name == "int"){
//			this.name = "Integer";
//		}
//		else if(this.name == "long"){
//			this.name = "Long";
//		}
//		else if(this.name == "float"){
//			this.name = "Float";
//		}
//		else if(this.name == "double"){
//			this.name = "Double";
//		}
		ClassOrInterfaceType t = new ClassOrInterfaceType(this.getDefinedLine(), this.getDefinedLine(), null, name, null);
		return t;
	}
}
