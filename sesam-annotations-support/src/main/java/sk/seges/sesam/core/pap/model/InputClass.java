package sk.seges.sesam.core.pap.model;


import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class InputClass extends AbstractPrintableType implements NamedType, MutableType {

	private String simpleClassName;
	private String packageName;
	private NamedType enclosedClass;
	
	public InputClass(String packageName, String simpleClassName) {
		this.simpleClassName = simpleClassName;
		this.packageName = packageName;
	}

	public InputClass(NamedType enclosedClass, String simpleClassName) {
		this.simpleClassName = simpleClassName;
		this.packageName = enclosedClass.getPackageName();
		this.enclosedClass = enclosedClass;
	}

	public NamedType getEnclosedClass() {
		return enclosedClass;
	}
	
	@Override
	protected MutableType clone() {
		return new OutputClass(packageName, simpleClassName);
	}

	public String getClassName() {
		return simpleClassName;
	}

	public String getPackageName() {
		return packageName;
	}

	public HasTypeParameters addType(TypeParameter typeParameter) {
		return new TypedOutputClass(getPackageName(), getClassName(), typeParameter);
	}

	public MutableType addClassSufix(String sufix) {
		simpleClassName += sufix;
		return clone();
	}

	public MutableType addClassPrefix(String prefix) {
		simpleClassName = prefix + simpleClassName;
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
		if (enclosedClass != null) {
			return enclosedClass.getCanonicalName() + "." + simpleClassName;
		}
		return (packageName != null ? (packageName + ".") : "") + simpleClassName;
	}

	@Override
	public String toString() {
		return getCanonicalName();
	}

	public String getSimpleName() {
		return simpleClassName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enclosedClass == null) ? 0 : enclosedClass.hashCode());
		result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result + ((simpleClassName == null) ? 0 : simpleClassName.hashCode());
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
		if (enclosedClass == null) {
			if (other.enclosedClass != null)
				return false;
		} else if (!enclosedClass.equals(other.enclosedClass))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (simpleClassName == null) {
			if (other.simpleClassName != null)
				return false;
		} else if (!simpleClassName.equals(other.simpleClassName))
			return false;
		return true;
	}

	@Override
	public String getQualifiedName() {
		return getCanonicalName().replace("$", ".");
	}

	@Override
	public MutableType setName(String name) {
		simpleClassName = name;
		return this;
	}
}