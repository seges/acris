package sk.seges.corpis.pap.service.hibernate.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.pap.service.hibernate.HibernateServiceInterfaceProcessor;
import sk.seges.corpis.pap.service.hibernate.service.MockRemoteService;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;

public class HibernateServiceInterfaceProcessorTest extends AnnotationTest {

	@Test
	public void testLocalService() {
		assertCompilationSuccessful(compileFiles(EnvironmentOptions.ECLIPSE, MockRemoteService.class));
		assertOutput(getResourceFile(MockRemoteService.class), getOutputFile(MockRemoteService.class));
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
			new HibernateServiceInterfaceProcessor()
		};
	}
}