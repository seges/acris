package sk.seges.sesam.core.pap.model;


import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public class InputClass extends AbstractPrintableType implements NamedType, MutableType {

	private String simpleClassName;
	private String packageName;
	private NamedType enclosedClass;
	private Set<AnnotationMirror> annotations = new HashSet<AnnotationMirror>();

	private TypeMirror type;

	public InputClass(String packageName, String simpleClassName) {
		this(null, packageName, simpleClassName);
	}

	public InputClass(TypeMirror type, String packageName, String simpleClassName) {
		this.simpleClassName = simpleClassName;
		this.packageName = packageName;
		setType(type);
	}

	public InputClass(TypeMirror type, NamedType enclosedClass, String simpleClassName) {
		this.simpleClassName = simpleClassName;
		this.packageName = enclosedClass.getPackageName();
		this.enclosedClass = enclosedClass;
		setType(type);
	}

	private void setType(TypeMirror type) {
		if (type != null && type.getKind().equals(TypeKind.DECLARED)) {
			for (AnnotationMirror annotation: ((DeclaredType)type).asElement().getAnnotationMirrors()) {
				annotations.add(annotation);
			}
		}
		this.type = type;		
	}
	
	public NamedType getEnclosedClass() {
		return enclosedClass;
	}
	
	@Override
	protected OutputClass clone() {
		OutputClass clone = new OutputClass(asType(), packageName, simpleClassName);
		for (AnnotationMirror annotation: annotations) {
			clone.annotateWith(annotation);
		}
		return clone;
	}

	public String getClassName() {
		return simpleClassName;
	}

	public String getPackageName() {
		return packageName;
	}

	public HasTypeParameters addType(TypeParameter typeParameter) {
		return new TypedOutputClass(asType(), getPackageName(), getClassName(), typeParameter);
	}

	public MutableType addClassSufix(String sufix) {
		InputClass result = clone();
		result.simpleClassName += sufix;
		return result;
	}

	public MutableType addClassPrefix(String prefix) {
		InputClass result = clone();
		result.simpleClassName = prefix + simpleClassName;
		return result;
	}

	public MutableType addPackageSufix(String sufix) {
		InputClass result = clone();
		result.packageName += sufix;
		return result;
	}

	public MutableType changePackage(String packageName) {
		InputClass result = clone();
		result.packageName = packageName;
		return result;
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
		if (!(obj instanceof InputClass)) {
			return false;
		}
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
		InputClass result = clone();
		result.simpleClassName = name;
		result.type = null;
		return result;
	}
	
	@Override
	public TypeMirror asType() {
		return type;
	}

	@Override
	public MutableType changePackage(PackageValidator packageValidator) {
		packageName = packageValidator.toString();
		return this;
	}
	
	public void annotateWith(AnnotationMirror annotation) {
		annotations.add(annotation);
	}
	
	public Set<AnnotationMirror> getAnnotations() {
		return annotations;
	}
}