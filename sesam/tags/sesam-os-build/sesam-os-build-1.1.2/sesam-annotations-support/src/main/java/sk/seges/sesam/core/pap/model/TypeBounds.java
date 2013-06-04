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
}