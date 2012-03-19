package sk.seges.sesam.pap.hashcode;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.pap.core.DtoTestCase;
import sk.seges.sesam.pap.model.TransferObjectProcessor;

public class EntityWithHashcodeTestCase extends DtoTestCase {

	@Test
	public void testCopyAnnotation() {
		assertCompilationSuccessful(compileFiles(EntityWithHashcodeDTOConfiguration.class));
		assertOutput(getResourceFile(EntityWithHashcodeDTOConfiguration.class), getOutputFile(EntityWithHashcodeDTOConfiguration.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new TransferObjectProcessor() };
	}
}
