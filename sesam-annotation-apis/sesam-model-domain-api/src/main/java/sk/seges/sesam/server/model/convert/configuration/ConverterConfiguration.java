package sk.seges.sesam.server.model.convert.configuration;

import java.util.HashMap;
import java.util.Map;

import sk.seges.sesam.shared.model.converter.BasicConverter;

public class ConverterConfiguration {

	private Map<Class<?>, BasicConverter<?, ?>> converterConfig = new HashMap<Class<?>, BasicConverter<?,?>>();

	public void registerConverter(Class<?> clazz, BasicConverter<?, ?> converter) {
		converterConfig.put(clazz, converter);

	}
	
	public BasicConverter<?, ?> getConverter(Class<?> clazz) {
		return converterConfig.get(clazz);
	}
}