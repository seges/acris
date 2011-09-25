package sk.seges.corpis.appscaffold.jpamodel.pap;

import org.junit.Test;

import sk.seges.corpis.appscaffold.datainterface.pap.DataInterfaceProcessor;
import sk.seges.sesam.core.pap.test.FluentProcessorTest;
import sk.seges.sesam.pap.model.MetaModelProcessor;


/**
 * @author ladislav.gazo
 */
public class JpaModelProcessorTest extends FluentProcessorTest {
	public JpaModelProcessorTest() {
		addProcessor(new JpaModelProcessor(), new MetaModelProcessor(), new DataInterfaceProcessor());
	}
	
	@Test
	public void testProcessor() {
		assertCompilationSuccessful(compileFiles(ThemeModel.class, JpaThemeModel.class));
	}
}
