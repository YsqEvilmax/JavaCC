package symboltable;


public class LocalScope extends BaseScope {

	public LocalScope() {
		super();
	}

	@Override
	public String getScopeName() {
		return "Local Scope";
	}
}
