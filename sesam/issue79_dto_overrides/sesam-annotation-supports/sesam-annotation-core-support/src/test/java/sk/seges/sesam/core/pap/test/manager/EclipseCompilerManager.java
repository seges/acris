package sk.seges.sesam.core.pap.test.manager;

import java.io.File;
import java.util.Collection;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

public class EclipseCompilerManager extends JavaCompilerManager {
	
	public EclipseCompilerManager() {
		super(new EclipseCompiler());
	}

	@Override
	protected void printCompilerOptions(Collection<File> compilationUnits) {
		System.out.println();
		System.out.println("Starting eclipse compiler:");
		System.out.print("java -jar ecj.jar -classpath rt.jar ");
	}	
}