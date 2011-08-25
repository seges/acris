package sk.seges.sesam.pap.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.model.PagedResultConfiguration;
import sk.seges.sesam.pap.model.TransferObjectConverterProcessor;
import sk.seges.sesam.pap.model.TransferObjectProcessor;

public class PagedResultTest extends AnnotationTest {

	@Test
	public void testNestedBounds() {
		assertCompilationSuccessful(compileFiles(PagedResultConfiguration.class));
	//	assertOutput(getResourceFile(TestBeanNestedBounds.class), getOutputFile(TestBeanNestedBounds.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new TransferObjectProcessor(),
			new TransferObjectConverterProcessor()
		};
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = TransferObjectProcessor.getOutputClass(inputClass);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
}
