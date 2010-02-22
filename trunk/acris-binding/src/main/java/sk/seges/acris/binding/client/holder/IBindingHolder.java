package sk.seges.acris.binding.client.holder;

import java.io.Serializable;

import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;

import sk.seges.acris.binding.client.wrappers.BeanProxyWrapper;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.user.client.ui.Widget;

public interface IBindingHolder<T extends Serializable> extends IBeanBindingHolder<T> {

	public T getBean();

	public void setBean(T bean);

	public BindingGroup addBindingGroup(
			String sourceProperty,
			Widget targetWidget,
			String targetProperty,
			BeanProxyWrapper<? extends IDomainObject<?>, ? extends IDomainObject<?>> sourceObject);

	public BindingGroup addBindingGroup(String sourceProperty,
			Widget targetWidget, String targetProperty,
			BeanWrapper<? extends IDomainObject<?>> sourceObject);

	@SuppressWarnings("unchecked")
	public Binding addBinding(String sourceProperty, Object targetWidget,
			String targetProperty);

	public void bind();

	public void rebind();
}