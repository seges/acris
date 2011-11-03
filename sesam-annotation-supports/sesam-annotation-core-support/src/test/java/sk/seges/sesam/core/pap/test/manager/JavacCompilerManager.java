package sk.seges.sesam.core.pap.test.manager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class JavacCompilerManager extends AbstractCompilerManager {

	protected StandardJavaFileManager fileManager;

	private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

	public JavacCompilerManager(Collection<File> compilationUnits) {
		super(compilationUnits);
	}

	@Override
	public List<Diagnostic<? extends JavaFileObject>> run() {
		DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
		if (COMPILER == null) {
			throw new RuntimeException("Please use JDK for runing the tests!");
		}
		fileManager = COMPILER.getStandardFileManager(diagnosticCollector, null, null);

		/*
		 * Call the compiler with the "-proc:only" option. The "class names" option (which could, in principle, be used
		 * instead of compilation units for annotation processing) isn't useful in this case because only annotations on
		 * the classes being compiled are accessible.
		 * 
		 * Information about the classes being compiled (such as what they are annotated with) is *not* available via
		 * the RoundEnvironment. However, if these classes are annotations, they certainly need to be validated.
		 */
		System.out.println();
		System.out.println("Starting java compiler:");
		System.out.print("javac ");
		printCompilerOptions();
		
		CompilationTask task = COMPILER.getTask(null, fileManager, diagnosticCollector, compilerOptions, null,
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