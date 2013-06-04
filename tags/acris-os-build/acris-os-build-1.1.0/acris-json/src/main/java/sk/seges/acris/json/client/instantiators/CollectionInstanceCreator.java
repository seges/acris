package sk.seges.acris.json.client.instantiators;

import java.util.Collection;
import java.util.HashSet;

import sk.seges.acris.json.client.InstanceCreator;

public class CollectionInstanceCreator implements InstanceCreator<Collection<?>> {

	@Override
	public Collection<?> createInstance(Class<Collection<?>> type) {
		return new HashSet<Object>();
	}
}
