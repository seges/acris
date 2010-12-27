package sk.seges.acris.json.client;

public interface InstanceCreator<T> {
	public T createInstance(Class<T> type);
}