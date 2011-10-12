package sk.seges.acris.site.server.manager.api;

import java.util.Collection;

import sk.seges.acris.site.shared.domain.api.ParameterData;

public interface ParametersManager {

	String getParameterValue(ParameterData parameter);

	Collection<? extends ParameterData> getParameters();
	
}