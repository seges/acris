/**
 * 
 */
package sk.seges.sesam.dao;

import java.io.Serializable;

/**
 * @author eldzi
 * 
 * @param <T>
 *            Type of value expression has to compare using operation for the
 *            property.
 */
public class SimpleExpression<T extends Serializable> implements Criterion {
	private static final long serialVersionUID = 8159534010141240168L;
	
	protected String property;
	protected T value;
	protected String operation;

	public SimpleExpression() {}

	public SimpleExpression(String property, String operation) {
		this.property = property;
		this.operation = operation;
	}
	
	public SimpleExpression(String property, T value, String operation) {
		this.property = property;
		this.value = value;
		this.operation = operation;
	}

	public String getProperty() {
		return property;
	}

	/**
	 * Don't use directly. Use {@link Filter} class or multiparameter
	 * constructor for construction. This is only because of serialization
	 * constraints.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getOperation() {
		return operation;
	}

	/**
	 * Don't use directly. Use {@link Filter} class or multiparameter
	 * constructor for construction. This is only because of serialization
	 * constraints.
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleExpression other = (SimpleExpression) obj;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[ op = " + operation + ", prop = " + property + ", value = " + value + " ]";
	}
}
