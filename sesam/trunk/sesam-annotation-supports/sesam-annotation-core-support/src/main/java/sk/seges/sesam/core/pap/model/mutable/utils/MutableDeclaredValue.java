package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;

class MutableDeclaredValue extends MutableValue implements MutableDeclaredTypeValue {

	private MutableDeclaredType type;
	
	public MutableDeclaredValue(MutableDeclaredType type, Object value) {
		super(value);
		this.type = type;
	}

	@Override
	public MutableDeclaredType asType() {
		return type;
	}
	
	@Override
	public String toString() {
		return toString(ClassSerializer.CANONICAL);
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return toString(serializer, true);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		if (value == null) {
			return "null";
		}

		if (value instanceof MutableType) {
			return ((MutableType)value).toString(serializer, typed);
		}

		if (value instanceof String) {
			return "\"" + value.toString() + "\"";
		}
		
		return value.toString();
	}

}