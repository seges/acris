package sk.seges.sesam.core.pap.test.manager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

public class EclipseCompilerManager extends AbstractCompilerManager {
	
	protected StandardJavaFileManager fileManager;

	public EclipseCompilerManager(Collection<File> compilationUnits) {
		super(compilationUnits);
	}

	@Override
	public List<Diagnostic<? extends JavaFileObject>> run() {

		System.out.println();
		System.out.println("Starting eclipse compiler:");
		System.out.print("java -jar ecj.jar -classpath rt.jar ");
		printCompilerOptions();

		DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();

		EclipseCompiler compiler = new EclipseCompiler();
		fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);

		CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, compilerOptions, null,
				fileManager.getJavaFileObjectsFromFiles(compilationUnits));
	
		task.setProcessors(processors);
		task.call();

		try {
			fileManager.close();
		} catch (IOException exception) {
		}

		return diagnosticCollector.getDiagnostics();
	}
}