package sk.seges.sesam.core.pap.model;

import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public class DelegateImmutableType implements HasTypeParameters {

	private ImmutableType delegateImmutableType;
	
	protected DelegateImmutableType() {}

	protected void setDelegateImmutableType(ImmutableType delegateImmutableType) {
		this.delegateImmutableType = delegateImmutableType;
	}
	
	protected ImmutableType getDelegateImmutableType() {
		return delegateImmutableType;
	}
	
	@Override
	public String getPackageName() {
		return delegateImmutableType.getPackageName();
	}

	@Override
	public String getSimpleName() {
		return delegateImmutableType.getSimpleName();
	}

	@Override
	public String getCanonicalName() {
		return delegateImmutableType.getCanonicalName();
	}

	@Override
	public String getQualifiedName() {
		return delegateImmutableType.getQualifiedName();
	}

	@Override
	public TypeMirror asType() {
		return delegateImmutableType.asType();
	}

	@Override
	public String toString(ClassSerializer serializer) {
		return delegateImmutableType.toString(serializer);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return delegateImmutableType.toString(serializer, typed);
	}

	@Override
	public void annotateWith(AnnotationMirror annotationMirror) {
		delegateImmutableType.annotateWith(annotationMirror);
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		return delegateImmutableType.getAnnotations();
	}

	@Override
	public HasTypeParameters addType(TypeParameter typeParameter) {
		return delegateImmutableType.addType(typeParameter);
	}

	@Override
	public ImmutableType setName(String name) {
		return delegateImmutableType.setName(name);
	}

	@Override
	public ImmutableType addClassSufix(String sufix) {
		return delegateImmutableType.addClassSufix(sufix);
	}

	@Override
	public ImmutableType addClassPrefix(String prefix) {
		return delegateImmutableType.addClassPrefix(prefix);
	}

	@Override
	public ImmutableType addPackageSufix(String sufix) {
		return delegateImmutableType.addPackageSufix(sufix);
	}

	@Override
	public ImmutableType changePackage(String packageName) {
		return delegateImmutableType.changePackage(packageName);
	}

	@Override
	public ImmutableType changePackage(PackageValidator packageValidator) {
		return delegateImmutableType.changePackage(packageValidator);
	}

	@Override
	public TypeParameter[] getTypeParameters() {
		if (delegateImmutableType instanceof HasTypeParameters) {
			return ((HasTypeParameters)delegateImmutableType).getTypeParameters();
		}
		return null;
	}

	@Override
	public ImmutableType stripTypeParameters() {
		if (delegateImmutableType instanceof HasTypeParameters) {
			return ((HasTypeParameters)delegateImmutableType).stripTypeParameters();
		}
		return delegateImmutableType;
	}
	
	@Override
	public String toString() {
		return delegateImmutableType.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delegateImmutableType == null) ? 0 : delegateImmutableType.hashCode());
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
		DelegateImmutableType other = (DelegateImmutableType) obj;
		if (delegateImmutableType == null) {
			if (other.delegateImmutableType != null)
				return false;
		} else if (!delegateImmutableType.equals(other.delegateImmutableType))
			return false;
		return true;
	}
	
	
}