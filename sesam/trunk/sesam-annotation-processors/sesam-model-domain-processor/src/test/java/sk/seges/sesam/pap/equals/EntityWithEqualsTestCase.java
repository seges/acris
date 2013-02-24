package sk.seges.sesam.pap.equals;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.pap.core.DtoTestCase;
import sk.seges.sesam.pap.model.TransferObjectProcessor;

public class EntityWithEqualsTestCase extends DtoTestCase {

	@Test
	public void testCopyAnnotation() {
		assertCompilationSuccessful(compileFiles(EntityWithEqualsDTOConfiguration.class));
		assertOutput(getResourceFile(EntityWithEqualsDTOConfiguration.class), getOutputFile(EntityWithEqualsDTOConfiguration.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new TransferObjectProcessor() };
	}
}
