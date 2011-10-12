package sk.seges.acris.json.client.instantiators;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.json.client.InstanceCreator;

public class ListInstanceCreator implements InstanceCreator<List<?>> {

	@Override
	public List<?> createInstance(Class<List<?>> type) {
		return new ArrayList<Object>();
	}

}
