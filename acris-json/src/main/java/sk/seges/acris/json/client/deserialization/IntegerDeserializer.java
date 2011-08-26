package sk.seges.acris.json.client.deserialization;

public class IntegerDeserializer extends NumberDeserializer<Integer>{

	@Override
	public Integer convertFromDouble(Double value) {
		return value == null ? null : value.intValue();
	}

}
