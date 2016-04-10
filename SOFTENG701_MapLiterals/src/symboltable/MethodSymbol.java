package symboltable;

import java.util.List;

public class MethodSymbol extends ScopedSymbol {
	private List<Type> parameters;
	private boolean hasReturn = false;

	public MethodSymbol(String name) {
		super(name, null);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Type> getParameters() {
		return parameters;
	}

	public void setParameters(List<Type> parameters) {
		this.parameters = parameters;
	}
	
	public boolean getHasReturn(){
		return this.hasReturn;
	}
	
	public void setHasReturn(boolean hasReturn){
		this.hasReturn = hasReturn;
	}
}
