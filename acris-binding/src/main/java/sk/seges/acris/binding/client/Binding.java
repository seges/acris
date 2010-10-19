/**
 * 
 */
package sk.seges.acris.binding.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.Converter;
import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

import sk.seges.acris.binding.client.holder.ConfigurableBinding;
import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.acris.binding.client.providers.support.widget.HasEnabled;
import sk.seges.acris.binding.client.providers.support.widget.HasVisible;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.UIObject;

/**
 * @author ladislav.gazo
 */
public class Binding {

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> BindingActions<T> alter(IBeanBindingHolder<T> binding) {
		if (!(binding instanceof ConfigurableBinding<?>)) {
			throw new RuntimeException("Provide configurable binding please");
		}
		return new BindingActions<T>((ConfigurableBinding<T>) binding);
	}

	public static class BindingConfiguration {
		private final BindingActions<?> actions;

		private Object sourceObject;
		private String targetField;
		private Condition<Boolean> condition;
		private Operation operation;

		public BindingConfiguration(BindingActions<?> actions) {
			super();
			this.actions = actions;
		}

		public void setTargetField(String targetField) {
			this.targetField = targetField;
		}

		public void setCondition(Condition<Boolean> condition) {
			this.condition = condition;
		}

		public void setOperation(Operation operation) {
			this.operation = operation;
		}

		public Object getSourceObject() {
			return sourceObject;
		}

		public void setSourceObject(Object sourceObject) {
			this.sourceObject = sourceObject;
		}

		public String getTargetField() {
			return targetField;
		}

		public Condition<Boolean> getCondition() {
			return condition;
		}

		public void instantiate() {
			operation.instantiateBinding();
		}
	}

	public static class AbstractConfigurable {
		protected BindingConfiguration configuration;

		public AbstractConfigurable(BindingConfiguration configuration) {
			super();
			this.configuration = configuration;
		}
	}

	public static class BindingActions<T extends Serializable> {
		private final ConfigurableBinding<T> binding;
		private final BindingConfiguration configuration;

		public BindingActions(ConfigurableBinding<T> binding) {
			this.binding = binding;
			configuration = new BindingConfiguration(this);
		}

		public ConfigurableBinding<T> getBinding() {
			return binding;
		}

		public Enable enable(Object widget) {
			if (widget instanceof FocusWidget || widget instanceof HasEnabled) {
				Enable enable = new Enable(configuration, widget);
				configuration.setOperation(enable);
				return enable;
			}
			throw new RuntimeException("The widget must have methods to enable and disable it");
		}

		public Visible visible(Object widget) {
			if (widget instanceof UIObject || widget instanceof HasVisible) {
				Visible visible = new Visible(configuration, widget);
				configuration.setOperation(visible);
				return visible;
			}
			throw new RuntimeException("The widget must have methods to set visibility");
		}
	}

	public static abstract class Operation extends AbstractConfigurable {
		public Operation(BindingConfiguration configuration) {
			super(configuration);
		}

		public abstract void instantiateBinding();
	}

	public static abstract class WidgetOperation extends Operation implements HasPropertyChangeSupport {
		protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

		public WidgetOperation(BindingConfiguration configuration, Object widget) {
			super(configuration);
			configuration.setSourceObject(widget);
		}

		protected abstract void setupBinding(BindingGroup group, Converter<Object, Boolean> converter);

		public Target when(String field) {
			return new Target(configuration, field);
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			pcs.addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			pcs.removePropertyChangeListener(listener);
		}

		@Override
		public void instantiateBinding() {
			BindingGroup group = new BindingGroup();

			Converter<Object, Boolean> converter = new Converter<Object, Boolean>() {
				@Override
				public Object convertReverse(Boolean value) {
					return null;
				}

				@Override
				public Boolean convertForward(Object value) {
					return configuration.getCondition().evaluate(value);
				}
			};

			setupBinding(group, converter);

			configuration.actions.getBinding().getHolder().manageBindingGroup(group);
		}
	}

	public static class Enable extends WidgetOperation {
		public static final String ENABLED = "enabled";

		private boolean enabled;

		public Enable(BindingConfiguration configuration, Object widget) {
			super(configuration, widget);
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			boolean oldValue = this.enabled;
			this.enabled = enabled;
			pcs.firePropertyChange(ENABLED, oldValue, enabled);
		}

