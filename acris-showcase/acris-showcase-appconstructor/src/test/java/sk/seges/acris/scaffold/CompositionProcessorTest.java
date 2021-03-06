/**
 * 
 */
package sk.seges.acris.scaffold;

import org.junit.Test;

import sk.seges.crm.client.activity.ActivitiesManagementComposition;
import sk.seges.pap.CompositionProcessor;
import sk.seges.pap.composition.CompositionActivatorProcessor;
import sk.seges.pap.detail.SelectedDetailViewDisplayProcessor;
import sk.seges.pap.detail.SelectedDetailViewPanelProcessor;
import sk.seges.pap.detail.SelectedDetailViewPresenterProcessor;
import sk.seges.pap.singleselect.SingleSelectViewDisplayProcessor;
import sk.seges.pap.singleselect.SingleSelectViewPanelProcessor;
import sk.seges.pap.singleselect.SingleSelectViewPresenterProcessor;
import sk.seges.sesam.core.pap.test.FluentProcessorTest;

/**
 * @author ladislav.gazo
 * 
 */
public class CompositionProcessorTest extends FluentProcessorTest {
	public CompositionProcessorTest() {
		addProcessor(new CompositionProcessor(),
				new SingleSelectViewPanelProcessor(),
				new SingleSelectViewDisplayProcessor(),
				new SingleSelectViewPresenterProcessor(),
				new SelectedDetailViewPanelProcessor(),
				new SelectedDetailViewDisplayProcessor(),
				new SelectedDetailViewPresenterProcessor(),
				new CompositionActivatorProcessor());
	}

	@Test
	public void testSimpleComposition() {
		assertCompilationSuccessful(compileFiles(ActivitiesManagementComposition.class));
	}

}
