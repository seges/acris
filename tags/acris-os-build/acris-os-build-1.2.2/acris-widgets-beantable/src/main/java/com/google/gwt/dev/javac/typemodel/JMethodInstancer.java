package com.google.gwt.dev.javac.typemodel;

import java.lang.annotation.Annotation;
import java.util.Map;

public class JMethodInstancer {

	public static JMethod instanceMethod(JClassType enclosingType, String name, Map<Class<? extends Annotation>, Annotation> declaredAnnotations,
			JTypeParameter[] jtypeParameters) {
		return new JMethod(enclosingType, name, declaredAnnotations, jtypeParameters);
	}
}