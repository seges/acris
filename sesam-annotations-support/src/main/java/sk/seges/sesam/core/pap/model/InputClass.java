package sk.seges.sesam.core.pap.model;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.ClassUtils;

public class InputClass extends AbstractPrintableType implements NamedType, MutableType {

	public interface TypeParameter {

		public static final String UNDEFINED = "?";
		
		String getVariable();

		Type getBounds();
		
		String toString(NamedType type, ClassSerializer serializer);
	}
	
	public static interface HasTypeParameters extends NamedType {
		TypeParameter[] getTypeParameters();
		
		String toString(NamedType inputClass, ClassSerializer serializer, boolean typed);
	}

	public static class OutputClass extends InputClass {

		public OutputClass(String packageName, String className) {
			super(packageName, className);
		}

		@Override
		protected OutputClass clone() {
			return new OutputClass(getPackageName(), getClassName());
		}
	}

	public static class TypeParameterBuilder {
		public static TypeParameter get(String variable, Type bounds) {
			return new TypeParameterClass(variable, bounds);
		}

		public static TypeParameter get(String variable) {
			return new TypeParameterClass(variable);
		}

		public static TypeParameter get(Type bounds) {
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
		
		public String toString(NamedType inputClass, ClassSerializer serializer) {
			String result = "";
			
			if (getVariable() != null) {
				result += getVariable() + " ";
			}
			
			if (getBounds() != null) {
				if (getVariable() != null) {
					result += "extends ";
				}
				if (getBounds().equals(NamedType.THIS)) {
					result += inputClass.toString(ClassSerializer.SIMPLE);
				} else {
					result += ClassUtils.toString(inputClass, getBounds(), serializer, true);
				}
			}
			
			if (result.length() == 0) {
				throw new IllegalArgumentException("Invalid type parameter");
			}
			return result;
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

	public static class TypedClassBuilder {
		public static HasTypeParameters get(String packageName, String className, TypeParameter... typeParameters) {
			return new TypedOutputClass(packageName, className, typeParameters);
		}

		public static HasTypeParameters get(Class<?> clazz, Class<?>... classes) {
			return new TypedOutputClass(clazz, classes);
		}

		public static HasTypeParameters get(Class<?> clazz, NamedType... classes) {
			return new TypedOutputClass(clazz, classes);
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
	
	static class TypedOutputClass extends OutputClass implements HasTypeParameters {

		private TypeParameter[] typeParameters;

		public TypedOutputClass(String packageName, String className, TypeParameter... typeParameters) {
			super(packageName, className);
			this.typeParameters = typeParameters;
		}

		public TypedOutputClass(Class<?> clazz, Class<?>... classes) {
			this(clazz.getPackage().getName(), clazz.getSimpleName());
			if (classes != null) {
				typeParameters = new TypeParameter[classes.length];
				for (int i = 0; i < classes.length; i++) {
					typeParameters[i] = new TypeParameterClass(classes[i]);
				}
			}
		}

		public TypedOutputClass(Class<?> clazz, NamedType... classes) {
			this(clazz.getPackage().getName(), clazz.getSimpleName());
			if (classes != null) {
				typeParameters = new TypeParameter[classes.length];
				for (int i = 0; i < classes.length; i++) {
					typeParameters[i] = new TypeParameterClass(classes[i]);
				}
			}
		}

		public TypedOutputClass(Class<?> clazz, TypeParameter... typeParameters) {
			this(clazz.getPackage().getName(), clazz.getSimpleName());
			this.typeParameters = typeParameters;
		}

		public TypedOutputClass(NamedType type, TypeParameter... typeParameters) {
			this(type.getPackageName(), type.getSimpleName());
			this.typeParameters = typeParameters;
		}

		public TypedOutputClass(NamedType type, NamedType... classes) {
			this(type.getPackageName(), type.getSimpleName());
			if (classes != null) {
				typeParameters = new TypeParameter[classes.length];
				for (int i = 0; i < classes.length; i++) {
					typeParameters[i] = new TypeParameterClass(classes[i]);
				}
			}
		}

		public String toString(NamedType inputClass, ClassSerializer serializer, boolean typed) {

			String resultName = this.toString(serializer);

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
			return new TypedOutputClass(getPackageName(), getClassName(), typeParameters);
		}

		public TypedOutputClass addType(TypeParameter typeParameter) {
			TypeParameter[] params = new TypeParameter[typeParameters.length + 1];
			for (int i = 0; i < typeParameters.length; i++) {
				params[i] = typeParameters[i];
			}
			params[typeParameters.length] = typeParameter;

			return new TypedOutputClass(getPackageName(), getClassName(), params);
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
	}

	private String className;
	private String packageName;

	public InputClass(String packageName, String className) {
		this.className = className;
		this.packageName = packageName;
	}

	@Override
	protected MutableType clone() {
		return new OutputClass(packageName, className);
	}

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public HasTypeParameters addType(TypeParameter typeParameter) {
		return new TypedOutputClass(getPackageName(), getClassName(), typeParameter);
	}

	public MutableType addClassSufix(String sufix) {
		className += sufix;
		return clone();
	}

	public MutableType addClassPrefix(String prefix) {
		className = prefix + className;
		return clone();
	}

	public MutableType addPackageSufix(String sufix) {
		packageName += sufix;
		return clone();
	}

	public MutableType changePackage(String packageName) {
		this.packageName = packageName;
		return clone();
	}

	public String getCanonicalName() {
		return (packageName != null ? (packageName + ".") : "") + className;
	}

	@Override
	public String toString() {
		return getCanonicalName();
	}

	public String getSimpleName() {
		return className;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isAssignableFrom(obj.getClass()))
			return false;
		InputClass other = (InputClass) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		return true;
	}

	@Override
	public String getQualifiedName() {
		return getCanonicalName().replace("$", ".");
	}
}