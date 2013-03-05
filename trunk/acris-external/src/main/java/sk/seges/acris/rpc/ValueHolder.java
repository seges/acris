/**
 * 
 */
package sk.seges.acris.rpc;

public class ValueHolder<T> {
	private T value;

	public ValueHolder() {
	}

	public ValueHolder(T value) {
		this.value = value;
	}

	public void setValue(T object) {
		this.value = object;
	}

	public T getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ValueHolder<T> other = (ValueHolder<T>) obj;
		if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
		return hash;
	}
}