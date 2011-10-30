package sk.seges.acris.binding.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.binding.jsr269.EnclosingMockEntity;
import sk.seges.acris.binding.jsr269.EnclosingMockEntity.EnclosedMockEntity;
import sk.seges.acris.binding.pap.BeanWrapperProcessor;
import sk.seges.acris.binding.pap.model.BeanWrapperType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class Issue22Test extends AnnotationTest {

	@Test
	public void enclosedType() {
		assertCompilationSuccessful(compileFiles(EnclosingMockEntity.class));
		assertOutput(getResourceFile(EnclosedMockEntity.class), getOutputFile(EnclosedMockEntity.class));
	}
	
	protected File getOutputFile(Class<?> clazz) {
		MutableDeclaredType inputClass = toMutable(clazz);
		BeanWrapperType dto = new BeanWrapperType(inputClass, processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(dto.getPackageName()) + "/" + dto.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new BeanWrapperProcessor()
		};
	}
}