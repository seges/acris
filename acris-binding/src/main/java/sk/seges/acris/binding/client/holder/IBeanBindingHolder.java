package sk.seges.acris.binding.client.holder;

import java.io.Serializable;

import sk.seges.acris.binding.client.bind.annotations.BindingField;
import sk.seges.sesam.domain.IDomainObject;

/**
 * Container interface for holding bindings with GWT UI widgets. You have to
 * implement this interface in order to realize binding using
 * {@link BindingField} annotations.
 * 
 * @author fat
 * 
 * @param <T>
 *            Bean used for binding with UI widgets (in most cases
 *            {@link IDomainObject} but we are supporting all
 *            {@link Serializable} objects for binding
 */
public interface IBeanBindingHolder<T extends Serializable> {
	/**
	 * Bind values from bean to widgets or reload existing binding if there is
	 * any.
	 * 
	 * @param bean
	 *            values holder
	 */
	public void setBean(T bean);

	/**
	 * @return bean with bound values. Reflects values specified in UI widgets.
	 */
	public T getBean();
}
