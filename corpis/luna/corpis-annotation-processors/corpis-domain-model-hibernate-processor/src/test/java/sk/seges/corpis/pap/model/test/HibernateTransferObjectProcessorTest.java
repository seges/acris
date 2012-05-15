package sk.seges.corpis.pap.model.test;

import java.io.File;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;

import org.junit.Test;

import sk.seges.corpis.pap.model.configuration.MockEntityDTOConfiguration;
import sk.seges.corpis.pap.model.hibernate.HibernateTransferObjectProcessor;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.ConfigurationCache;

public class HibernateTransferObjectProcessorTest extends AnnotationTest {

	@Test
	public void testDto() {
		assertCompilationSuccessful(compileFiles(Compiler.JAVAC, MockEntityDTOConfiguration.class));
		assertOutput(getResourceFile(MockEntityDTOConfiguration.class), getOutputFile(MockEntityDTOConfiguration.class));
	}

	@Test
	public void testDtoInEclipse() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, MockEntityDTOConfiguration.class));
		assertOutput(getResourceFile(MockEntityDTOConfiguration.class), getOutputFile(MockEntityDTOConfiguration.class));
	}

	public File getOutputFile(Class<?> clazz) {
		TypeElement configurationElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
		TransferObjectProcessingEnvironment transferObjectProcessingEnvironment = new TransferObjectProcessingEnvironment(processingEnv, roundEnv, new ConfigurationCache());
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, transferObjectProcessingEnvironment.getEnvironmentContext(), null);
		return toFile(configurationTypeElement.getDto());
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new HibernateTransferObjectProcessor()
		};
	}
}