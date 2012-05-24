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
		assertOutput(getResourceFile(DummyService.class), getOutputFile(DummyService.class));
	}

	@Test
	public void testAsyncServiceInEclipse() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, DummyService.class));
		assertOutput(getResourceFile(DummyService.class), getOutputFile(DummyService.class));
	}

	@Test
	public void testTypedAsyncService() {
		assertCompilationSuccessful(compileFiles(TypedService.class));
		assertOutput(getResourceFile(TypedService.class), getOutputFile(TypedService.class));
	}

	@Test
	public void testTypedAsyncServiceInEclipse() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, TypedService.class));
		assertOutput(getResourceFile(TypedService.class), getOutputFile(TypedService.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new AsyncServiceProcessor()
		};
	}
}