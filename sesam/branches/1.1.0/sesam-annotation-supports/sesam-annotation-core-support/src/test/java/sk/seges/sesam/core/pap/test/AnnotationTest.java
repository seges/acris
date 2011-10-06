package sk.seges.sesam.core.pap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.TestClass;
import sk.seges.sesam.core.pap.test.model.utils.TestProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ClassFinder;

public abstract class AnnotationTest {

	private static final String TEST_SOURCE_FOLDER = "src/test/java";
	private static final String MAIN_SOURCE_FOLDER = "src/main/java";
	protected static final String SOURCE_FILE_SUFFIX = ".java";
	protected static final String OUTPUT_FILE_SUFFIX = ".output";
	protected static final String OUTPUT_DIRECTORY = "target/generated-test";

	private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

	protected enum CompilerOptions {
		GENERATED_SOURCES_DIRECTORY("-s <directory>", "<directory>", "Specify where to place generated source files"), GENERATED_CLASSES_DIRECTORY(
				"-d <directory>", "<directory>", "Specify where to place generated class files"), ;

		private String option;
		private String description;
		private String parameter;

		CompilerOptions(String option, String parameter, String description) {
			this.option = option;
			this.parameter = parameter;
			this.description = description;
		}

		public String getOption() {
			return option;
		}

		public String[] getOption(String parameterValue) {
			if (parameter != null) {

				String[] result = new String[2];

				int index = option.indexOf(parameter);
				result[0] = option.substring(0, index).trim();
				result[1] = parameterValue;
				return result;
			}

			return new String[] { getOption() };
		}

		public String getDescription() {
			return description;
		}
	}

	protected MutableDeclaredType toMutable(Class<?> clazz) {
		return new TestClass(clazz.getPackage().getName(), clazz.getSimpleName(), processingEnv);
	}
	
	protected MutableProcessingEnvironment processingEnv = new MutableProcessingEnvironment(new TestProcessingEnvironment());
	
	/**
	 * @return the processor instances that should be tested
	 */
	protected abstract Processor[] getProcessors();

	protected String getTestSourceFolder() {
		return TEST_SOURCE_FOLDER;
	}

	protected String getMainSourceFolder() {
		return MAIN_SOURCE_FOLDER;
	}

	protected String[] getCompilerOptions() {
		return CompilerOptions.GENERATED_SOURCES_DIRECTORY.getOption(ensureOutputDirectory().getAbsolutePath());
	}

	protected String toPath(Package packageName) {
		return toPath(packageName.getName());
	}

	protected String toPath(String packageName) {
		return packageName.replace(".", "/");
	}

	protected File getResourceFile(Class<?> clazz) {
		return new File(getClass().getResource(
				"/" + toPath(clazz.getPackage()) + "/" + clazz.getSimpleName() + OUTPUT_FILE_SUFFIX).getFile());
	}

	protected File ensureOutputDirectory() {
		File file = new File(OUTPUT_DIRECTORY);
		if (!file.exists()) {
			file.mkdirs();
		}

		return file;
	}

	protected static void assertOutput(File expectedResult, File output) {
		String[] expectedContent = getContents(expectedResult);
		String[] outputContent = getContents(output);
		assertEquals(expectedContent.length, outputContent.length);

		for (int i = 0; i < expectedContent.length; i++) {
			assertEquals(expectedContent[i].trim(), outputContent[i].trim());
		}
	}

