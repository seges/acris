package sk.seges.acris.json.client.instantiators;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.json.client.InstanceCreator;

public class MapInstanceCreator implements InstanceCreator<Map<?, ?>> {

	@Override
	public Map<?, ?> createInstance(Class<Map<?, ?>> type) {
		return new HashMap<Object, Object>();
	}
}
