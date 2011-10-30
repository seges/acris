package sk.seges.acris.pap.service.test;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.pap.service.AsyncServiceProcessor;
import sk.seges.acris.pap.service.model.DummyService;
import sk.seges.acris.pap.service.model.TypedService;

public class AsyncServiceProcessorTest extends AsyncTest {

	@Test
	public void testAsyncService() {
		assertCompilationSuccessful(compileFiles(DummyService.class));
	}
	
	@Test
	public void testTypedAsyncService() {
		assertCompilationSuccessful(compileFiles(TypedService.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new AsyncServiceProcessor()
		};
	}
}