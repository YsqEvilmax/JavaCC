package symboltable;

import japa.parser.ast.type.PrimitiveType;

public class BuiltInTypeSymbol extends Symbol implements Type {
	
	public BuiltInTypeSymbol(String name) {
		super(name, null);
		setDefinedLine(0);
	}

	@Override
	public japa.parser.ast.type.Type castType() {
		PrimitiveType t = null;
		if(this.name == "boolean"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Boolean);
		}else if(this.name == "char"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Char);
		}else if(this.name == "byte"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Byte);
		}
		else if(this.name == "short"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Short);
		}
		else if(this.name == "int"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Int);
		}
		else if(this.name == "long"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Long);
		}
		else if(this.name == "float"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Float);
		}
		else if(this.name == "double"){
			t = new PrimitiveType(
					this.getDefinedLine(), 
					this.getDefinedLine(), 
					PrimitiveType.Primitive.Double);
		}
		return t;
	}
}
