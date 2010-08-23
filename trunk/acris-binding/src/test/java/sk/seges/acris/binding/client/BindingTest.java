/**
 * 
 */
package sk.seges.acris.binding.client;

import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.junit.Test;

import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.holder.ConfigurableBinding;
import sk.seges.acris.binding.client.holder.IBindingHolder;
import sk.seges.acris.binding.client.providers.support.widget.HasEnabled;
import sk.seges.acris.binding.client.providers.wrapper.BeanWrapperAdapterProvider;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.core.rebind.RebindUtils;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

/**
 * @author ladislav.gazo
 */
public class BindingTest {
	@Test
//	@Ignore
	public void testBindingChain() throws Exception {
		BeanAdapterFactory.addProvider(new sk.seges.acris.binding.client.providers.HasEnabledAdapterProvider());
		BeanAdapterFactory.addProvider(new BeanWrapperAdapterProvider());
		
		Forest forest = new Forest();
		ForestBeanWrapper wrapper = new ForestBeanWrapperImpl();
		wrapper.setBeanWrapperContent(forest);

		MockForestConfigurationPanel panel = new MockForestConfigurationPanel(UpdateStrategy.READ_WRITE, wrapper);
		panel.setEnabled(false);

		Binding.alter(panel).enable(panel.perilLevelBox).when(ForestBeanWrapper.TIGERS_IN_VICINITY)
				.isNotNull();

		wrapper.setBeanAttribute(ForestBeanWrapper.TIGERS_IN_VICINITY, 7);
		assertTrue(panel.isEnabled());
	}

	public class Forest {
		private boolean perilLevel;
		private Integer tigersInVicinity;
		private Integer lionsInVicinity;
		private Double fightProbability;

		public boolean isPerilLevel() {
			return perilLevel;
		}

		public void setPerilLevel(boolean perilLevel) {
			this.perilLevel = perilLevel;
		}

		public Integer getTigersInVicinity() {
			return tigersInVicinity;
		}

		public void setTigersInVicinity(Integer tigersInVicinity) {
			this.tigersInVicinity = tigersInVicinity;
		}

		public Integer getLionsInVicinity() {
			return lionsInVicinity;
		}

		public void setLionsInVicinity(Integer lionsInVicinity) {
			this.lionsInVicinity = lionsInVicinity;
		}

		public Double getFightProbability() {
			return fightProbability;
		}

		public void setFightProbability(Double fightProbability) {
			this.fightProbability = fightProbability;
		}
	}

	public interface ForestBeanWrapper extends BeanWrapper<Forest> {
		public static final String PERIL_LEVEL = "perilLevel";
		public static final String TIGERS_IN_VICINITY = "tigersInVicinity";
		public static final String LIONS_IN_VICINITY = "lionsInVicinity";
		public static final String FIGHT_PROBABILITY = "fightProbability";
	}

	public static class ForestBeanWrapperImpl extends BeanWrapperImpl<Forest> implements ForestBeanWrapper {}

	public static class BeanWrapperImpl<T> implements BeanWrapper<T> {
		private T contentBean;
		private final PropertyChangeSupport pcs = new PropertyChangeSupport(this); 

		@Override
		public Object getBeanAttribute(String attr) {
			try {
				Method m = contentBean.getClass().getMethod(
						"get" + RebindUtils.getterSetterDeterminator(attr), (Class<?>[]) null);
				return m.invoke(contentBean, (Object[]) null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public T getBeanWrapperContent() {
			return contentBean;
		}

		@Override
		public void setBeanAttribute(String attr, Object value) {
			try {
				Object oldValue = getBeanAttribute(attr);
				
				Class<?>[] attrs = new Class[] { value.getClass() };
				Method m = contentBean.getClass().getMethod(
						"set" + RebindUtils.getterSetterDeterminator(attr), attrs);
				Object[] values = new Object[] { value };
				m.invoke(contentBean, values);
				
				pcs.firePropertyChange(attr, oldValue, value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void setBeanWrapperContent(T content) {
			this.contentBean = content;
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			pcs.addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			pcs.removePropertyChangeListener(listener);
		}

	}

	public class MockTextBox implements HasEnabled, HasValue<String> {

		@Override
		public boolean isEnabled() {
			return false;
		}

		@Override
		public void setEnabled(boolean enabled) {}

		@Override
		public String getValue() {
			return null;
		}

		@Override
		public void setValue(String value) {}

		@Override
		public void setValue(String value, boolean fireEvents) {}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
			return null;
		}

		@Override
		public void fireEvent(GwtEvent<?> event) {}

	}

	public class MockForestConfigurationPanel extends BindingHolder<Forest> implements IBindingHolder<Forest>, ConfigurableBinding<Forest>,
			HasEnabled {
		private BeanWrapper<Forest> beanWrapper;
		
		public MockForestConfigurationPanel(UpdateStrategy updateStrategy, BeanWrapper<Forest> beanWrapper) {
			super(updateStrategy, beanWrapper);
			this.beanWrapper = beanWrapper;
		}

		private boolean enabled = true;

		protected MockTextBox perilLevelBox = new MockTextBox();
		protected MockTextBox tigersInVicinityBox = new MockTextBox();

		@Override
		public IBindingHolder<Forest> getHolder() {
			return this;
		}

		@Override
		public boolean isEnabled() {
			return enabled;
		}

		@Override
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public BeanWrapper<Forest> getBeanWrapper() {
			return beanWrapper;
		}



	}
}
