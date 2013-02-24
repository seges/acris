package sk.seges.sesam.pap.converter;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.configuration.MockEntityDtoConfiguration;

public class ConverterProviderTest extends AnnotationTest {

	@Test
	public void testMockEntity() {
		assertCompilationSuccessful(compileFiles(MockEntityDtoConfiguration.class));
//		assertOutput(getResourceFile(MockEntity.class), getOutputFile(MockEntity.class));
	}

//	private File getOutputFile(Class<?> clazz) {
//		MutableDeclaredType outputClass = toMutable(clazz).addClassSufix(MetaModelTypeElement.META_MODEL_SUFFIX);
//		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
//	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new ConverterProviderProcessor()
		};
	}		

}
