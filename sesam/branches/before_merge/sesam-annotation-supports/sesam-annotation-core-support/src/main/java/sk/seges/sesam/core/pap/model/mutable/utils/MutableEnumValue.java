package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

class MutableEnumValue extends MutableDeclaredValue {

	public MutableEnumValue(MutableDeclaredType type, Object value, MutableProcessingEnvironment processingEnv) {
		super(type, value, processingEnv);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return asType().toString(serializer) + "." + value.toString();
	}
}