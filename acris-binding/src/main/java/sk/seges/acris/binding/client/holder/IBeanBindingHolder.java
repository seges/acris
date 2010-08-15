package sk.seges.acris.binding.client.holder;

import java.io.Serializable;

import sk.seges.acris.binding.client.annotations.BindingField;
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
public interface IBeanBindingHolder<T> extends IHasBean<T> {
}
