package sk.seges.acris.binding.jsr269;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class InvalidPropertyTest extends AnnotationTest {

	@Test
	public void invadidProperty() {
		assertCompilationSuccessful(compileFiles(BeanWrapper.class, MockData.class));

	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new BeanWrapperProcessor()
		};
	}

}
