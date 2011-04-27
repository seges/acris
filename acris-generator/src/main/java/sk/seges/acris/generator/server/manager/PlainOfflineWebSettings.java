package sk.seges.acris.generator.server.manager;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.shared.domain.api.ParameterData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public class PlainOfflineWebSettings implements OfflineWebSettings {

	public enum OfflineGeneratorParameter implements ParameterData {

		INACTIVE_PROCESSORS("offline.post.processor.inactive"),
		INACTIVE_INDEX_PROCESSORS("offline.index.post.processor.inactive"),
		AUTODETECT_MODE("offline.autodetect.mode");

		private String key;
		
		OfflineGeneratorParameter(String key) {
			this.key = key;
		}
		
		@Override
		public String getKey() {
			return key;
		}
	}
	
	private static final String PROCESSOR_SEPARATOR = ",";

	private WebSettingsData webSettings;
	private ParametersManager parametersManager;
	
	public PlainOfflineWebSettings(WebSettingsData webSettings, ParametersManagerFactory parameterManagerFactory) {
		this.webSettings = webSettings;
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
		
		String parameterValue = parametersManager.getParameterValue(parameter);
		if (parameterValue == null) {
			return hashSet;
		}
		
		String[] inactiveProcessors = parameterValue.split(PROCESSOR_SEPARATOR);
		for (String inactiveProcessor: inactiveProcessors) {
			if (inactiveProcessor != null) {
				hashSet.add(inactiveProcessor.trim());
			}
		}
		
		return hashSet;
	}

	public boolean supportsAutodetectMode() {
		String parameterValue = parametersManager.getParameterValue(OfflineGeneratorParameter.AUTODETECT_MODE);
		return (parameterValue != null && parameterValue.trim().toLowerCase().equals(Boolean.TRUE.toString().toLowerCase()));
	}
}