package sk.seges.sesam.core.pap.model;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.model.api.TypeVariable;

class TypeBounds implements TypeVariable {

	private Type type;

	public TypeBounds(Type type) {
		this.type = type;
	}

	@Override
	public Type getUpperBound() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TypeBounds other = (TypeBounds) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}