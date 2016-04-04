package symboltable;

public abstract class Symbol {

	protected String name;
	protected Type type;
	private int definedLine = -1;
	
	public Symbol(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}

	public int getDefinedLine() {
		return definedLine;
	}

	public void setDefinedLine(int definedLine) {
		this.definedLine = definedLine;
	}
}
