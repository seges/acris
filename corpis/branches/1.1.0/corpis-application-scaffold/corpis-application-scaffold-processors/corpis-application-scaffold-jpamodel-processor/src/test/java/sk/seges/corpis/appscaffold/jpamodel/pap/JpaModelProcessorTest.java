package sk.seges.corpis.appscaffold.jpamodel.pap;

import org.junit.Test;

import sk.seges.corpis.appscaffold.datainterface.pap.DataInterfaceProcessor;
import sk.seges.sesam.core.pap.test.FluentProcessorTest;


/**
 * @author ladislav.gazo
 */
public class JpaModelProcessorTest extends FluentProcessorTest {
	public JpaModelProcessorTest() {
		addProcessor(new JpaModelProcessor(), new DataInterfaceProcessor());
	}
	
	@Test
	public void testProcessorWithoutReferences() {
		assertCompilationSuccessful(compileFiles(ThemeModel.class, JpaThemeModel.class));
	}
	
	@Test
	public void testProcessorWithReferences() {
		assertCompilationSuccessful(compileFiles(CustomerModel.class, RoleModel.class, JpaCustomerModel.class, JpaRoleModel.class));
	}
}
