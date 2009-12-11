package sk.seges.acris.binding.bind.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import sk.seges.acris.binding.bind.providers.support.AbstractBindingChangeHandlerAdapterProvider.ChangeHandlerAdapter;
import sk.seges.acris.binding.bind.providers.support.generic.IBindingBeanAdapterProvider;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

@Deprecated
public abstract class AbstractBindingChangeListenerAdapterProvider<M extends SourcesChangeEvents, T> implements IBindingBeanAdapterProvider<M> {
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	public static final class ChangeListenerAdapter<M extends SourcesChangeEvents, T> extends BeanAdapterBase {
		
		private M widget;		
		private Handler handler;
		private T previousValue;
		private AbstractBindingChangeListenerAdapterProvider<M, T> provider;
		
		public ChangeListenerAdapter(M changeHandlerWidget, String property, AbstractBindingChangeListenerAdapterProvider<M, T> provider) {
			super(property);
			this.widget = changeHandlerWidget;
			this.provider = provider;
		}

		public T getValue() {
			return provider.getValue(widget);
		}

		public void setValue(T t) {
			provider.setValue(widget, t);
		}

		@Override
		protected void listeningStarted() {
			handler = new Handler();
			previousValue = getValue();
			widget.addChangeListener(handler);
		}

		@Override
		protected void listeningStopped() {
			if (handler != null) {
				widget.removeChangeListener(handler);
				handler = null;
			}
			previousValue = null;
		}

		private class Handler implements ChangeListener {
			@Override
			public void onChange(Widget sender) {
				Object oldElementOrElements = previousValue;
				previousValue = getValue();
				firePropertyChange(oldElementOrElements, previousValue);
			}
		}
	}

	public BeanAdapter createAdapter(Object source, String property) {
		if (!providesAdapter(source.getClass(), property)) {
			throw new IllegalArgumentException();
		}
		if (source instanceof SourcesChangeEvents) {
			return new ChangeListenerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support change events");
	}

	public boolean providesAdapter(Class<?> type, String property) {
		return isSupportedClass(type) && property.intern() == getBindingWidgetProperty();
	}

	public Class<?> getAdapterClass(Class<?> type) {
		return isSupportedClass(type) ? ChangeHandlerAdapter.class : null;
	}

	protected boolean isSupportedClass(Class<?> type) {
		if (type == getBindingWidgetClasses()) {
			return true;
		}
		
		if (isSupportSuperclass()) {
			type = type.getSuperclass();
			if (type != null && type != Object.class) {
				return isSupportedClass(type);
			}
		}
		return false;
	}
	
	public String getBindingWidgetProperty() {
		return "value";
	}

	@Override
	public boolean isSupportSuperclass() {
		return true;
	}
}