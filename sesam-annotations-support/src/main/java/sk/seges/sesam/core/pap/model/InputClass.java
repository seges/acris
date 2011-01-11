package sk.seges.sesam.core.pap.model;

import java.util.Arrays;

import sk.seges.sesam.core.pap.model.TypedClass.TypeParameter;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

public class InputClass implements NamedType, MutableType {

	public static interface HasTypeParameters extends NamedType {
		TypeParameter[] getTypeParameters();
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

	public static class TypedOutputClass extends OutputClass implements HasTypeParameters {
		
		private TypeParameter[] typeParameters;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Arrays.hashCode(typeParameters);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			TypedOutputClass other = (TypedOutputClass) obj;
			if (!Arrays.equals(typeParameters, other.typeParameters))
				return false;
			return true;
		}

		public TypedOutputClass(String packageName, String className, TypeParameter... typeParameters) {
			super(packageName, className);
			this.typeParameters = typeParameters;
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
		
		@Override
		public String toString() {
			return super.toString() + TypedUtils.toString(this);
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
		if (getClass() != obj.getClass())
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