package sk.seges.acris.pap.service.test;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.pap.service.AsyncServiceProcessor;
import sk.seges.acris.pap.service.model.DummyService;
import sk.seges.acris.pap.service.model.TypedService;
import sk.seges.acris.pap.service.model.VarargsService;

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
	public void testVarargsService() {
		assertCompilationFailed(compileFiles(VarargsService.class), "Varargs are forbidden");
	}

	@Test
	public void testVarargsServiceInEclipse() {
		//Again there is a bug in the eclipse processor classes and it even not recognize
		//that there is a varargs parameters - it assumets that it is an array and cannot be
		//determined correct type
		//assertCompilationFailed(compileFiles(Compiler.ECLIPSE, VarargsService.class));
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