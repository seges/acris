package sk.seges.sesam.pap.validation;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.pap.core.DtoTestCase;
import sk.seges.sesam.pap.model.TransferObjectProcessor;

public class EntityWithValidationTestCase extends DtoTestCase {

	@Test
	public void testCopyAnnotationByPackage() {
		assertCompilationSuccessful(compileFiles(ValidationPackageDTOConfiguration.class));
		assertOutput(getResourceFile(ValidationPackageDTOConfiguration.class), getOutputFile(ValidationPackageDTOConfiguration.class));
	}

	@Test
	public void testCopyAnnotationByTypes() {
		assertCompilationSuccessful(compileFiles(ValidationTypesDTOConfiguration.class));
		assertOutput(getResourceFile(ValidationTypesDTOConfiguration.class), getOutputFile(ValidationTypesDTOConfiguration.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new TransferObjectProcessor() };
	}
}