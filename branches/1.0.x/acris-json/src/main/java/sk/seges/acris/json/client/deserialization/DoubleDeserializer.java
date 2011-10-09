package sk.seges.acris.json.client.deserialization;

import com.google.gwt.i18n.client.NumberFormat;

public class DoubleDeserializer extends NumberDeserializer<Double> {

	@Override
	protected NumberFormat getNumberFormat() {
		return NumberFormat.getDecimalFormat();
	}

	@Override
	public Double convertFromDouble(Double value) {
		return value;
	}
}
