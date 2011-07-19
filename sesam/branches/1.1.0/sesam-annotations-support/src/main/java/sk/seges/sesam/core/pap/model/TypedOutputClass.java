package sk.seges.sesam.core.pap.model;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

class TypedOutputClass extends OutputClass implements HasTypeParameters {

	private TypeParameter[] typeParameters;

	public TypedOutputClass(TypeMirror type, String packageName, String className, TypeParameter... typeParameters) {
		super(type, packageName, className);
		this.typeParameters = typeParameters;
	}

	public TypedOutputClass(Class<?> clazz, Class<?>... classes) {
		this(null, clazz.getPackage().getName(), clazz.getSimpleName());
		if (classes != null) {
			typeParameters = new TypeParameter[classes.length];
			for (int i = 0; i < classes.length; i++) {
				typeParameters[i] = TypeParameterBuilder.get(classes[i]);
			}
		}
	}

	public TypedOutputClass(Class<?> clazz, NamedType... classes) {
		this(null, clazz.getPackage().getName(), clazz.getSimpleName());
		if (classes != null) {
			typeParameters = new TypeParameter[classes.length];
			for (int i = 0; i < classes.length; i++) {
				typeParameters[i] = TypeParameterBuilder.get(classes[i]);
			}
		}
	}

	public TypedOutputClass(Class<?> clazz, TypeParameter... typeParameters) {
		this(null, clazz.getPackage().getName(), clazz.getSimpleName());
		this.typeParameters = typeParameters;
	}

	public TypedOutputClass(NamedType type, TypeParameter... typeParameters) {
		this(type instanceof MutableType ? ((MutableType)type).asType() : null, type.getPackageName(), type.getSimpleName());
		this.typeParameters = typeParameters;
	}

	public TypedOutputClass(NamedType type, NamedType... classes) {
		this(type instanceof MutableType ? ((MutableType)type).asType() : null, type.getPackageName(), type.getSimpleName());
		if (classes != null) {
			typeParameters = new TypeParameter[classes.length];
			for (int i = 0; i < classes.length; i++) {
				typeParameters[i] = TypeParameterBuilder.get(classes[i]);
			}
		}
	}

	public String toString(NamedType inputClass, ClassSerializer serializer, boolean typed) {

		String resultName;
		
		if (toString(ClassSerializer.QUALIFIED).equals(NamedType.THIS.getName())) {
			resultName = inputClass.toString(serializer);
		} else {
			resultName = this.toString(serializer);
		}
		
		if (!typed || this.getTypeParameters() == null || this.getTypeParameters().length == 0) {
			return resultName;
		}

		String types = "<";

		int i = 0;

		for (TypeParameter typeParameter : this.getTypeParameters()) {
			if (i > 0) {
				types += ", ";
			}
			types += typeParameter.toString(inputClass, ClassSerializer.SIMPLE);
			i++;
		}

		types += ">";

		return resultName + types;
	}

	public TypeParameter[] getTypeParameters() {
		return typeParameters;
	}

	@Override
	protected TypedOutputClass clone() {
		return new TypedOutputClass(asType(), getPackageName(), getClassName(), typeParameters);
	}

	public TypedOutputClass addType(TypeParameter typeParameter) {
		TypeParameter[] params = new TypeParameter[typeParameters.length + 1];
		for (int i = 0; i < typeParameters.length; i++) {
			params[i] = typeParameters[i];
		}
		params[typeParameters.length] = typeParameter;

		return new TypedOutputClass(asType(), getPackageName(), getClassName(), params);
	}

	String toString(HasTypeParameters hasTypeParameters) {
		String types = "<";
		
		int i = 0;
		
		for (TypeParameter typeParameter: hasTypeParameters.getTypeParameters()) {
			if (i > 0) {
				types += ", ";
			}
			types += typeParameter.toString();
			i++;
		}
		
		types += ">";
		
		return types;
	}
	
	@Override
	public String toString() {
		return super.toString() + toString(this);
	}

	@Override
	public MutableType stripTypeParameters() {
		if (getEnclosedClass() != null) {
			return new OutputClass(asType(), getEnclosedClass(), getSimpleName());
		}
		return new OutputClass(asType(), getPackageName(), getSimpleName());
	}
}