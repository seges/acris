package sk.seges.acris.site.server.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public class CommaSeparatedParametersManager implements ParametersManager {

	class ValueParameterData implements ParameterData {

		private String key;
		private String value;
		
		public ValueParameterData(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}
	
	private Map<String, ValueParameterData> parameters;
	
	public CommaSeparatedParametersManager(String parameters) {
		parseParameters(parameters);
	}
	
	@Override
	public String getParameterValue(ParameterData parameter) {
		ValueParameterData parameterData = parameters.get(parameter.getKey());
		return parameterData == null ? null : parameterData.getValue();
	}

	@Override
	public Collection<? extends ParameterData> getParameters() {
		return parameters.values();
	}

	protected void parseParameters(String parameters) {

		this.parameters = new HashMap<String, ValueParameterData>();

		if (parameters == null) {
			return;
		}
		
		String[] params = parameters.split(getSeparator());
		
		for (String param: params) {
			if (param != null && param.contains("=")) {
				int index = param.indexOf("=");
				String paramKey = param.substring(0, index);
				String paramValue = "";
				if (param.length() > index + 1) {
					paramValue = param.substring(index + 1);
				}
				this.parameters.put(paramKey.trim(), new ValueParameterData(paramKey.trim(), paramValue.trim()));
			}
		}
	}

	protected String getSeparator() {
		return ";";
	}
}