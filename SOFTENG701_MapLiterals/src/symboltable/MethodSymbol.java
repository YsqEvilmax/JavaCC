package symboltable;

import java.util.List;

public class MethodSymbol extends ScopedSymbol {

	private Type returnType;
	private List<Type> parameters;

	public MethodSymbol(String name) {
		super(name, null);
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public List<Type> getParameters() {
		return parameters;
	}

	public void setParameters(List<Type> parameters) {
		this.parameters = parameters;
	}
}
