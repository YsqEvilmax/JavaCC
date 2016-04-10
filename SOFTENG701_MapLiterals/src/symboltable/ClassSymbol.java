package symboltable;

import java.util.List;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;

public class ClassSymbol extends ScopedSymbol implements Type {

	private List<InterfaceSymbol> interfaces;
	private ClassSymbol superclass;

	public ClassSymbol(String name) {
		super(name, null);
	}

	public List<InterfaceSymbol> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<InterfaceSymbol> interfaces) {
		this.interfaces = interfaces;
	}

	public ClassSymbol getSuperclass() {
		return superclass;
	}

	public void setSuperclass(ClassSymbol superclass) {
		this.superclass = superclass;
	}

	@Override
	public Symbol resolve(String name) {
		// if the symbol exists in the current scope, return it
		Symbol s = symbols.get(name);
		if (s != null)
			return s;

		// otherwise look in the superclass, if there is one
		if (superclass != null)
			return superclass.resolve(name);

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

		// otherwise look in the enclosing scope, if there is one
		if (enclosingScope != null)
			return enclosingScope.resolveType(name);

		// otherwise look in the superclass, if there is one
		if (superclass != null)
			return superclass.resolveType(name);

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
