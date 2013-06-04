package sk.seges.acris.binding.client.providers.wrapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;

import org.gwt.beansbinding.core.client.PropertyResolutionException;
import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;

public class BeanWrapperAdapterProvider implements BeanAdapterProvider {

	public static final class BeanWrapperAdapter extends BeanAdapterBase implements PropertyChangeListener {
		private Object cachedValue;
		private BeanWrapper<?> beanWrapper;

		public BeanWrapperAdapter(BeanWrapper<?> beanWrapper, String property) {
			super(property);
			this.beanWrapper = beanWrapper;
		}

		public Object getValue() {
			return beanWrapper.getBeanAttribute(property);
		}

		public void setValue(Object value) {
			beanWrapper.setBeanAttribute(property, value);
		}

		@Override
		protected void listeningStarted() {
			cachedValue = getValue();
			beanWrapper.addPropertyChangeListener(this);
		}

		@Override
		protected void listeningStopped() {
			beanWrapper.removePropertyChangeListener(this);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Object oldValue = cachedValue;
			cachedValue = getValue();
			firePropertyChange(oldValue, cachedValue);
		}
	}

	public BeanAdapter createAdapter(Object source, String property) {
		if (!providesAdapter(source.getClass(), property)) {
			throw new IllegalArgumentException();
		}

		if (source instanceof BeanWrapper<?>) {
			return new BeanWrapperAdapter((BeanWrapper<?>) source, property);
		}
		throw new IllegalArgumentException("Source does not support change events");
	}

	private static String globalProperty = "value";

	public boolean providesAdapter(Class<?> type, String property) {
		boolean supports = isSupportedClass(type);

		if (supports) {
			PropertyDescriptor[] pds = getBeanInfo(new BeanWrapperAdapter(null, "temp"))
					.getPropertyDescriptors();

			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals(globalProperty)) {
					pd.setName(property);
					globalProperty = property;
				}
			}
		}

		return supports;
	}

	private BeanInfo getBeanInfo(Object object) {
		assert object != null;

		try {
			return Introspector.getBeanInfo(object.getClass());
		} catch (IntrospectionException ie) {
			throw new PropertyResolutionException("Exception while introspecting "
					+ object.getClass().getName(), ie);
		}
	}

	public Class<?> getAdapterClass(Class<?> type) {
		return isSupportedClass(type) ? BeanWrapperAdapter.class : null;
	}

	private static final String WRAPPER_SUFFIX = "BeanWrapperImpl";

	protected boolean isSupportedClass(Class<?> type) {
		if (type == null) {
			return false;
		}

		if (type.getName().endsWith(WRAPPER_SUFFIX)) {
			return true;
		}

		return isSupportedClass(type.getSuperclass());
	}
}