package sk.seges.acris.binding.client.holder;

import java.io.Serializable;

import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * Extension specially for list binding. Interface is used to reload values in
 * list-boxes (or any other x-to-many binding UI widget). By default all
 * x-to-many bindings proxy values are cached in binding holder and are not
 * reloaded until page is manually refreshed (this is done for performance
 * purposes), but sometimes there is need for reloading values (for example if
 * data in database were changed).
 * 
 * @author fat
 * 
 * @param <T>
 *            Bean used for binding with UI widgets (in most cases
 *            {@link IDomainObject} but we are supporting all
 *            {@link Serializable} objects for binding
 */
public interface IListBeanBindingHolder<T extends Serializable> extends
		IBeanBindingHolder<T> {

	/**
	 * Reload data for widget bound with bean property specified as a argument
	 * for the method.
	 * 
	 * @param property
	 *            used in binding with widget which have to be reloaded
	 */
	public void reloadDataForBinding(String property);

	/**
	 * Reload data for widget used in binding for holding binding proxy objects
	 * 
	 * @param widget
	 *            UI binding widget used to x-to-many binding
	 */
	public void reloadDataForWidget(Widget widget);
}
