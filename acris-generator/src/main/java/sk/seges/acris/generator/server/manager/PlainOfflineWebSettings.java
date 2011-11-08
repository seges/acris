package sk.seges.acris.generator.server.manager;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.manager.data.OfflineGeneratorParameter;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public class PlainOfflineWebSettings implements OfflineWebSettings {

	private static final String PROCESSOR_SEPARATOR = ",";

	private ParametersManager parametersManager;

	public PlainOfflineWebSettings(WebSettingsData webSettings, ParametersManagerFactory parameterManagerFactory) {
		this.parametersManager = parameterManagerFactory.create(webSettings.getParameters());
	}

	public Set<String> getInactiveIndexProcessors() {
		return getInactiveProcessors(OfflineGeneratorParameter.INACTIVE_INDEX_PROCESSORS);
	}

	public Set<String> getInactiveProcessors() {
		return getInactiveProcessors(OfflineGeneratorParameter.INACTIVE_PROCESSORS);
	}

	protected Set<String> getInactiveProcessors(OfflineGeneratorParameter parameter) {
		HashSet<String> hashSet = new HashSet<String>();

		if (parameter == null) {
			return hashSet;
		}

		String parameterValue = (String) parametersManager.getParameterValue(parameter);
		if (parameterValue == null) {
			return hashSet;
		}

		String[] inactiveProcessors = parameterValue.split(PROCESSOR_SEPARATOR);
		for (String inactiveProcessor : inactiveProcessors) {
			if (inactiveProcessor != null) {
				hashSet.add(inactiveProcessor.trim());
			}
		}

		return hashSet;
	}

	public boolean supportsAutodetectMode() {
		String parameterValue = (String) parametersManager.getParameterValue(OfflineGeneratorParameter.AUTODETECT_MODE);
		return (parameterValue != null && parameterValue.trim().toLowerCase().equals(Boolean.TRUE.toString().toLowerCase()));
	}

	@Override
	public boolean publishOnSaveEnabled() {
		String parameterValue = (String) parametersManager.getParameterValue(OfflineGeneratorParameter.PUBLISH_ON_SAVE_ENABLED);
		return (parameterValue != null && parameterValue.trim().toLowerCase().equals(Boolean.TRUE.toString().toLowerCase()));
	}
}
