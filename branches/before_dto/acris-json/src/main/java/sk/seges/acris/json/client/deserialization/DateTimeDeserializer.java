package sk.seges.acris.json.client.deserialization;

import org.gwttime.time.DateTime;
import org.gwttime.time.format.DateTimeFormat;

import sk.seges.acris.json.client.annotation.DateTimePattern;
import sk.seges.acris.json.client.context.DeserializationContext;

import com.google.gwt.json.client.JSONValue;

public class DateTimeDeserializer extends BaseJsonDeserializer<DateTime> {

	public static final String PATTERN = DateTimePattern.class.getName() + "_value";

	@Override
	public DateTime deserialize(JSONValue s, DeserializationContext context) {
		String dateTimeString = _deserialize(s);
		if (dateTimeString == null) {
			return null;
		}

		String pattern = context.getAttribute(PATTERN);

		return DateTimeFormat.forPattern(pattern).parseDateTime(dateTimeString);
	}

}
