package sk.seges.acris.json.client.sample.data;

import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.annotation.JsonObject;

@JsonObject
public class SampleData {

	@Field
	private String data;

	@Field
	private FooSampler fooSampler;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public FooSampler getFooSampler() {
		return fooSampler;
	}

	public void setFooSampler(FooSampler fooSampler) {
		this.fooSampler = fooSampler;
	}
}