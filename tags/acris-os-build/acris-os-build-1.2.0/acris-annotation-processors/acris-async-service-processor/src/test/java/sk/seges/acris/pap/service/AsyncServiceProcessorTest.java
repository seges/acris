package sk.seges.acris.pap.service;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.test.AnnotationTest;

public class AsyncServiceProcessorTest extends AnnotationTest {

	@Test
	public void testAsyncService() {
		assertCompilationSuccessful(compileFiles(AsyncServiceProcessorTestConfiguration.class,
				DummyService.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new AsyncServiceProcessor()
		};
	}

}
