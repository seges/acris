package sk.seges.acris.scaffold.model.view;

public interface ViewContext {
	<T> T get(String key);
	void put(String key, Object value);
}
