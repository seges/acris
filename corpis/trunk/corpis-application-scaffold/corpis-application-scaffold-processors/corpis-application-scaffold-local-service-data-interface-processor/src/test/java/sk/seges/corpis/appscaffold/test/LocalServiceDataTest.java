package sk.seges.corpis.appscaffold.test;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.appscaffold.model.pap.LocalServiceDataInterfaceProcessor;
import sk.seges.corpis.appscaffold.service.MockRemoteService;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class LocalServiceDataTest extends AnnotationTest {

	@Test
	public void testLocalServiceDataInterfaceForJava() {
		 assertCompilationSuccessful(compileFiles(Compiler.JAVAC, MockRemoteService.class));
	}

	@Test
	public void testLocalServiceDataInterfaceForEclipe() {
		 assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, MockRemoteService.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new LocalServiceDataInterfaceProcessor()
		};
	}

}
