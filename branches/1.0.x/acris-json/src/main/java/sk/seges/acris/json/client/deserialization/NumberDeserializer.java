package sk.seges.acris.json.client.deserialization;

import sk.seges.acris.json.client.annotation.NumberPattern;
import sk.seges.acris.json.client.context.DeserializationContext;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONValue;

public abstract class NumberDeserializer<T extends Number> extends BaseJsonDeserializer<T> {
	
	public static final String PATTERN = NumberPattern.class.getName() + "_value";

	protected NumberFormat getNumberFormat() {
		return NumberFormat.getDecimalFormat();
	}
	
	public abstract T convertFromDouble(Double value);
	
	@Override
	public T deserialize(JSONValue s, DeserializationContext context) {
		if (s.isNumber() != null) {
			return convertFromDouble(s.isNumber().doubleValue());
		}
		
		String numberString = _deserialize(s);
		if (numberString == null) {
			return null;
		}

		String pattern = context.getAttribute(PATTERN);

		if (pattern != null) {
			return convertFromDouble(NumberFormat.getFormat(pattern).parse(numberString));
		}
		return convertFromDouble(getNumberFormat().parse(numberString));
	}
}
