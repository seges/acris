package sk.seges.acris.binding.client.providers.support.generic;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import sk.seges.acris.binding.client.wrappers.BeanProxyWrapper;

public abstract class AbstractBindingBeanAdapterProvider<T> implements IBindingBeanAdapterProvider<BeanProxyWrapper<?,?>> {

	public static final String WRAPPER_SUFFIX = "_BeanWrapperProxy"; 

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(BeanProxyWrapper<?,?> widget);
	protected abstract void setValue(BeanProxyWrapper<?,?> widget, T t);
	
	public static final class BeanProxyWrapperAdapter<T> extends BeanAdapterBase {
		
		private BeanProxyWrapper<?,?> beanProxyWrapper;
		private AbstractBindingBeanAdapterProvider<T> provider;
		
		public BeanProxyWrapperAdapter(BeanProxyWrapper<?,?> beanProxyWrapper, String property, AbstractBindingBeanAdapterProvider<T> provider) {
			super(property);
			this.beanProxyWrapper = beanProxyWrapper;
			this.provider = provider;
		}

		public T getValue() {
			return provider.getValue(beanProxyWrapper);
		}

		public void setValue(T t) {
			provider.setValue(beanProxyWrapper, t);
		}

		@Override
		protected void listeningStarted() {
		}

		@Override
		protected void listeningStopped() {
		}
	}

	@SuppressWarnings("unchecked")
	public BeanAdapter createAdapter(Object source, String property) {
		if (!providesAdapter(source.getClass(), property)) {
			throw new IllegalArgumentException();
		}
		
		if (source instanceof BeanProxyWrapper) {
			return new BeanProxyWrapperAdapter<T>((BeanProxyWrapper<?,?>)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support change events");
	}

	@Override
	public Class<BeanProxyWrapper<?, ?>> getBindingWidgetClasses() {
		//Not used
		return null;
	}

	public boolean providesAdapter(Class<?> type, String property) {
		return isSupportedClass(type) && property.intern() == getBindingWidgetProperty();
	}

	public Class<?> getAdapterClass(Class<?> type) {
		return isSupportedClass(type) ? BeanProxyWrapperAdapter.class : null;
	}

	public String getBindingWidgetProperty() {
		return PROPERTY_VALUE;
	}

	protected boolean isSupportedClass(Class<?> type) {
		return type.getName().endsWith(WRAPPER_SUFFIX);
	}
	
	@Override
	public boolean isSupportSuperclass() {
		return true;
	}
}