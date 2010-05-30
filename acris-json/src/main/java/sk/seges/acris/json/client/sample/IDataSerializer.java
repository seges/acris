package sk.seges.acris.json.client.sample;

public interface IDataSerializer<T> {
	String serialize(T data);
}