package sk.seges.acris.pivo;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;


public abstract class AbstractObjectFactory {
	protected IDependency[] dependencies;
	protected ChocolateFactory factory;
	private Object cache;
	
	public AbstractObjectFactory(ChocolateFactory factory) {
		this.factory = factory;
		initDependencies();
	}
	
	private Object[] resolve() {
		if(dependencies == null)
			return null;
		
		Object[] values = new Object[dependencies.length];
		
		for(int i = 0; i < dependencies.length; i++) {
			IDependency dependency = dependencies[i];
			values[i] = dependency.resolve(factory);
		}
		
		return values;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T create() {
		if(cache == null || Scope.PROTOTYPE.equals(getScope())) {
			cache = construct(resolve()); 
		}
		return (T) cache;
	}
	
	public <T extends Object> void create(final TrackingAsyncCallback<T> callback) {
		if(cache == null || Scope.PROTOTYPE.equals(getScope())) {
			// caching callback is created for each create request to avoid
			// race-condition (which is highly unlikely but...
			TrackingAsyncCallback<T> cachingCallback = new TrackingAsyncCallback<T>() {
				@Override
				public void onFailureCallback(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccessCallback(T result) {
					cache = result;
					callback.onSuccess(result);
				}
			};
			construct(cachingCallback, resolve()); 
		}
	}
		
	/**
	 * Overriden by specific implementation when a chocolate needs dependencies
	 * defined in the factory. Currently these dependencies must be registered
	 * before this chocolate gets constructed.
	 */
	protected void initDependencies() {};
	
	/**
	 * Constructs a chocolate.
	 * 
	 * @param values
	 *            Dependencies in order in which they were defined.
	 * @return
	 */
	protected abstract Object construct(final Object ... values);

	@SuppressWarnings("unchecked")
	protected <T extends Object> void construct(final TrackingAsyncCallback<T> callback, final Object ... values) {
		callback.onSuccess((T) construct(values));
	};
	
	/**
	 * Determines the scope of chocolate.
	 * 
	 * @return
	 * @see Scope
	 */
	protected Scope getScope() { return Scope.PROTOTYPE; }
	
	/**
	 * In case of getting chocolate asynchronously (via callback) whether
	 * GWT.runAsync should be used.
	 */
	protected boolean applyRunAsync() { return false; }
	
}