	private static String[] getContents(File file) {
		List<String> content = new ArrayList<String>();

		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			try {
				String line = null; // not declared within while loop
				while ((line = input.readLine()) != null) {
					content.add(line);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return content.toArray(new String[] {});
	}

	/**
	 * Attempts to compile the given compilation units using the Java Compiler API.
	 * <p>
	 * The compilation units and all their dependencies are expected to be on the classpath.
	 * 
	 * @param compilationUnits
	 *            the classes to compile
	 * @return the {@link Diagnostic diagnostics} returned by the compilation, as demonstrated in the documentation for
	 *         {@link JavaCompiler}
	 * @see #compileFiles(String...)
	 */
	protected List<Diagnostic<? extends JavaFileObject>> compileFiles(Type... compilationUnits) {
		assert (compilationUnits != null);

		List<File> files = new ArrayList<File>();

		addCollection(files, compilationUnits);

		return compileFiles(files);
	}

	protected List<Diagnostic<? extends JavaFileObject>> compileFiles(Package... compilationUnits) {
		assert (compilationUnits != null);

		List<File> files = new ArrayList<File>();

		addCollection(files, compilationUnits);

		return compileFiles(files);
	}

	private void addCollection(List<File> files, Package[] compilationUnits) {
		if (compilationUnits == null) {
			return;
		}
		for (Package element: compilationUnits) {
			assert (element != null);

			ClassFinder classFinder = new ClassFinder();
			addCollection(files, classFinder.findClassesInPackage(((Package)element).getName()));
		}
	}

	protected <T extends AnnotatedElement> void addCollection(List<File> files, Collection<T> compilationUnits) {
		if (compilationUnits == null) {
			return;
		}
		addCollection(files, compilationUnits.toArray(new Type[] {}));
	}

	protected <T extends Type> void addCollection(List<File> files, T... compilationUnits) {
		if (compilationUnits == null) {
			return;
		}
		for (T element : compilationUnits) {
			assert (element != null);

			if (element instanceof Class<?>) {
				File file = toFile(((Class<?>) element));
				if (file != null) {
					files.add(file);
				} else {
					// These are innerclasses, etc ... that should not be defined in this way
				}
			} else if (element instanceof Package) {
				ClassFinder classFinder = new ClassFinder();
				addCollection(files, classFinder.findClassesInPackage(((Package) element).getName()));
			}
		}
	}

	private String convertClassNameToResourcePath(String name) {
		return name.replace(".", File.separator);
	}

	private File toFile(Class<?> clazz) {
		File file = new File(getTestSourceFolder() + File.separator
				+ convertClassNameToResourcePath(clazz.getCanonicalName()) + SOURCE_FILE_SUFFIX);
		if (!file.exists()) {
			file = new File(getMainSourceFolder() + File.separator
					+ convertClassNameToResourcePath(clazz.getCanonicalName()) + SOURCE_FILE_SUFFIX);
			if (!file.exists()) {
				return null;
			}
		}
		return file;
	}

	private List<String> mergeCompilerOptions(List<String> options) {

		if (options == null) {
			return Arrays.asList(getCompilerOptions());
		}
		List<String> result = new ArrayList<String>();

		for (String option : options) {
			result.add(option);
		}

		for (String option : getCompilerOptions()) {
			result.add(option);
		}

		return result;
	}

	protected StandardJavaFileManager fileManager;

	protected String getClassPath() {
		String classPath = System.getProperty("maven.test.class.path");
		if (classPath == null || classPath.length() == 0) {
			return System.getProperty("java.class.path");
		}

		classPath = classPath.replaceAll(", ", isWindows() ? ";" : ":").trim();
		return "\"" + classPath.substring(1, classPath.length() - 2).trim() + ";"
				+ new File("target\\classes").getAbsolutePath() + "\"";
	}

	private String getOsName() {
      return System.getProperty("os.name");
	}

    private boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	protected List<Diagnostic<? extends JavaFileObject>> compileFiles(Collection<File> compilationUnits) {
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
		List<String> compilerOptions = mergeCompilerOptions(Arrays.asList("-proc:only", "-classpath", getClassPath(), "-Aclasspath=" + getClassPath(), "-AprojectName=" + getClass().getCanonicalName()));

		System.out.println();
		System.out.println("Starting java compiler:");
		System.out.print("javac ");
		for (String option : compilerOptions) {
			System.out.print(option + " ");
		}
		CompilationTask task = COMPILER.getTask(null, fileManager, diagnosticCollector, compilerOptions, null,
				fileManager.getJavaFileObjectsFromFiles(compilationUnits));
		List<Processor> processors = new ArrayList<Processor>();
		for (Processor processor : getProcessors()) {
			processors.add(processor);
		}
		task.setProcessors(processors);
		System.out.print("-processor ");
		for (Processor processor : processors) {
			System.out.print(processor.getClass().getCanonicalName() + " ");
		}
		System.out.print(" ");
		for (File file : compilationUnits) {
			System.out.print(file.getName() + " ");
		}
		System.out.println();
		System.out.println();

		task.call();

		try {
			fileManager.close();
		} catch (IOException exception) {
		}

		return diagnosticCollector.getDiagnostics();
	}

	protected static void assertCompilationSuccessful(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
		assert (diagnostics != null);

		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
			System.out.println(diagnostic.toString());
		}

		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
			assertFalse("Expected no errors", diagnostic.getKind().equals(Kind.ERROR));
		}

	}

	/**
	 * Asserts that the compilation produced results of the following {@link Kind Kinds} at the given line numbers,
	 * where the <em>n</em>th kind is expected at the <em>n</em>th line number.
	 * <p>
	 * Does not check that these is the <em>only</em> diagnostic kinds returned!
	 * 
	 * @param expectedDiagnosticKinds
	 *            the kinds of diagnostic expected
	 * @param expectedLineNumber
	 *            the line numbers at which the diagnostics are expected
	 * @param diagnostics
	 *            the result of the compilation
	 * @see #assertCompilationSuccessful(List)
	 * @see #assertCompilationReturned(Kind, long, List)
	 */
	protected static void assertCompilationReturned(Kind[] expectedDiagnosticKinds, long[] expectedLineNumbers,
			List<Diagnostic<? extends JavaFileObject>> diagnostics) {
		assert ((expectedDiagnosticKinds != null) && (expectedLineNumbers != null) && (expectedDiagnosticKinds.length == expectedLineNumbers.length));

		for (int i = 0; i < expectedDiagnosticKinds.length; i++) {
			assertCompilationReturned(expectedDiagnosticKinds[i], expectedLineNumbers[i], diagnostics);
		}

	}

	/**
	 * Asserts that the compilation produced a result of the following {@link Kind} at the given line number.
	 * <p>
	 * Does not check that this is the <em>only</em> diagnostic kind returned!
	 * 
	 * @param expectedDiagnosticKind
	 *            the kind of diagnostic expected
	 * @param expectedLineNumber
	 *            the line number at which the diagnostic is expected
	 * @param diagnostics
	 *            the result of the compilation
	 * @see #assertCompilationSuccessful(List)
	 * @see #assertCompilationReturned(Kind[], long[], List)
	 */
	protected static void assertCompilationReturned(Kind expectedDiagnosticKind, long expectedLineNumber,
			List<Diagnostic<? extends JavaFileObject>> diagnostics) {
		assert ((expectedDiagnosticKind != null) && (diagnostics != null));
		boolean expectedDiagnosticFound = false;

		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {

			if (diagnostic.getKind().equals(expectedDiagnosticKind)
					&& (diagnostic.getLineNumber() == expectedLineNumber)) {
				expectedDiagnosticFound = true;
			}
		}

		assertTrue("Expected a result of kind " + expectedDiagnosticKind + " at line " + expectedLineNumber,
				expectedDiagnosticFound);
	}
}