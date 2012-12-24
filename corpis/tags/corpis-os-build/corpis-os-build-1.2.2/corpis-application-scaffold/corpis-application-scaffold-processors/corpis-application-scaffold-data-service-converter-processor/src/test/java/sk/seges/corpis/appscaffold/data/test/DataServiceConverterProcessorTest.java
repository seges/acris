package sk.seges.corpis.appscaffold.data.test;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.appscaffold.data.service.MockService;
import sk.seges.corpis.appscaffold.model.pap.DataServiceConverterProcessor;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class DataServiceConverterProcessorTest extends AnnotationTest {

	@Test
	public void testServiceConveter() {
		assertCompilationSuccessful(compileFiles(MockService.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new DataServiceConverterProcessor()
			};
	}
}