		@Override
		protected void setupBinding(BindingGroup group, Converter<Object, Boolean> converter) {
			if (configuration.getSourceObject() instanceof FocusWidget) {
				BeanProperty<Object, Object> beanValueSourceProperty = BeanProperty
						.<Object, Object> create(configuration.getTargetField());

				BeanProperty<FocusWidget, Boolean> enabledSourceProperty = BeanProperty
						.<FocusWidget, Boolean> create(ENABLED);

				BeanWrapper<?> sourceObject = configuration.actions.getBinding().getBeanWrapper();
				AutoBinding<Object, Object, FocusWidget, Boolean> binding = Bindings.createAutoBinding(
						UpdateStrategy.READ, sourceObject, beanValueSourceProperty,
						(FocusWidget) configuration.getSourceObject(), enabledSourceProperty);

				binding.setConverter(converter);
				group.addBinding(binding);
			} else if (configuration.getSourceObject() instanceof HasEnabled) {
				BeanProperty<Object, Object> beanValueSourceProperty = BeanProperty
						.<Object, Object> create(configuration.getTargetField());

				BeanProperty<HasEnabled, Boolean> enabledSourceProperty = BeanProperty
						.<HasEnabled, Boolean> create(ENABLED);

				BeanWrapper<?> sourceObject = configuration.actions.getBinding().getBeanWrapper();
				AutoBinding<Object, Object, HasEnabled, Boolean> binding = Bindings.createAutoBinding(
						UpdateStrategy.READ, sourceObject, beanValueSourceProperty,
						(HasEnabled) configuration.getSourceObject(), enabledSourceProperty);

				binding.setConverter(converter);
				group.addBinding(binding);
			}
		}

	}

	public static class Visible extends WidgetOperation {
		public static final String VISIBLE = "visible";

		private boolean visible;

		public Visible(BindingConfiguration configuration, Object widget) {
			super(configuration, widget);
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			boolean oldValue = this.visible;
			this.visible = visible;
			pcs.firePropertyChange(VISIBLE, oldValue, visible);
		}

		@Override
		protected void setupBinding(BindingGroup group, Converter<Object, Boolean> converter) {
			if (configuration.getSourceObject() instanceof UIObject) {
				BeanProperty<Object, Object> beanValueSourceProperty = BeanProperty
						.<Object, Object> create(configuration.getTargetField());

				BeanProperty<UIObject, Boolean> enabledSourceProperty = BeanProperty
						.<UIObject, Boolean> create(VISIBLE);

				BeanWrapper<?> sourceObject = configuration.actions.getBinding().getBeanWrapper();
				AutoBinding<Object, Object, UIObject, Boolean> binding = Bindings.createAutoBinding(
						UpdateStrategy.READ, sourceObject, beanValueSourceProperty,
						(UIObject) configuration.getSourceObject(), enabledSourceProperty);

				binding.setConverter(converter);
				group.addBinding(binding);
			} else if (configuration.getSourceObject() instanceof HasVisible) {
				BeanProperty<Object, Object> beanValueSourceProperty = BeanProperty
						.<Object, Object> create(configuration.getTargetField());

				BeanProperty<HasVisible, Boolean> enabledSourceProperty = BeanProperty
						.<HasVisible, Boolean> create(VISIBLE);

				BeanWrapper<?> sourceObject = configuration.actions.getBinding().getBeanWrapper();
				AutoBinding<Object, Object, HasVisible, Boolean> binding = Bindings.createAutoBinding(
						UpdateStrategy.READ, sourceObject, beanValueSourceProperty,
						(HasVisible) configuration.getSourceObject(), enabledSourceProperty);

				binding.setConverter(converter);
				group.addBinding(binding);
			}
		}
	}

	public static class Target extends AbstractConfigurable {

		public Target(BindingConfiguration configuration, String field) {
			super(configuration);
			configuration.setTargetField(field);
		}

		public void isNotNull() {
			configuration.setCondition(new NotNull());
			configuration.instantiate();
		}

		public void isNotEmpty() {
			configuration.setCondition(new NotEmpty());
			configuration.instantiate();
		}
		
		public void matches(Condition<Boolean> condition) {
			configuration.setCondition(condition);
			configuration.instantiate();
		}
		
		public void equalTo(Object equalValue) {
			configuration.setCondition(new Equals(equalValue));
			configuration.instantiate();
		}
	}

	public static interface Condition<R> {
		R evaluate(Object value);
	}

	public static class NotNull implements Condition<Boolean> {
		@Override
		public Boolean evaluate(Object value) {
			return (value != null);
		}
	}

	public static class NotEmpty implements Condition<Boolean> {
		@Override
		public Boolean evaluate(Object value) {
			return (value != null && value.toString().length() > 0);
		}
	}
	
	public static class Equals implements Condition<Boolean> {
		private Object equalValue;
		
		public Equals(Object equalValue) {
			super();
			this.equalValue = equalValue;
		}

		@Override
		public Boolean evaluate(Object value) {
			if(value == null && equalValue == null) {
				return true;
			}
			return (value != null && value.equals(equalValue));
		}
	}
}
