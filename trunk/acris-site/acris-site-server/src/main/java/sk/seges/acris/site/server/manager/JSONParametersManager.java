package sk.seges.acris.site.server.manager;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.server.manager.data.ObjectParameterData;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public class JSONParametersManager implements ParametersManager {

	private Map<String, ObjectParameterData> parameters;

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

		Map<String, Object> map = null;
		
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

		for (Entry<String, Object> params: map.entrySet()) {
			this.parameters.put(params.getKey(), new ObjectParameterData(params.getKey(), params.getValue()));
		}
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

	@Override
	public String store() {
		StringWriter stringWriter = new StringWriter();

		Map<String, Object> map = new HashMap<String, Object>();

		for (Entry<String, ObjectParameterData> params: parameters.entrySet()) {
			map.put(params.getKey(), params.getValue().getValue());
		}

		try {
			mapper.writeValue(stringWriter, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return stringWriter.toString();
	}
}
