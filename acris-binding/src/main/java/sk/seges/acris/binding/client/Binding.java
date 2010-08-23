/**
 * 
 */
package sk.seges.acris.binding.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

import sk.seges.acris.binding.client.holder.ConfigurableBinding;
import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.acris.binding.client.providers.support.widget.HasEnabled;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;

import com.google.gwt.user.client.ui.FocusWidget;

/**
 * @author ladislav.gazo
 */
public class Binding {

	@SuppressWarnings("unchecked")
	public static <T> BindingActions<T> alter(IBeanBindingHolder<T> binding) {
		if (!(binding instanceof ConfigurableBinding<?>)) {
			throw new RuntimeException("Provide configurable binding please");
		}
		return new BindingActions<T>((ConfigurableBinding<T>) binding);
	}

	public static class BindingConfiguration {
		private final BindingActions<?> actions;

		private Object sourceObject;
		private String targetField;
		private Condition condition;
		private Operation operation;

		public BindingConfiguration(BindingActions<?> actions) {
			super();
			this.actions = actions;
		}

		public void setTargetField(String targetField) {
			this.targetField = targetField;
		}

		public void setCondition(Condition condition) {
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

		public Condition getCondition() {
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

	public static class BindingActions<T> {
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
	}

	public static abstract class Operation extends AbstractConfigurable {
		public Operation(BindingConfiguration configuration) {
			super(configuration);
		}

		public abstract void instantiateBinding();
	}

	public static class Enable extends Operation implements HasPropertyChangeSupport {
		public static final String ENABLED = "enabled";

		private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

		private boolean enabled;

		public Enable(BindingConfiguration configuration, Object widget) {
			super(configuration);
			configuration.setSourceObject(widget);
		}

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

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			boolean oldValue = this.enabled;
			this.enabled = enabled;
			pcs.firePropertyChange(ENABLED, oldValue, enabled);
		}

		@Override
		public void instantiateBinding() {
			BindingGroup group = new BindingGroup();

			if (configuration.getSourceObject() instanceof FocusWidget) {
				BeanProperty<FocusWidget, Boolean> enabledSourceProperty = BeanProperty
						.<FocusWidget, Boolean> create("enabled");
				BeanProperty<Enable, Boolean> enabledTargetProperty = BeanProperty
						.<Enable, Boolean> create(ENABLED);

				AutoBinding<FocusWidget, Boolean, Enable, Boolean> enabledBinding = Bindings
						.createAutoBinding(UpdateStrategy.READ_WRITE, (FocusWidget) configuration
								.getSourceObject(), enabledSourceProperty, this, enabledTargetProperty);
				group.addBinding(enabledBinding);
			} else if (configuration.getSourceObject() instanceof HasEnabled) {
				BeanProperty<HasEnabled, Boolean> enabledSourceProperty = BeanProperty
						.<HasEnabled, Boolean> create("enabled");
				BeanProperty<Enable, Boolean> enabledTargetProperty = BeanProperty
						.<Enable, Boolean> create(ENABLED);

				AutoBinding<HasEnabled, Boolean, Enable, Boolean> enabledBinding = Bindings
						.createAutoBinding(UpdateStrategy.READ_WRITE, (HasEnabled) configuration
								.getSourceObject(), enabledSourceProperty, this, enabledTargetProperty);
				group.addBinding(enabledBinding);
			} else {
				throw new RuntimeException("Source object must be FocusWidget or HasEnabled");
			}

			BeanProperty<Object, Object> beanValueSourceProperty = BeanProperty
					.<Object, Object> create(configuration.getTargetField());
			BeanProperty<Condition, Object> conditionValueTargetProperty = BeanProperty
					.<Condition, Object> create(Condition.VALUE);

			BeanWrapper<?> sourceObject = configuration.actions.getBinding().getBeanWrapper();
			AutoBinding<Object, Object, Condition, Object> conditionBinding = Bindings.createAutoBinding(
					UpdateStrategy.READ_WRITE, sourceObject, beanValueSourceProperty, configuration
							.getCondition(), conditionValueTargetProperty);
			group.addBinding(conditionBinding);

			BeanProperty<Enable, Boolean> operationSourceProperty = BeanProperty
					.<Enable, Boolean> create(Enable.ENABLED);
			BeanProperty<Condition, Boolean> operationTargetProperty = BeanProperty
					.<Condition, Boolean> create(Condition.RESULT);
			AutoBinding<Enable, Boolean, Condition, Boolean> operationBinding = Bindings.createAutoBinding(
					UpdateStrategy.READ_WRITE, this, operationSourceProperty, configuration.getCondition(),
					operationTargetProperty);
			// AutoBinding<Condition, Boolean, Enable, Boolean> operationBinding
			// = Bindings.createAutoBinding(
			// UpdateStrategy.READ_WRITE, configuration.getCondition(),
			// operationTargetProperty, this,
			// operationSourceProperty);

			group.addBinding(operationBinding);

			group.bind();

			configuration.actions.getBinding().getHolder().manageBindingGroup(group);
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
	}

	public static abstract class Condition implements HasPropertyChangeSupport {
		public static final String VALUE = "value";
		public static final String RESULT = "result";
		private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

		private Boolean result;
		protected Object value;

		protected abstract void evaluate();

		public Condition() {
			addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(VALUE.equals(evt.getPropertyName())) {
						evaluate();
					}
				}
			});
		}
		
		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			pcs.addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			pcs.removePropertyChangeListener(listener);
		}

		public final Boolean isResult() {
			return result;
		}

		public final void setResult(Boolean result) {
			Boolean oldValue = this.result;
			this.result = result;
			pcs.firePropertyChange(RESULT, oldValue, result);
		}

		public final Object getValue() {
			return value;
		}

		public final void setValue(Object value) {
			Object oldValue = this.value;
			this.value = value;
			pcs.firePropertyChange(VALUE, oldValue, value);
		}
	}

	public static class NotNull extends Condition {
		@Override
		protected void evaluate() {
			setResult(value != null && value.toString().length() > 0);
		}
	}
}
