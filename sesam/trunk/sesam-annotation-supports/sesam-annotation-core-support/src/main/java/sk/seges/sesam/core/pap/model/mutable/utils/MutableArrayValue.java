package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;

class MutableArrayValue extends MutableValue implements MutableArrayTypeValue {

	private MutableArrayType type;
	
	public MutableArrayValue(MutableArrayType type, MutableTypeValue[] value) {
		super(value);
		this.type = type;
	}

	@Override
	public MutableArrayType asType() {
		return type;
	}

	@Override
	public MutableTypeValue[] getValue() {
		return (MutableTypeValue[]) super.getValue();
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
		String result = "new " + type.toString(serializer, typed) + " {";
		
		int i = 0;
		for (MutableTypeValue value: getValue()) {
			if (i > 0) {
				result += ", ";
			}
			result += value.toString(serializer, typed);
			i++;
		}
		
		result += "}";
		
		return result;
	}
}