package sk.seges.sesam.handler;

public interface ValueChangeHandler<T> {

	void onValueChanged(T oldValue, T newValue);
}
