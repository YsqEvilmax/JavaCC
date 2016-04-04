package symboltable;

public class BuiltInTypeSymbol extends Symbol implements Type {
	
	public BuiltInTypeSymbol(String name) {
		super(name, null);
		setDefinedLine(0);
	}
}
