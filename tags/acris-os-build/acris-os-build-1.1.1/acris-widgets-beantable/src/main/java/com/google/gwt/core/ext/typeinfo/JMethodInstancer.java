package com.google.gwt.core.ext.typeinfo;

import java.lang.annotation.Annotation;
import java.util.Map;

public class JMethodInstancer {

	public static JMethod instanceMethod(JClassType enclosingType, String name, Map<Class<? extends Annotation>, Annotation> declaredAnnotations,
			JTypeParameter[] jtypeParameters) {
		return new JMethod(enclosingType, name, declaredAnnotations, jtypeParameters);
	}
}