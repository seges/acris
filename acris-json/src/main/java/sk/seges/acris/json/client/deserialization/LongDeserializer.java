package sk.seges.acris.json.client.deserialization;

public class LongDeserializer extends NumberDeserializer<Long>{

	@Override
	public Long convertFromDouble(Double value) {
		return value == null ? null : value.longValue();
	}

}
