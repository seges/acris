package sk.seges.sesam.core.pap.model;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.model.InputClass.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.NamedType;

public class TypedClass implements HasTypeParameters {
	
	public interface TypeParameter {

		public static final String UNDEFINED = "?";
		
		String getVariable();

		Type getBounds();
	}

	public static class TypeParameterBuilder {
		public static TypeParameterClass get(String variable, Type bounds) {
			return new TypeParameterClass(variable, bounds);
		}

		public static TypeParameterClass get(String variable) {
			return new TypeParameterClass(variable);
		}

		public static TypeParameterClass get(Type bounds) {
			return new TypeParameterClass(bounds);
		}
	}
	
	static class TypeParameterClass implements TypeParameter {

		private String variable;
		private Type bounds;
		
		public TypeParameterClass(String variable) {
			this.variable = variable;
		}

		public TypeParameterClass(Type bounds) {
			this.bounds = bounds;
		}

		public TypeParameterClass(String variable, Type bounds) {
			this(variable);
			this.bounds = bounds;
		}

		@Override
		public String getVariable() {
			return variable;
		}

		@Override
		public Type getBounds() {
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
				
				if (getBounds().equals(NamedType.THIS)) {
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
	
	private NamedType namedType;
	private TypeParameter[] typeParameters;
	
	public TypedClass(Class<?> clazz, Class<?>... classes) {
		this.namedType = new InputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		if (classes != null) {
			typeParameters = new TypeParameter[classes.length];
			for (int i = 0; i < classes.length; i++) {
				typeParameters[i] = new TypeParameterClass(classes[i]);
			}
		}
	}

	public TypedClass(Class<?> clazz, NamedType... classes) {
		this.namedType = new InputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		if (classes != null) {
			typeParameters = new TypeParameter[classes.length];
			for (int i = 0; i < classes.length; i++) {
				typeParameters[i] = new TypeParameterClass(classes[i]);
			}
		}
	}

	public TypedClass(Class<?> clazz, TypeParameter... typeParameters) {
		this.namedType = new InputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		this.typeParameters = typeParameters;
	}

	public TypedClass(NamedType type, TypeParameter... typeParameters) {
		this.namedType = type;
		this.typeParameters = typeParameters;
	}

	public TypeParameter[] getTypeParameters() {
		return typeParameters;
	}
	
	public static TypedClass get(Class<?> clazz, Class<?>... classes) {
		return new TypedClass(clazz, classes);
	}

	public static TypedClass get(Class<?> clazz, NamedType... classes) {
		return new TypedClass(clazz, classes);
	}

	public static TypedClass get(Class<?> clazz, TypeParameter... typeParameters) {
		return new TypedClass(clazz, typeParameters);
	}

	public static TypedClass get(NamedType type, TypeParameter... typeParameters) {
		return new TypedClass(type, typeParameters);
	}

	public String toString() {
		
		String resultName = getCanonicalName();
		
		if (getTypeParameters() == null || getTypeParameters().length == 0) {
			return resultName;
		}
		
		return resultName + TypedUtils.toString(this);
	}
	
	@Override
	public String getCanonicalName() {
		return namedType.getCanonicalName();
	}
	
	@Override
	public String getSimpleName() {
		return namedType.getSimpleName();
	}

	@Override
	public String getQualifiedName() {
		return namedType.getQualifiedName();
	}

	@Override
	public String getPackageName() {
		return namedType.getPackageName();
	}
}