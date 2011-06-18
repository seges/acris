package sk.seges.sesam.pap.model;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.AnnotationTest;
import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.shared.model.mock.MockEntity;

public class MetaModelProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(MockEntity.class));
		assertOutput(getResourceFile(MockEntity.class), getOutputFile(MockEntity.class));
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = MetaModelProcessor.getOutputClass(inputClass, new DefaultPackageValidatorProvider());
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new MetaModelProcessor()
		};
	}		
}