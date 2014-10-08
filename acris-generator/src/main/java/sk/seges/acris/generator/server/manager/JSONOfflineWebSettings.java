package sk.seges.acris.generator.server.manager;

import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.shared.params.OfflineParameterType;
import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.server.model.data.WebSettingsData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSONOfflineWebSettings implements OfflineWebSettings {

	private ParametersManager parametersManager;

	public JSONOfflineWebSettings(WebSettingsData webSettings, ParametersManagerFactory parameterManagerFactory) {
		this.parametersManager = parameterManagerFactory.create(webSettings.getParameters());
	}

	@Override
	public Set<String> getInactiveIndexProcessors() {
		return getInactiveProcessors(OfflineParameterType.INACTIVE_INDEX_PROCESSORS);
	}

	@Override
	public Set<String> getInactiveProcessors() {
		return getInactiveProcessors(OfflineParameterType.INACTIVE_PROCESSORS);
	}

	@SuppressWarnings("unchecked")
	protected Set<String> getInactiveProcessors(OfflineParameterType parameter) {
		HashSet<String> hashSet = new HashSet<String>();

		if (parameter == null) {
			return hashSet;
		}

		if (parametersManager.getParameterValue(parameter) instanceof String[]) {
			String[] parameterValue = (String[]) parametersManager.getParameterValue(parameter);
			if (parameterValue == null) {
				return hashSet;
			}

			for (String inactiveProcessor : parameterValue) {
				if (inactiveProcessor != null) {
					hashSet.add(inactiveProcessor.trim());
				}
			}
		} else if (parametersManager.getParameterValue(parameter) instanceof List) {
			List<String> parameterValue = (List<String>) parametersManager.getParameterValue(parameter);
			if (parameterValue == null) {
				return hashSet;
			}

			for (String inactiveProcessor : parameterValue) {
				if (inactiveProcessor != null) {
					hashSet.add(inactiveProcessor.trim());
				}
			}
		}

		return hashSet;
	}

	@Override
	public boolean supportsAutodetectMode() {
		Boolean parameterValue = (Boolean) parametersManager.getParameterValue(OfflineParameterType.AUTODETECT_MODE);
		return (parameterValue != null && parameterValue);
	}

	@Override
	public boolean publishOnSaveEnabled() {
		Boolean parameterValue = (Boolean) parametersManager.getParameterValue(OfflineParameterType.PUBLISH_ON_SAVE_ENABLED);
		return (parameterValue != null && parameterValue);
	}
}
