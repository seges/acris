package sk.seges.sesam.pap.core;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.pap.model.TransferObjectProcessor;

public class EntityTestCase extends DtoTestCase {

	@Test
	public void testCopyAnnotation() {
		assertCompilationSuccessful(compileFiles(EntityDTOConfiguration.class));
		assertOutput(getResourceFile(EntityDTOConfiguration.class), getOutputFile(EntityDTOConfiguration.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new TransferObjectProcessor() };
	}
}