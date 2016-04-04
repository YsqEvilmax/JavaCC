package se701;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.visitor.SillyBreakVisitor;
import japa.parser.ast.visitor.TypeVisitor;
import japa.parser.ast.visitor.VariableVisitor;
import japa.parser.ast.visitor.ClassVisitor;
import japa.parser.ast.visitor.DumpVisitor;
import japa.parser.ast.visitor.MethodVisitor;
import japa.parser.ast.visitor.ScopeVisitor;

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
		ScopeVisitor scopes = new ScopeVisitor();
		ast.accept(scopes, null);

		// check types
		TypeVisitor types = new TypeVisitor();
		ast.accept(types, null);

		// check variables
		VariableVisitor variables = new VariableVisitor();
		ast.accept(variables, null);

		// Check that all classes have been accessed only after their
		// creation, and that any assignments are of the correct type.
		ClassVisitor classes = new ClassVisitor();
		ast.accept(classes, null);


		// Check that all method calls are to methods that exist, and pass in
		// the correct type for each parameter.
		MethodVisitor methods = new MethodVisitor();
		ast.accept(methods, null);
		
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
