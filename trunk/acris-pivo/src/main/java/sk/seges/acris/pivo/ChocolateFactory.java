package sk.seges.acris.pivo;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

public class ChocolateFactory {
	private Map<String, AbstractObjectFactory> registry;
	
	public ChocolateFactory() {
		registry = new HashMap<String, AbstractObjectFactory>();
	}
	
	public boolean contains(String token) {
		return registry.containsKey(token);
	}
	
	public void registerChocolate(String token, AbstractObjectFactory iocFactory) {
		registry.put(token, iocFactory);
	}
	
	public void clear() {
		registry.clear();
	}
	
	@SuppressWarnings("unchecked")
	public final <T extends Object> T getChocolate(String token) {
		AbstractObjectFactory factory = registry.get(token);
		if(factory == null) {
			throw new PivoException("Cannot get chocolate factory for token " + token);
		} else if(factory.applyRunAsync()) {
			throw new PivoException("Cannot use chocolate factory for token " + token + " because it is asynchronous.");
		}
		return (T) factory.create();
	}
	
	public final <T extends Object> void getChocolate(String token, final TrackingAsyncCallback<T> callback) {
		final AbstractObjectFactory factory = registry.get(token);
		if(factory == null)
			throw new PivoException("Cannot get chocolate factory for token " + token);
		if(factory.applyRunAsync()) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onSuccess() {
					factory.create(callback);
				}
				
				@Override
				public void onFailure(Throwable reason) {
					callback.onFailure(reason);
				}
			});
		} else {
			factory.create(callback);
		}
	}
	
	public void registerAlias(String name, String ref) {
		registerChocolate(name, new Alias(this, ref));
	}
	
	public AbstractObjectFactory getFactory(String chocolate) {
		return registry.get(chocolate);
	}
}
