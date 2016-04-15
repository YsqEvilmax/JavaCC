package se701;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.visitor.SillyBreakVisitor;
import japa.parser.ast.visitor.TypeCheckVisitor;
import japa.parser.ast.visitor.VariableCheckVisitor;
import japa.parser.ast.visitor.VariableDefineVisitor;
import japa.parser.ast.visitor.ClassCheckVisitor;
import japa.parser.ast.visitor.ClassDefineVisitor;
import japa.parser.ast.visitor.DumpVisitor;
import japa.parser.ast.visitor.MapCheckVisitor;
import japa.parser.ast.visitor.MethodCheckVisitor;
import japa.parser.ast.visitor.MethodDefineVisitor;
import japa.parser.ast.visitor.ScopeCreateVisitor;

public class A2Compiler {
	
	/*
	 * This is the only method you should need to change inside this class. But do not modify the signature of the method! 
	 */
	public static void compile(File file) throws ParseException, FileNotFoundException {

		// parse the input, performs lexical and syntatic analysis
		JavaParser parser = new JavaParser(new FileReader(file));
		CompilationUnit ast = parser.CompilationUnit();
		
		// perform visit 1...
		SillyBreakVisitor semanticsVisitor = new SillyBreakVisitor();
		ast.accept(semanticsVisitor, null);
		
		// perform visit 2... etc etc 
		// check scopes
		ScopeCreateVisitor scopes = new ScopeCreateVisitor();
		ast.accept(scopes, null);
		
		ClassDefineVisitor ccv = new ClassDefineVisitor();
		ast.accept(ccv, null);
		
		MethodDefineVisitor mcv = new MethodDefineVisitor();
		ast.accept(mcv, null);
		
		VariableDefineVisitor vcv = new VariableDefineVisitor();
		ast.accept(vcv, null);
		
		ClassCheckVisitor cckv = new ClassCheckVisitor();
		ast.accept(cckv, null);
		
		MethodCheckVisitor mckv = new MethodCheckVisitor();
		ast.accept(mckv, null);
			
		VariableCheckVisitor vckv = new VariableCheckVisitor();
		ast.accept(vckv, null);
			
		MapCheckVisitor mapckv = new MapCheckVisitor();
		ast.accept(mapckv, null);

		
		// perform visit N 
		DumpVisitor printVisitor = new DumpVisitor();
		ast.accept(printVisitor, null);
		
		String result = printVisitor.getSource();
		
		// save the result into a *.java file, same level as the original file
		File javaFile = getAsJavaFile(file);
		writeToFile(javaFile, result);
	}
	
	/*
	 * Given a *.javax File, this method returns a *.java File at the same directory location  
	 */
	private static File getAsJavaFile(File javaxFile) {
		String javaxFileName = javaxFile.getName();
		File containingDirectory = javaxFile.getAbsoluteFile().getParentFile();
		String path = containingDirectory.getAbsolutePath()+System.getProperty("file.separator");
//		String path = "src" + System.getProperty("file.separator") + "se701" + System.getProperty("file.separator");
		String javaFilePath = path + javaxFileName.substring(0,javaxFileName.lastIndexOf("."))+".java";
		return new File(javaFilePath);
	}
	
	/*
	 * Given the specified file, writes the contents into it.
	 */
	private static void writeToFile(File file, String contents) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.print(contents);
		writer.close();
	}
}
