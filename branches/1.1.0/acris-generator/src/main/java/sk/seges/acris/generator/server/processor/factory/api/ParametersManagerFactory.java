package sk.seges.acris.generator.server.processor.factory.api;

import sk.seges.acris.site.server.manager.api.ParametersManager;

public interface ParametersManagerFactory {

	ParametersManager create(String parameters);

}
