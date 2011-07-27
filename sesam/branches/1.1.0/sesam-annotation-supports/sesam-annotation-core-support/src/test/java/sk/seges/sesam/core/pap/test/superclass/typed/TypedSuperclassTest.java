package sk.seges.sesam.core.pap.test.superclass.typed;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class TypedSuperclassTest extends AnnotationTest {

	@Test
	public void testNestedBounds() {
		assertCompilationSuccessful(compileFiles(SuperClassMarker.class, TestBeanNestedBounds.class));
		assertOutput(getResourceFile(TestBeanNestedBounds.class), getOutputFile(TestBeanNestedBounds.class));
	}

	@Test
	public void testUpperBounds() {
		assertCompilationSuccessful(compileFiles(SuperClassMarker.class, TestBeanUpperBounds.class));
		assertOutput(getResourceFile(TestBeanUpperBounds.class), getOutputFile(TestBeanUpperBounds.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new SuperclassProcessor()
		};
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = SuperclassProcessor.getOutputClass(inputClass, new DefaultPackageValidatorProvider());
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
}