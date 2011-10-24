package sk.seges.acris.json.client.instantiators;

import java.util.AbstractSequentialList;
import java.util.LinkedList;

import sk.seges.acris.json.client.InstanceCreator;

public class SequentialListInstanceCreator implements InstanceCreator<AbstractSequentialList<?>> {

	@Override
	public AbstractSequentialList<?> createInstance(Class<AbstractSequentialList<?>> type) {
		return new LinkedList<Object>();
	}

}
