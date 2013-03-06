package sk.seges.acris.pivo;

public class Value<T> implements IDependency {
	private T value;
	
	public Value(T value) {
		this.value = value;
	}
	
	public T resolve(ChocolateFactory factory) {
		return value;
	}
}
