package sk.seges.acris.json.client.instantiators;

import java.util.SortedMap;
import java.util.TreeMap;

import sk.seges.acris.json.client.InstanceCreator;

public class SortedMapInstanceCreator implements InstanceCreator<SortedMap<?, ?>> {

	@Override
	public SortedMap<?, ?> createInstance(Class<SortedMap<?, ?>> type) {
		return new TreeMap<Object, Object>();
	}

}
