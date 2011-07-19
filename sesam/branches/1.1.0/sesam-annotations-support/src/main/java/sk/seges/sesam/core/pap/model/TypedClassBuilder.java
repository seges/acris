package sk.seges.sesam.core.pap.model;

import java.lang.reflect.Type;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class TypedClassBuilder {
	
	public static HasTypeParameters get(Class<?> clazz, Type... types) {
		if (types != null) {
			TypeParameter[] typeParameters = new TypeParameter[types.length];
			for (int i = 0; i < types.length; i++) {
				typeParameters[i] = TypeParameterBuilder.get(types[i]);
			}
			return new TypedOutputClass(clazz, typeParameters);
		}
		return new TypedOutputClass(clazz, (Class<?>)null);
	}

	public static HasTypeParameters get(TypeMirror type, String packageName, String className, TypeParameter... typeParameters) {
		return new TypedOutputClass(type, packageName, className, typeParameters);
	}

	public static HasTypeParameters get(Class<?> clazz, Class<?>... classes) {
		return new TypedOutputClass(clazz, classes);
	}

	public static HasTypeParameters get(Class<?> clazz, NamedType... classes) {
		return new TypedOutputClass(clazz, classes);
	}

	public static HasTypeParameters get(Type type, TypeParameter... typeParameters) {
		if (type instanceof Class) {
			return new TypedOutputClass((Class<?>)type, typeParameters);
		}

		if (type instanceof NamedType) {
			return new TypedOutputClass((NamedType)type, typeParameters);
		}
		return null;
	}

	public static HasTypeParameters get(Class<?> clazz, TypeParameter... typeParameters) {
		return new TypedOutputClass(clazz, typeParameters);
	}

	public static HasTypeParameters get(NamedType type, TypeParameter... typeParameters) {
		return new TypedOutputClass(type, typeParameters);
	}		

	public static HasTypeParameters get(NamedType type, NamedType... classes) {
		return new TypedOutputClass(type, classes);
	}		
}