package sk.seges.acris.generator.server.processor.factory;

import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.site.server.manager.JSONParametersManager;
import sk.seges.acris.site.server.manager.api.ParametersManager;

public class JSONParameterManagerFactory implements ParametersManagerFactory {

	public ParametersManager create(String parameters) {
		return new JSONParametersManager(parameters);
	}
}
