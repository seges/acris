/**
 * 
 */
package sk.seges.acris.binding.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.core.rebind.RebindUtils;

public class BeanWrapperImpl<T> implements BeanWrapper<T> {
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

			String setter = "set" + RebindUtils.getterSetterDeterminator(attr);

			Class<?>[] attrs = null;
			if (value != null) {
				attrs = new Class[] { value.getClass() };
			} else {
				for (Method method : contentBean.getClass().getMethods()) {
					if (method.getName().equals(setter)) {
						attrs = method.getParameterTypes();
						break;
					}
				}
			}

			Method m = contentBean.getClass().getMethod(setter, attrs);
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