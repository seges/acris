package sk.seges.sesam.pap.service.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.service.ServiceInterfaceProcessor;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.service.MockRemoteService;
import sk.seges.sesam.pap.service.service.TypedRemoteService;

public class ServiceInterfaceProcessorTest extends AnnotationTest {

	@Test
	public void testLocalService() {
		assertCompilationSuccessful(compileFiles(MockRemoteService.class));
		assertOutput(getResourceFile(MockRemoteService.class), getOutputFile(MockRemoteService.class));
	}

	@Test
	public void testLocalServiceInEclipse() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, MockRemoteService.class));
		assertOutput(getResourceFile(MockRemoteService.class), getOutputFile(MockRemoteService.class));
	}

	@Test
	public void testTypedService() {
		assertCompilationSuccessful(compileFiles(TypedRemoteService.class));
		assertOutput(getResourceFile(TypedRemoteService.class), getOutputFile(TypedRemoteService.class));
	}

	private File getOutputFile(Class<?> clazz) {
		MutableDeclaredType inputClass = toMutable(clazz);
		LocalServiceTypeElement outputClass = new RemoteServiceTypeElement(
				processingEnv.getElementUtils().getTypeElement(inputClass.getCanonicalName()), processingEnv).getLocalServiceElement();
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new ServiceInterfaceProcessor()
		};
	}
}