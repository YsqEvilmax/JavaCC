package symboltable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se701.A2SemanticsException;

public abstract class BaseScope implements Scope {

	protected HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();
	protected Scope enclosingScope = null;

	public BaseScope() {
	}

	@Override
	public String getScopeName() {
		return null;
	}

	@Override
	public Scope getEnclosingScope() {
		return enclosingScope;
	}

	@Override
	public void setEnclosingScope(Scope scope) {
		this.enclosingScope = scope;
	}

	@Override
	public void define(Symbol symbol) {
		String name = symbol.getName();
		if (this.resolve(name) != null) {
			throw new A2SemanticsException("The symbol " + name + "(on line "
					+ symbol.getDefinedLine() + ")" + " has already been defined in scope " 
					+ getScopeName() + "(on line "
					+ this.resolve(name).getDefinedLine() + ")" + "!");
		}
		symbols.put(name, symbol);
	}

	@Override
	public Symbol resolve(String name) {
		// if the symbol exists in the current scope, return it
		Symbol s = symbols.get(name);
		if (s != null)
			return s;

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

		// otherwise it doesn't exist
		return null;
	}

	@Override
	public List<Symbol> getAllSymbols() {
		List<Symbol> allSymbols = new ArrayList<Symbol>();

		for (String key : symbols.keySet()) {
			allSymbols.add(symbols.get(key));
		}

		if (enclosingScope != null) {
			allSymbols.addAll(enclosingScope.getAllSymbols());
		}

		return allSymbols;
	}

	@Override
	public void printScopeHierarchy() {
		Scope s = this;

		while (s != null) {
			System.out.println(s.getScopeName());
			s = s.getEnclosingScope();
		}
	}
}
