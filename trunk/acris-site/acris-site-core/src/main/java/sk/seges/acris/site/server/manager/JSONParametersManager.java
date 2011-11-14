package sk.seges.acris.site.server.manager;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import sk.seges.acris.site.client.json.params.WebParams;
import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.server.manager.data.ObjectParameterData;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public class JSONParametersManager implements ParametersManager {

	private Map<String, ObjectParameterData> parameters;
	private HashMap<String, Object> map;

	private final ObjectMapper mapper;

	public JSONParametersManager(String parameters) {
		mapper = new ObjectMapper();
		parseParameters(parameters);
	}

	@Override
	public Object getParameterValue(ParameterData parameter) {
		ObjectParameterData parameterData = parameters.get(parameter.getKey());
		return parameterData == null ? null : parameterData.getValue();
	}

	@Override
	public Collection<? extends ParameterData> getParameters() {
		return parameters.values();
	}

	protected void parseParameters(String parameters) {
		this.parameters = new HashMap<String, ObjectParameterData>();

		if (parameters == null) {
			return;
		}

		try {
			map = mapper.readValue(parameters, new TypeReference<HashMap<String, Object>>() {
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (map == null) {
			return;
		}

		this.parameters.put(WebParams.PUBLISH_ON_SAVE_ENABLED,
				new ObjectParameterData(WebParams.PUBLISH_ON_SAVE_ENABLED, map.get(WebParams.PUBLISH_ON_SAVE_ENABLED)));
		this.parameters.put(WebParams.OFFLINE_AUTODETECT_MODE,
				new ObjectParameterData(WebParams.OFFLINE_AUTODETECT_MODE, map.get(WebParams.OFFLINE_AUTODETECT_MODE)));
		this.parameters.put(WebParams.OFFLINE_INDEX_PROCESSOR_INACTIVE, new ObjectParameterData(WebParams.OFFLINE_INDEX_PROCESSOR_INACTIVE,
				map.get(WebParams.OFFLINE_INDEX_PROCESSOR_INACTIVE)));
		this.parameters.put(WebParams.OFFLINE_POST_PROCESSOR_INACTIVE,
				new ObjectParameterData(WebParams.OFFLINE_POST_PROCESSOR_INACTIVE, map.get(WebParams.OFFLINE_POST_PROCESSOR_INACTIVE)));
	}

	@Override
	public void setParameterValue(ParameterData parameter, Object value) {
		if (this.parameters == null) {
			this.parameters = new HashMap<String, ObjectParameterData>();
		}

		this.parameters.put(parameter.getKey().trim(), new ObjectParameterData(parameter.getKey().trim(), value));
	}

	@Override
	public void setParameters(String parameters) {
		parseParameters(parameters);
	}
}
