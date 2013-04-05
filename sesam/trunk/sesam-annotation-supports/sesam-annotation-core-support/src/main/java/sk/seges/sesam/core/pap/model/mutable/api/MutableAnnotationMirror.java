package sk.seges.sesam.core.pap.model.mutable.api;

import java.util.Map;

public interface MutableAnnotationMirror {

    MutableDeclaredType getAnnotationType();

    Map<? extends MutableExecutableType, ? extends MutableTypeValue> getElementValues();

    MutableAnnotationMirror setAnnotationValue(String methodName, MutableTypeValue value);
}