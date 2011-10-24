package sk.seges.acris.json.client.samples.data;

import org.gwttime.time.DateTime;

import sk.seges.acris.json.client.annotation.DateTimePattern;
import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.annotation.JsonObject;
import sk.seges.acris.json.client.extension.ExtensionPoint;

@JsonObject
public class FooSampler extends ExtensionPoint {

	@Field
	@DateTimePattern("y-M-d'T'H:m:s.SSSZ")
	// @DateTimePattern("yyyy-MM-ddTHH:mm:ss.SSSZZ")
	private DateTime value;

	public DateTime getValue() {
		return value;
	}

	public void setValue(DateTime value) {
		this.value = value;
	}
}