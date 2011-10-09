/**
 * 
 */
package sk.seges.sesam.dao;


/**
 * @author eldzi
 * 
 * @param <T>
 *            Type of value expression has to compare using operation for the
 *            property.
 */
public class NotNullExpression implements Criterion {
	private static final long serialVersionUID = -6109098619832880962L;

	private static final String operation = "isNotNull";
	
	protected String property;

	public NotNullExpression() {}

	public NotNullExpression(String property) {
		this.property = property;
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


	public String getOperation() {
		return operation;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotNullExpression other = (NotNullExpression) obj;
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
		return "[ op = " + operation + ", prop = " + property + " ]";
	}
}
