package sk.seges.acris.generator.server.manager;

import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.shared.params.OfflineParameterType;
import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.server.model.data.WebSettingsData;

import java.util.HashSet;
import java.util.Set;

public class PlainOfflineWebSettings implements OfflineWebSettings {

	private static final String PROCESSOR_SEPARATOR = ",";

	private ParametersManager parametersManager;

	public PlainOfflineWebSettings(WebSettingsData webSettings, ParametersManagerFactory parameterManagerFactory) {
		this.parametersManager = parameterManagerFactory.create(webSettings.getParameters());
	}

	public Set<String> getInactiveIndexProcessors() {
		return getInactiveProcessors(OfflineParameterType.INACTIVE_INDEX_PROCESSORS);
	}

	public Set<String> getInactiveProcessors() {
		return getInactiveProcessors(OfflineParameterType.INACTIVE_PROCESSORS);
	}

	protected Set<String> getInactiveProcessors(OfflineParameterType parameter) {
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
		String parameterValue = (String) parametersManager.getParameterValue(OfflineParameterType.AUTODETECT_MODE);
		return (parameterValue != null && parameterValue.trim().toLowerCase().equals(Boolean.TRUE.toString().toLowerCase()));
	}

	@Override
	public boolean publishOnSaveEnabled() {
		String parameterValue = (String) parametersManager.getParameterValue(OfflineParameterType.PUBLISH_ON_SAVE_ENABLED);
		return (parameterValue != null && parameterValue.trim().toLowerCase().equals(Boolean.TRUE.toString().toLowerCase()));
	}
}
