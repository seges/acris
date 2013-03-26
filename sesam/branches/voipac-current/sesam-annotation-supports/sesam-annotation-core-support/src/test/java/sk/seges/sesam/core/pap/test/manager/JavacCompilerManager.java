package sk.seges.sesam.core.pap.test.manager;

import java.io.File;
import java.util.Collection;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JavacCompilerManager extends JavaCompilerManager {

	public JavacCompilerManager() {
		super(ToolProvider.getSystemJavaCompiler());
	}

	@Override
	protected void printCompilerOptions(Collection<File> compilationUnits) {
		System.out.println();
		System.out.println("Starting java compiler:");
		System.out.print("javac ");
		super.printCompilerOptions(compilationUnits);
	}
	
	@Override
	protected void validateCompiler(JavaCompiler compiler) {
		if (compiler == null) {
			throw new RuntimeException("Please use JDK for runing the tests!");
		}
	}
}