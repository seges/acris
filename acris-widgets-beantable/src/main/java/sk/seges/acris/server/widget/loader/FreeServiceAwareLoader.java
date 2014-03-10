package sk.seges.acris.server.widget.loader;

import sk.seges.acris.callbacks.client.CallbackAdapter;
import sk.seges.sesam.dao.IAsyncDataLoader;
import sk.seges.sesam.shared.model.dao.ICallback;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

public abstract class FreeServiceAwareLoader<E, Service> implements IAsyncDataLoader<E> {
	
	public FreeServiceAwareLoader() {}

	public void load(Page page, ICallback<PagedResult<E>> callback) {
		Service service = getService();
		load(service, page, new CallbackAdapter<PagedResult<E>>(callback));
	}
	
	protected abstract void load(Service service, Page page, CallbackAdapter<PagedResult<E>> callback);
	
	protected abstract Service getService();

}
