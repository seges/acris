package sk.seges.acris.json.client.adapter;

import java.util.Map;

import org.gwttime.time.DateTime;
import org.gwttime.time.format.DateTimeFormat;

import sk.seges.acris.json.client.annotation.DateTimePattern;
import sk.seges.acris.json.client.provider.BaseJsonAdapter;

import com.google.gwt.json.client.JSONValue;

public class DateTimeAdapter extends BaseJsonAdapter<DateTime> {

	public static final String PATTERN = DateTimePattern.class.getName() + "_value";
	
	@Override
	public DateTime setValue(JSONValue s, String property,
			Map<String, String> attributes) {
		String dateTimeString = getStringValue(s, property);
		if (dateTimeString == null) {
			return null;
		}

		String pattern = attributes.get(PATTERN);
		
		return DateTimeFormat.forPattern(pattern).parseDateTime(dateTimeString);
	}

}
