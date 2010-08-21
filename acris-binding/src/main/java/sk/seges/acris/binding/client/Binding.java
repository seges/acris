/**
 * 
 */
package sk.seges.acris.binding.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

import com.google.gwt.user.client.ui.FocusWidget;

import sk.seges.acris.binding.client.BindingTest.HasEnabled;
import sk.seges.acris.binding.client.holder.ConfigurableBinding;
import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.acris.binding.client.holder.IBindingHolder;

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
		private String sourceField;
		private Object sourceObject;
		private String targetField;
		private Condition condition;
		private Operation operation;

		public void setSourceField(String sourceField) {
			this.sourceField = sourceField;
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

		public void instantiate() {

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
		private IBindingHolder<T> holder;
		private BindingConfiguration configuration;

		public BindingActions(ConfigurableBinding<T> binding) {
			holder = binding.getHolder();
			configuration = new BindingConfiguration();
		}

		public Enable enable(Object widget) {
			if (widget instanceof FocusWidget || widget instanceof HasEnabled) {
				return new Enable(configuration, widget);
			}
			throw new RuntimeException("The widget must have methods to enable and disable it");
		}
	}

	public static class Operation extends AbstractConfigurable {
		public Operation(BindingConfiguration configuration) {
			super(configuration);
		}
	}

	public static class Enable extends Operation implements HasPropertyChangeSupport {
		public static final String ENABLED = "enabled";
		
		private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
		
		private boolean enabled;
		
		public Enable(BindingConfiguration conf, String field) {
			super(conf);
			conf.setSourceField(field);
			conf.setOperation(this);
		}

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
		public static final String RESULT = "result";
		private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
		
		private boolean result;
		protected Object value;

		protected abstract void evaluate();
		
		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			pcs.addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			pcs.removePropertyChangeListener(listener);
		}

		public final boolean isResult() {
			return result;
		}

		public final void setResult(boolean result) {
			boolean oldValue = this.result;
			this.result = result;
			pcs.firePropertyChange(RESULT, oldValue, result);
		}

		public final Object getValue() {
			return value;
		}

		public final void setValue(Object value) {
			this.value = value;
			evaluate();
		}
	}

	public static class NotNull extends Condition {
		@Override
		protected void evaluate() {
			setResult(value != null);
		}
	}
}
