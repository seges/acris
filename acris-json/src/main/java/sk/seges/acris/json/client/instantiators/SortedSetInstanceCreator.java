package sk.seges.acris.json.client.instantiators;

import java.util.SortedSet;
import java.util.TreeSet;

import sk.seges.acris.json.client.InstanceCreator;

public class SortedSetInstanceCreator implements InstanceCreator<SortedSet<?>> {

	@Override
	public SortedSet<?> createInstance(Class<SortedSet<?>> type) {
		return new TreeSet<Object>();
	}

}
