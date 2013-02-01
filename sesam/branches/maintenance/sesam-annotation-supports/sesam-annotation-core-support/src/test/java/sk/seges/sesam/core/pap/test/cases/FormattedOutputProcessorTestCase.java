package sk.seges.sesam.core.pap.test.cases;

import java.io.File;
import java.net.URL;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.core.pap.test.cases.model.BasicModel;
import sk.seges.sesam.core.pap.test.cases.processor.FormattedOutputAnnotationProcessor;

public class FormattedOutputProcessorTestCase extends AnnotationTest {

	@Test
	public void testNestedBounds() {
		assertCompilationSuccessful(compileFiles(BasicModel.class));
		assertOutput(getResourceFile(BasicModel.class), getOutputFile(BasicModel.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new FormattedOutputAnnotationProcessor()
		};
	}
	
	private File getOutputFile(Class<?> clazz) {
		MutableDeclaredType outputClass = toMutable(clazz).addClassSufix(FormattedOutputAnnotationProcessor.SUFFIX);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
	
	protected File getResourceFile(Class<?> clazz) {
		URL resource = getClass().getResource(
				"/" + toPath(clazz.getPackage()) + "/" + clazz.getSimpleName() + ".formatted" + OUTPUT_FILE_SUFFIX );
		
		if (resource == null) {
			throw new RuntimeException("Unable to find output file " + 
					"/" + toPath(clazz.getPackage()) + "/" + clazz.getSimpleName() + ".formatted" + OUTPUT_FILE_SUFFIX );
		}
		
		return new File(resource.getFile());
	}

}
