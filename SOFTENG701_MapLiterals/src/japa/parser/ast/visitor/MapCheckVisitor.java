package japa.parser.ast.visitor;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.MapInitializationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.Type;
import se701.A2SemanticsException;
import symboltable.Symbol;
import symboltable.VariableSymbol;

public class MapCheckVisitor extends TypeCheckVisitor {
	private Type analyseType(Expression expr){
		if(expr instanceof LiteralExpr){
			String name = ((LiteralExpr)expr).getType().getName();
			if(name == "boolean"){
				name = "Boolean";
			}else if(name == "char"){
				name = "Char";
			}else if(name == "byte"){
				name = "Byte";
			}else if(name == "short"){
				name = "Short";
			}else if(name == "int"){
				name = "Integer";
			}else if(name == "long"){
				name = "Long";
			}else if(name == "float"){
				name = "Float";
			}else if(name == "double"){
				name = "Double";
			}
		ClassOrInterfaceType t = new ClassOrInterfaceType(expr.getBeginLine(), expr.getBeginColumn(), null, name, null);
		return t;
		}
		if(expr instanceof NameExpr){
			Symbol s = currentScope.resolve(((NameExpr)expr).getName());
			if(s instanceof VariableSymbol){
				return s.getType().castType();
			}
		}
		return null;
	}
	
	private void initializeMapType(MapInitializationExpr n, Object arg){
		if(n.getKeys().size() != n.getValues().size()){
			throw new A2SemanticsException("Error eccours on line "
					+ n.getBeginLine() + " :" 
					+ " there are " + n.getKeys().size()
					+ " keys but "  + n.getValues().size()
					+ " values in Map.");
		}	
		ClassOrInterfaceType lastKeysType, lastValuesType;
        if(n.getTypeArgs()== null || n.getTypeArgs().isEmpty())
        {
        	Expression expr = n.getKeys().get(0);
        	lastKeysType = (ClassOrInterfaceType)analyseType(expr);
        	expr = n.getValues().get(0);
        	lastValuesType = (ClassOrInterfaceType)analyseType(expr);		
    		n.setTypeArgs(lastKeysType, lastValuesType);
     	}
        else if(n.getTypeArgs().size() == 2)
        {
        	lastKeysType = (ClassOrInterfaceType)n.getTypeArgs().get(0);
        	lastValuesType = (ClassOrInterfaceType)n.getTypeArgs().get(1);
        }
        else
        {
        	throw new A2SemanticsException("Error occurs on line "
        			+ n.getBeginLine() + " :"
        			+ " there should be only 2 columns in Maps, but at " + n.getBeginLine() 
        			+ "there are " + n.getTypeArgs().size());
        }
        
    	for(Expression e: n.getKeys())
    	{
    		checkType(lastKeysType.castType(), analyseType(e).castType(), n);
    	}
     
		for(Expression e: n.getKeys())
    	{
    		checkType(lastKeysType.castType(), analyseType(e).castType(), n);
    	}
	}
	
	public MapCheckVisitor() {
		super();
	}

	@Override
	public void visit(MapInitializationExpr n, Object arg) {
		super.visit(n, arg);
		initializeMapType(n, arg);
	}
}
