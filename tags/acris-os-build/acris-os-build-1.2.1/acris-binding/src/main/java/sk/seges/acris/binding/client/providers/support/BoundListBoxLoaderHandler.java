/**
 * 
 */
package sk.seges.acris.binding.client.providers.support;

import java.util.List;

import org.gwt.beansbinding.observablecollections.client.ObservableList;

import sk.seges.sesam.dao.IAsyncDataLoader;
import sk.seges.sesam.dao.Page;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class BoundListBoxLoaderHandler<T> implements ClickHandler {
	private IAsyncDataLoader<List<T>> loader;
	private Page page;
	private ObservableList<T> list;

	public BoundListBoxLoaderHandler(IAsyncDataLoader<List<T>> loader, Page page, ObservableList<T> list) {
		super();
		this.loader = loader;
		this.page = page;
		this.list = list;
	}

	@Override
	public void onClick(ClickEvent arg0) {
//		loader.load(page, new BoundListAsyncCallback<T>(list));
	}
}