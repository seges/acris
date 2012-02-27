package sk.seges.sesam.pap.model.utils;

public class InitializableValue<T> {
	
	private T value;
	private boolean isInitialized = false;
	
	public T setValue(T value) {
		this.value = value;
		this.isInitialized = true;
		return value;
	}
	
	public T getValue() {
		return value;
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}
	
	public void setInitialized() {
		this.isInitialized = true;
	}
}
