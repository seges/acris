/**
 * 
 */
package sk.seges.acris.binding.client.loader;

import java.util.Arrays;
import java.util.List;

import sk.seges.sesam.dao.IAsyncDataLoader;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * @author eldzi
 */
public class StaticListLoader<T> implements IAsyncDataLoader<List<T>> {
	private final List<T> list;
	
	public StaticListLoader(List<T> list) {
		super();
		this.list = list;
	}

	public StaticListLoader(T[] list) {
		super();
		this.list = Arrays.asList(list);
	}
	
	@Override
	public void load(Page page, ICallback<PagedResult<List<T>>> callback) {
		if(list == null) {
			callback.onFailure(new RuntimeException("List in static list loader cannot be null."));
			return;
		}
		PagedResult<List<T>> result = new PagedResult<List<T>>(page, list, list.size());
		callback.onSuccess(result);
	}
}
