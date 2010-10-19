/**
 * 
 */
package sk.seges.acris.binding.client.providers.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.Converter;
import org.gwt.beansbinding.observablecollections.client.ObservableCollections;
import org.gwt.beansbinding.ui.client.ListBoxBinding;

import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Callback responsible for initializing a list of values bound to a list box.
 * List is fed using a loader.
 * 
 * @author eldzi
 * 
 * @param <T>
 *            Type of domain object contained in the list.
 */
public abstract class BoundListAsyncCallback<T extends Serializable> implements
		ICallback<PagedResult<List<T>>> {
	private final BindingHolder<T> bindingHolder;
	private final String sourceProperty;
	private final ListBox targetWidget;
	private final String itemTextProperty;
	private final String itemValueProperty;

	private boolean wasEnabled;

	private List<BeanWrapper<T>> listOfValues;
	
	private ListBoxBinding binding;
	private Binding selectedItemBinding;

	public BoundListAsyncCallback(BindingHolder<T> bindingHolder, String sourceProperty,
			ListBox targetWidget, String itemTextProperty, String itemValueProperty) {
		super();
		this.bindingHolder = bindingHolder;
		this.sourceProperty = sourceProperty;
		this.targetWidget = targetWidget;
		this.itemTextProperty = itemTextProperty;
		this.itemValueProperty = itemValueProperty;

		wasEnabled = targetWidget.isEnabled();
	}

	@Override
	public void onFailure(Throwable arg0) {
		GWT.log("Exception in bound list callback", arg0);
	}

	@Override
	public void onSuccess(PagedResult<List<T>> arg0) {
		if(selectedItemBinding != null) {
			bindingHolder.removeSelectedItemBinding(selectedItemBinding);
		}
		if (listOfValues != null && listOfValues.size() > 0) {
			listOfValues = null;
		}
		List<BeanWrapper<T>> listOfWrappers = null;
		if (listOfValues == null) {
			listOfWrappers = new ArrayList<BeanWrapper<T>>();
		}
		BeanWrapper<T> wrapper;
		for (T result : arg0.getResult()) {
			wrapper = createWrapper();
			wrapper.setBeanWrapperContent(result);
			if (listOfValues == null) {
				listOfWrappers.add(wrapper);
			} else {
				listOfValues.add(wrapper);
			}
		}
		if (listOfValues == null) {
			listOfValues = ObservableCollections.observableList(listOfWrappers);
		}

		binding = bindingHolder.addListBoxBinding(listOfValues, targetWidget, itemTextProperty, itemValueProperty);
		
		// selection binding with converter between wrapper and bean
		selectedItemBinding = bindingHolder.addSelectedItemBinding(sourceProperty, targetWidget,
				new Converter<T, BeanWrapper<T>>() {

					@Override
					public BeanWrapper<T> convertForward(T value) {
						BeanWrapper<T> wrapper = createWrapper();
						if(value instanceof BeanWrapper<?>) {
							wrapper.setBeanWrapperContent(((BeanWrapper<T>)value).getBeanWrapperContent());
						} else {
							wrapper.setBeanWrapperContent(value);
						}
						return wrapper;
					}

					@Override
					public T convertReverse(BeanWrapper<T> value) {
						return value.getBeanWrapperContent();
					}
				});
		selectedItemBinding.refreshAndNotify();
		
		if (wasEnabled) {
			targetWidget.setEnabled(true);
		}
	}

	protected abstract BeanWrapper<T> createWrapper();

	public void setWasEnabled(boolean wasEnabled) {
		this.wasEnabled = wasEnabled;
	}
}