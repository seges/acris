package sk.seges.corpis.appscaffold.datainterface.pap;

import org.junit.Test;

import sk.seges.sesam.core.pap.test.FluentProcessorTest;


/**
 * @author ladislav.gazo
 */
public class DataInterfaceProcessorTest extends FluentProcessorTest {
	public DataInterfaceProcessorTest() {
		addProcessor(new DataInterfaceProcessor());
	}
	
	@Test
	public void testProcessor() {
		assertCompilationSuccessful(compileFiles(ThemeModel.class));
	}
}
