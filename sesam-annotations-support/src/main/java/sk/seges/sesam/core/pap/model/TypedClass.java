package sk.seges.sesam.core.pap.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.InputClass.HasTypeParameters;

public class TypedClass implements AnnotatedElement, HasTypeParameters {
	
	public interface TypeParameter {

		public static final String UNDEFINED = "?";
		
		String getVariable();

		AnnotatedElement getBounds();

	}
	
	public static class TypeParameterBuilder {
		public static TypeParameterClass get(String variable, AnnotatedElement bounds) {
			return new TypeParameterClass(variable, bounds);
		}

		public static TypeParameterClass get(String variable) {
			return new TypeParameterClass(variable);
		}

		public static TypeParameterClass get(AnnotatedElement bounds) {
			return new TypeParameterClass(bounds);
		}
		
	}
	
	static class TypeParameterClass implements TypeParameter {

		private String variable;
		private AnnotatedElement bounds;
		
		public TypeParameterClass(String variable) {
			this.variable = variable;
		}

		public TypeParameterClass(AnnotatedElement bounds) {
			this.bounds = bounds;
		}

		public TypeParameterClass(String variable, AnnotatedElement bounds) {
			this(variable);
			this.bounds = bounds;
		}

		@Override
		public String getVariable() {
			return variable;
		}

		@Override
		public AnnotatedElement getBounds() {
			return bounds;
		}
		
		public String toString() {
			String result = "";
			
			if (getVariable() != null) {
				result += getVariable() + " ";
			}
			
			if (getBounds() != null) {
				if (getVariable() != null) {
					result += "extends ";
				}
				
				if (getBounds().equals(AbstractConfigurableProcessor.THIS)) {
					result += "_THIS_";
				} else {
					if (getBounds() instanceof Class) {
						result += ((Class<?>)getBounds()).getCanonicalName();
					} else {
						result += getBounds().toString();
					}
				}
			}
			
			return result;
		}

	}
	
	private Class<?> clazz;

	private TypeParameter[] typeParameters;
	
	public TypedClass(Class<?> clazz, Class<?>... classes) {
		this.clazz = clazz;
		if (classes != null) {
			typeParameters = new TypeParameter[classes.length];
			for (int i = 0; i < classes.length; i++) {
				typeParameters[i] = new TypeParameterClass(classes[i]);
			}
		}
	}
	
	public TypedClass(Class<?> clazz, TypeParameter... typeParameters) {
		this.clazz = clazz;
		this.typeParameters = typeParameters;
	}
	
	@Override
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return clazz.isAnnotationPresent(annotationClass);
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return clazz.getAnnotation(annotationClass);
	}

	@Override
	public Annotation[] getAnnotations() {
		return clazz.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return clazz.getDeclaredAnnotations();
	}

	public Class<?> getTypedClass() {
		return clazz;
	}
	
	public TypeParameter[] getTypeParameters() {
		return typeParameters;
	}
	
	public static TypedClass get(Class<?> clazz, Class<?>... classes) {
		return new TypedClass(clazz, classes);
	}

	public static TypedClass get(Class<?> clazz, TypeParameter... typeParameters) {
		return new TypedClass(clazz, typeParameters);
	}
	
	public String toString() {
		
		String resultName = getTypedClass().getCanonicalName();
		
		if (getTypeParameters() == null || getTypeParameters().length == 0) {
			return resultName;
		}
		
		return resultName + TypedUtils.toString(this);
	}
	
	@Override
	public String getCanonicalName() {
		return clazz.getCanonicalName();
	}
	
	@Override
	public String getSimpleName() {
		return clazz.getSimpleName();
	}
}