package sk.seges.acris.json.client.samples;

import sk.seges.acris.json.client.samples.data.FooSampler;
import sk.seges.acris.json.client.samples.data.SampleData;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class SampleDataSerializer implements IDataSerializer<SampleData> {
	
	public String serialize(SampleData sample) {
		JSONString data = new JSONString(sample.getData());
		
		JSONObject foo = new JSONObject();
		
		if (sample.getFooSampler() != null) {
			JSONString value = new JSONString(sample.getFooSampler().getValue().toString());
			foo.put("value", value);
		}
		
		JSONObject sampleData = new JSONObject();
		sampleData.put("data", data);
		sampleData.put("fooSampler", foo);

		if (sample.getSamples() != null) {
			int index = 0;
			JSONArray samplesArray = new JSONArray();
			for (FooSampler sampler: sample.getSamples()) {
				JSONObject fooSa = new JSONObject();
				JSONString valueSa = new JSONString(sampler.getValue().toString());
				fooSa.put("value", valueSa);
				samplesArray.set(index++, fooSa);
			}
			
			sampleData.put("samples", samplesArray);
		}
		
		
		return sampleData.toString();
	}
}
