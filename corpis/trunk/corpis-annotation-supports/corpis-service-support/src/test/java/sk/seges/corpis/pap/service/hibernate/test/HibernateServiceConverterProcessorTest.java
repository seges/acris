package sk.seges.corpis.pap.service.hibernate.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.pap.service.hibernate.HibernateServiceConverterProcessor;
import sk.seges.corpis.pap.service.hibernate.service.MockService;
import sk.seges.corpis.pap.service.hibernate.service.TransactionalMockService;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class HibernateServiceConverterProcessorTest extends AnnotationTest {

	@Test
	public void testServiceConveter() {
		assertCompilationSuccessful(compileFiles(MockService.class));
		assertOutput(getResourceFile(MockService.class), getOutputFile(MockService.class));
	}

	@Test
	public void testServiceConveterForEclipse() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, MockService.class));
		assertOutput(getEclipseResourceFile(MockService.class), getOutputFile(MockService.class));
	}

	@Test
	public void testTransactionalServiceConveter() {
		assertCompilationSuccessful(compileFiles(TransactionalMockService.class));
		assertOutput(getResourceFile(TransactionalMockService.class), getOutputFile(TransactionalMockService.class));
	}

	public File getOutputFile(Class<?> type) {
		MutableDeclaredType mutableType = toMutable(type);
		ServiceTypeElement serviceTypeElement = new ServiceTypeElement(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()), processingEnv);
		return toFile(serviceTypeElement.getServiceConverter());
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new HibernateServiceConverterProcessor()
		};
	}
}