package sk.seges.acris.theme.pap.accessor;

import java.util.Map;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableAnnotationMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class MutableAnnotationAccessor extends AnnotationAccessor implements MutableAnnotationMirror {

	private static final String ANNOTATION_VALUE = "value";

	private final MutableAnnotationMirror mutableAnnotation;
	private final MutableProcessingEnvironment processingEnv;
	
	public MutableAnnotationAccessor(Class<?> annotationClazz, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.mutableAnnotation = processingEnv.getTypeUtils().toMutableAnnotation(annotationClazz);
		this.processingEnv = processingEnv;
	}

	@Override
	public MutableDeclaredType getAnnotationType() {
		return mutableAnnotation.getAnnotationType();
	}

	@Override
	public Map<? extends MutableExecutableType, ? extends MutableTypeValue> getElementValues() {
		return mutableAnnotation.getElementValues();
	}

	@Override
	public MutableAnnotationMirror setAnnotationValue(String methodName, MutableTypeValue value) {
		return mutableAnnotation.setAnnotationValue(methodName, value);
	}

	public MutableAnnotationMirror setAnnotationValue(String methodName, Object value) {
		return mutableAnnotation.setAnnotationValue(methodName, processingEnv.getTypeUtils().getTypeValue(value));
	}
	
	public MutableAnnotationAccessor setValue(MutableTypeValue value) {
		setAnnotationValue(ANNOTATION_VALUE, value);
		return this;
	}

	public MutableAnnotationAccessor setValue(Object value) {
		setAnnotationValue(ANNOTATION_VALUE, value);
		return this;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}