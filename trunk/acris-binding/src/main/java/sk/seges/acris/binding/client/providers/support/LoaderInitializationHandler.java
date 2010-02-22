/**
 * 
 */
package sk.seges.acris.binding.client.providers.support;

import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.holder.BindingHolder.BindingHolderListener;
import sk.seges.sesam.dao.IAsyncDataLoader;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Handles the moment when the binding becomes active and more complex binding
 * (one-to-many) must gather all required data.
 * 
 * @author eldzi
 */
public class LoaderInitializationHandler<T> implements BindingHolderListener {
	private final IAsyncDataLoader<T> loader;
	private final ICallback<PagedResult<T>> callback;
	private final Widget widget;

	public LoaderInitializationHandler(IAsyncDataLoader<T> loader, ICallback<PagedResult<T>> callback,
			Widget widget) {
		super();
		this.loader = loader;
		this.callback = callback;
		this.widget = widget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gwt.beansbinding.core.client.BindingListener#bindingBecameBound(org
	 * .gwt.beansbinding.core.client.Binding)
	 */
	@Override
	public void bindingBecameBound(BindingHolder binding) {
		if (widget instanceof FocusWidget) {
			((FocusWidget) widget).setEnabled(false);
		}
		loader.load(Page.ALL_RESULTS_PAGE, callback);
	}
}
