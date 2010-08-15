package sk.seges.acris.json.client.samples;

public interface IDataSerializer<T> {
	String serialize(T data);
}