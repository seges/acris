package sk.seges.acris.site.server.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.server.manager.data.StringParameterData;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public class CommaSeparatedParametersManager implements ParametersManager {

	private Map<String, StringParameterData> parameters;

	public CommaSeparatedParametersManager(String parameters) {
		parseParameters(parameters);
	}

	@Override
	public String getParameterValue(ParameterData parameter) {
		StringParameterData parameterData = parameters.get(parameter.getKey());
		return parameterData == null ? null : parameterData.getValue();
	}

	@Override
	public Collection<? extends ParameterData> getParameters() {
		return parameters.values();
	}

	protected void parseParameters(String parameters) {
		this.parameters = new HashMap<String, StringParameterData>();

		if (parameters == null) {
			return;
		}

		String[] params = parameters.split(getSeparator());

		for (String param : params) {
			if (param != null && param.contains("=")) {
				int index = param.indexOf("=");
				String paramKey = param.substring(0, index);
				String paramValue = "";
				if (param.length() > index + 1) {
					paramValue = param.substring(index + 1);
				}
				this.parameters.put(paramKey.trim(), new StringParameterData(paramKey.trim(), paramValue.trim()));
			}
		}
	}

	protected String getSeparator() {
		return ";";
	}

	@Override
	public void setParameterValue(ParameterData parameter, Object value) {
		if (this.parameters == null) {
			this.parameters = new HashMap<String, StringParameterData>();
		}

		this.parameters.put(parameter.getKey().trim(), new StringParameterData(parameter.getKey().trim(), ((String) value).trim()));
	}

	@Override
	public void setParameters(String parameters) {
		parseParameters(parameters);
	}

	@Override
	public String store() {
		String result = "";
		
		int i = 0;
		for (Entry<String, StringParameterData> entry: parameters.entrySet()) {
			if (i > 0) {
				result += ";";
			}
			result += entry.getKey() + "=" + entry.getValue();
			i++;
		}
		
		return result;
	}
}