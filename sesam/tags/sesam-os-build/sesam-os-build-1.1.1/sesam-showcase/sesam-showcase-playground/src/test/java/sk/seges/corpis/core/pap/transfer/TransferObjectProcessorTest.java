package sk.seges.corpis.core.pap.transfer;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.dto.ParametrizedEntity;
import sk.seges.sesam.core.pap.AnnotationTest;

public class TransferObjectProcessorTest extends AnnotationTest {

	@Test
	public void testTransferObject() {
//		assertCompilationSuccessful(compileFiles(MockEntity.class));
		assertCompilationSuccessful(compileFiles(ParametrizedEntity.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new TransferObjectProcessor()
		};
	}
}