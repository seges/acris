package sk.seges.acris.bind;

import java.io.Serializable;

import org.gwt.beansbinding.core.client.BindingGroup;

import com.google.gwt.user.client.ui.Widget;

import sk.seges.acris.holder.IBeanBindingHolder;
import sk.seges.sesam.domain.IDomainObject;

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

	public void addBinding(String sourceProperty, Widget targetWidget,
			String targetProperty);

	public void bind();

	public void rebind();
}