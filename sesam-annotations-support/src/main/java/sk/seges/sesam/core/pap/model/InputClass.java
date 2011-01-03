package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.TypedClass.TypeParameter;
import sk.seges.sesam.core.pap.model.api.Type;

public class InputClass implements Type {

	public static interface HasTypeParameters extends Type {
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
	protected OutputClass clone() {
		return new OutputClass(packageName, className);
	}

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public TypedOutputClass addType(TypeParameter typeParameter) {
		return new TypedOutputClass(getPackageName(), getClassName(), typeParameter);
	}
	
	public OutputClass addClassSufix(String sufix) {
		className += sufix;
		return clone();
	}

	public OutputClass addClassPrefix(String prefix) {
		className = prefix + className;
		return clone();
	}

	public OutputClass addPackageSufix(String sufix) {
		packageName += sufix;
		return clone();
	}

	public OutputClass changePackage(String packageName) {
		this.packageName = packageName;
		return clone();
	}

	public String getCanonicalName() {
		return packageName + "." + className;
	}
	
	@Override
	public String toString() {
		return getCanonicalName();
	}
	
	public String getSimpleName() {
		return className;
	}
}