package sk.seges.acris.json.client.adapter;

import java.util.Map;

import sk.seges.acris.json.client.provider.BaseJsonAdapter;

import com.google.gwt.json.client.JSONValue;

public class StringAdapter extends BaseJsonAdapter<String> {

	@Override
	public String setValue(JSONValue s, String property,
			Map<String, String> attributes) {
		return getStringValue(s, property);
	}
}