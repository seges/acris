package sk.seges.sesam.core.pap.test.cases;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.core.pap.test.cases.model.BasicModel;
import sk.seges.sesam.core.pap.test.cases.processor.BasicTestAnnotationProcessor;

public class BasicAnnotationProcessorTestCase extends AnnotationTest {

	@Test
	public void testNestedBounds() {
		assertCompilationSuccessful(compileFiles(BasicModel.class));
		assertOutput(getResourceFile(BasicModel.class), getOutputFile(BasicModel.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new BasicTestAnnotationProcessor()
		};
	}

	private File getOutputFile(Class<?> clazz) {
		MutableDeclaredType outputClass = toMutable(clazz).addClassSufix(BasicTestAnnotationProcessor.SUFFIX);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

}
