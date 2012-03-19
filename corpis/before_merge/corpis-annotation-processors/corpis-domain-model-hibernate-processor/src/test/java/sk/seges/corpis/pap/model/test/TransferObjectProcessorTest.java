package sk.seges.corpis.pap.model.test;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.pap.model.configuration.DataInterfaceConfiguration;
import sk.seges.corpis.pap.model.configuration.SuperclassEntityDTOConfiguration;
import sk.seges.corpis.pap.model.hibernate.HibernateTransferObjectProcessor;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class TransferObjectProcessorTest extends AnnotationTest {

	@Test
	public void testDto() {
		assertCompilationSuccessful(compileFiles(Compiler.JAVAC, DataInterfaceConfiguration.class));
		//assertOutput(getResourceFile(MockData.class), getOutputFile(MockEntityDTOConfiguration.class));
	}

	@Test
	public void testSuperclassJAVAC() {
		assertCompilationSuccessful(compileFiles(Compiler.JAVAC, SuperclassEntityDTOConfiguration.class));
	}
	
	@Test
	public void testSuperclassECLIPSE() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, SuperclassEntityDTOConfiguration.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new HibernateTransferObjectProcessor()
		};
	}
}
