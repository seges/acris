package sk.seges.corpis.pap.model.domain.data;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.appscaffold.model.pap.DomainDataInterfaceProcessor;
import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.pap.MockData;
import sk.seges.corpis.pap.MockData.MockDomainModel;
import sk.seges.corpis.pap.MockData.MockiestDomainModel;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class DomainDataInterfaceProcessorTest extends AnnotationTest {

	@Test
	public void testDomainDataInterfaceForJava() {
		 assertCompilationSuccessful(compileFiles(Compiler.JAVAC, MockData.class));
		 assertOutput(getResourceFile(MockDomainModel.class), getOutputClass(MockDomainModel.class));
		 assertOutput(getResourceFile(MockiestDomainModel.class), getOutputClass(MockiestDomainModel.class));
	}

	@Test
	public void testDomainDataInterfaceForEclipse() {
		 assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, MockData.class));
		 assertOutput(getResourceFile(MockDomainModel.class), getOutputClass(MockDomainModel.class));
		 assertOutput(getResourceFile(MockiestDomainModel.class), getOutputClass(MockiestDomainModel.class));
	}

	private File getOutputClass(Class<?> clazz) {
		return toFile(new DomainDataInterfaceType(toMutable(clazz), processingEnv));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DomainDataInterfaceProcessor() };
	}

}
