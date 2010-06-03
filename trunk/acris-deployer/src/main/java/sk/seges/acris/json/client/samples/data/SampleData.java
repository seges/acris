package sk.seges.acris.json.client.samples.data;

import java.util.Collection;

import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.annotation.JsonObject;

@JsonObject
public class SampleData {

	@Field
	private String data;

	@Field
	private FooSampler fooSampler;

	@Field
	private Collection<FooSampler> samples;
	
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

	public Collection<FooSampler> getSamples() {
		return samples;
	}

	public void setSamples(Collection<FooSampler> samples) {
		this.samples = samples;
	}
}