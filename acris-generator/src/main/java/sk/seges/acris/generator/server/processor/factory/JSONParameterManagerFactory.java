package sk.seges.acris.generator.server.processor.factory;

import org.codehaus.jackson.map.ObjectMapper;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.site.server.manager.JSONParametersManager;
import sk.seges.acris.site.server.manager.api.ParametersManager;

public class JSONParameterManagerFactory implements ParametersManagerFactory {

	private final ObjectMapper objectMapper;

	public JSONParameterManagerFactory(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public ParametersManager create(String parameters) {
		return new JSONParametersManager(parameters, objectMapper);
	}
}
