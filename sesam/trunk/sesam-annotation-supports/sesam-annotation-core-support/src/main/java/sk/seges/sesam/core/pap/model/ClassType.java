package sk.seges.sesam.core.pap.model;

import java.lang.annotation.Annotation;

import sk.seges.sesam.core.pap.model.api.TypeDelegate;

@SuppressWarnings("rawtypes")
public class ClassType implements TypeDelegate<Class> {
  
	public static interface AnnotationProcessor<X extends Annotation> {

		Class<?> getAnnotationValue(X annotation);
	}
	
	private String className;
	 
	public ClassType(String className) {
		this.className = className;
	}

	public ClassType(Class<?> clazz) {
		this.className = clazz.getName();
	}

	public String getQualifiedName() {
		return className;
	}
	
	@Override
	public Class<Class> getDelegateClass() {
		return Class.class;
	}
}