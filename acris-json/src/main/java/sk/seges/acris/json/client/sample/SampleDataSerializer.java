package sk.seges.acris.json.client.sample;

import sk.seges.acris.json.client.sample.data.SampleData;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class SampleDataSerializer implements IDataSerializer<SampleData> {
	
	public String serialize(SampleData sample) {
		JSONString data = new JSONString(sample.getData());
		
		JSONObject foo = new JSONObject();
		JSONString value = new JSONString(sample.getFooSampler().getValue().toString());
		foo.put("value", value);
		
		JSONObject sampleData = new JSONObject();
		sampleData.put("data", data);
		sampleData.put("fooSampler", foo);
		
		return sampleData.toString();
	}
}
