package sk.seges.sesam.core.pap.test.cases;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.core.pap.test.cases.annotation.SuperclassTestAnnotation;
import sk.seges.sesam.core.pap.test.cases.model.MultipleBoundsModel;
import sk.seges.sesam.core.pap.test.cases.model.NestedBoundsModel;
import sk.seges.sesam.core.pap.test.cases.processor.SuperclassProcessor;
import sk.seges.sesam.core.pap.test.cases.processor.SuperclassProcessor.SuperclassAwareType;

public class SuperclassProcessorTestCase extends AnnotationTest {

	@Test
	public void testNestedBounds() {
		assertCompilationSuccessful(compileFiles(SuperclassTestAnnotation.class, NestedBoundsModel.class));
		assertOutput(getResourceFile(NestedBoundsModel.class), getOutputFile(NestedBoundsModel.class));
	}

	@Test
	public void testUpperBounds() {
		assertCompilationSuccessful(compileFiles(SuperclassTestAnnotation.class, MultipleBoundsModel.class));
		assertOutput(getResourceFile(MultipleBoundsModel.class), getOutputFile(MultipleBoundsModel.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new SuperclassProcessor()
		};
	}

	private File getOutputFile(Class<?> clazz) {
		MutableDeclaredType outputClass = toMutable(clazz).addClassSufix(SuperclassAwareType.SUFFIX);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
}