package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;

public interface ConvertedInstanceCache {
	
	<S> S getInstance(Object source);
	<S> S putInstance(Object source, S result);

	<S> S getInstance(Class<S> instanceClass, Serializable id);
	<S> S putInstance(S instance, Serializable id);
}