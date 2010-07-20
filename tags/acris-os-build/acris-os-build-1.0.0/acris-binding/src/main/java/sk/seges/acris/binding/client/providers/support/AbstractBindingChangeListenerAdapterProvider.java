package sk.seges.acris.binding.client.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.providers.support.generic.IBindingBeanAdapterProvider;
import sk.seges.acris.binding.client.providers.support.handlers.ChangeHandlerAdapter;

import com.google.gwt.user.client.ui.SourcesChangeEvents;

@Deprecated
public abstract class AbstractBindingChangeListenerAdapterProvider<M extends SourcesChangeEvents, T> implements IBindingBeanAdapterProvider<M> {
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
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