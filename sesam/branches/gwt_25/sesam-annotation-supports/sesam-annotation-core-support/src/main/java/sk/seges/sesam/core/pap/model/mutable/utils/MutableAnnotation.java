package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.HashMap;
import java.util.Map;

import sk.seges.sesam.core.pap.model.mutable.api.MutableAnnotationMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;

class MutableAnnotation implements MutableAnnotationMirror {

	private final MutableDeclaredType type;
	private Map<MutableExecutableType, MutableTypeValue> values = new HashMap<MutableExecutableType, MutableTypeValue>();
	private final MutableProcessingEnvironment processingEnv;
	
	public MutableAnnotation(MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		this.type = type;
		this.processingEnv = processingEnv;
	}
	
	@Override
	public MutableDeclaredType getAnnotationType() {
		return type;
	}

	@Override
	public Map<? extends MutableExecutableType, ? extends MutableTypeValue> getElementValues() {
		return values;
	}

	@Override
	public MutableAnnotationMirror setAnnotationValue(String methodName, MutableTypeValue value) {
		MutableExecutableType methodType = processingEnv.getElementUtils().getExecutableElement(methodName).asType();
		values.put(methodType, value);
		return this;
	}
}