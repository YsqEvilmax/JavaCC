package symboltable;


public class LocalScope extends BaseScope {
	private String name;

	public LocalScope(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getScopeName() {
		return "Scope: " + name;
	}
}
