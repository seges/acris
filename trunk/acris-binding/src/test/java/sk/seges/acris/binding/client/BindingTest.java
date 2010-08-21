/**
 * 
 */
package sk.seges.acris.binding.client;

import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Converter;
import org.gwt.beansbinding.core.client.Validator;
import org.junit.Ignore;
import org.junit.Test;

import sk.seges.acris.binding.client.holder.ConfigurableBinding;
import sk.seges.acris.binding.client.holder.IBindingHolder;
import sk.seges.acris.binding.client.wrappers.BeanProxyWrapper;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.core.rebind.RebindUtils;
import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class BindingTest {
	@Test
	@Ignore
	public void testBindingChain() throws Exception {
		Forest forest = new Forest();
		ForestBeanWrapper wrapper = new ForestBeanWrapperImpl();
		wrapper.setBeanWrapperContent(forest);

		MockForestConfigurationPanel panel = new MockForestConfigurationPanel();
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
				Class<?>[] attrs = new Class[] { value.getClass() };
				Method m = contentBean.getClass().getMethod(
						"set" + RebindUtils.getterSetterDeterminator(attr), attrs);
				Object[] values = new Object[] { value };
				m.invoke(contentBean, values);
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
		// TODO Auto-generated method stub

		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

		}

	}

	public interface HasEnabled {
		boolean isEnabled();

		void setEnabled(boolean enabled);
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

	public class MockForestConfigurationPanel implements IBindingHolder<Forest>, ConfigurableBinding<Forest>,
			HasEnabled {
		private boolean enabled = true;

		protected MockTextBox perilLevelBox = new MockTextBox();
		protected MockTextBox tigersInVicinityBox = new MockTextBox();

		@Override
		public org.gwt.beansbinding.core.client.Binding addBinding(String sourceProperty,
				Object targetWidget, String targetProperty) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BindingGroup addBindingGroup(String sourceProperty, Widget targetWidget,
				String targetProperty,
				BeanProxyWrapper<? extends IDomainObject<?>, ? extends IDomainObject<?>> sourceObject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BindingGroup addBindingGroup(String sourceProperty, Widget targetWidget,
				String targetProperty, BeanWrapper<? extends IDomainObject<?>> sourceObject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void bind() {
		// TODO Auto-generated method stub

		}

		@Override
		public Forest getBean() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void rebind() {
		// TODO Auto-generated method stub

		}

		@Override
		public void setBean(Forest bean) {
		// TODO Auto-generated method stub

		}

		@Override
		public IBindingHolder<Forest> getHolder() {
			// TODO Auto-generated method stub
			return null;
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
		public org.gwt.beansbinding.core.client.Binding addBinding(String sourceProperty,
				Object targetWidget, String targetProperty, Converter<?, ?> converter, Validator<?> validator) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
