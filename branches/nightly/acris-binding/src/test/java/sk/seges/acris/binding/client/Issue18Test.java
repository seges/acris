/**
 * 
 */
package sk.seges.acris.binding.client;

import static org.junit.Assert.assertEquals;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.ui.client.adapters.HasTextAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.HasValueAdapterProvider;
import org.junit.Test;

import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.providers.wrapper.BeanWrapperAdapterProvider;

/**
 * @author ladislav.gazo
 */
public class Issue18Test {
	@Test
	public void testSimulate() throws Exception {
		BeanAdapterFactory.addProvider(new BeanWrapperAdapterProvider());
		BeanAdapterFactory.addProvider(new HasTextAdapterProvider());
		BeanAdapterFactory.addProvider(new HasValueAdapterProvider<String>());
		HasTextAdapterProvider.register(MockTextBox.class);
		HasValueAdapterProvider.<String> register(MockTextBox.class);

		String value = "nice";

		Forest forest = new Forest();
		ForestBeanWrapper wrapper = new ForestBeanWrapperImpl();

		MockTextBox textBox = new MockTextBox();

		BindingHolder<Forest> holder = new BindingHolder<Forest>(UpdateStrategy.READ_WRITE, wrapper);
		holder.addBinding(ForestBeanWrapper.DESCRIPTION, textBox, "value", null, null);
		if (holder.getBean() != null) {
			holder.bind();
		}

		holder.setBean(forest);
		wrapper.setBeanAttribute(ForestBeanWrapper.DESCRIPTION, value);
		assertEquals(value.toString(), textBox.getValue());
		
		forest = new Forest();
		holder.setBean(forest);
		assertEquals(forest.getDescription(), textBox.getValue());
	}
}
