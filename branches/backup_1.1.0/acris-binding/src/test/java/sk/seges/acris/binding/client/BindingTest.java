/**
 * 
 */
package sk.seges.acris.binding.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.junit.Ignore;
import org.junit.Test;

import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.holder.ConfigurableBinding;
import sk.seges.acris.binding.client.holder.IBindingHolder;
import sk.seges.acris.binding.client.providers.wrapper.BeanWrapperAdapterProvider;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;


/**
 * @author ladislav.gazo
 */
public class BindingTest {
	@Test
	@Ignore
	public void testBindingChain() throws Exception {
		BeanAdapterFactory
				.addProvider(new sk.seges.acris.binding.client.providers.HasEnabledAdapterProvider());
		BeanAdapterFactory.addProvider(new BeanWrapperAdapterProvider());

		Forest forest = new Forest();
		ForestBeanWrapper wrapper = new ForestBeanWrapperImpl();
		wrapper.setBeanWrapperContent(forest);

		MockForestConfigurationPanel panel = new MockForestConfigurationPanel(UpdateStrategy.READ_WRITE,
				wrapper);
		panel.perilLevelBox.setEnabled(false);

		Binding.alter(panel).enable(panel.perilLevelBox).when(ForestBeanWrapper.TIGERS_IN_VICINITY)
				.isNotNull();

		wrapper.setBeanAttribute(ForestBeanWrapper.TIGERS_IN_VICINITY, 7);
		assertTrue(panel.perilLevelBox.isEnabled());
		wrapper.setBeanAttribute(ForestBeanWrapper.TIGERS_IN_VICINITY, null);
		assertFalse(panel.perilLevelBox.isEnabled());
	}

	public class MockForestConfigurationPanel extends BindingHolder<Forest> implements
			IBindingHolder<Forest>, ConfigurableBinding<Forest> {
		private BeanWrapper<Forest> beanWrapper;

		public MockForestConfigurationPanel(UpdateStrategy updateStrategy, BeanWrapper<Forest> beanWrapper) {
			super(updateStrategy, beanWrapper);
			this.beanWrapper = beanWrapper;
		}

		protected MockTextBox perilLevelBox = new MockTextBox();
		protected MockTextBox tigersInVicinityBox = new MockTextBox();

		@Override
		public IBindingHolder<Forest> getHolder() {
			return this;
		}

		@Override
		public BeanWrapper<Forest> getBeanWrapper() {
			return beanWrapper;
		}
	}
}
