package sk.seges.sesam.core.pap.model;

import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public abstract class DelegateImmutableType implements HasTypeParameters {

	private ImmutableType delegateImmutableType;
	
	protected DelegateImmutableType() {}

	protected void setDelegateImmutableType(ImmutableType delegateImmutableType) {
		this.delegateImmutableType = delegateImmutableType;
	}
	
	public ImmutableType ensureDelegateType() {
		if (delegateImmutableType == null) {
			delegateImmutableType = getDelegateImmutableType();
		}
		return delegateImmutableType;
	}

	abstract protected ImmutableType getDelegateImmutableType();

	@Override
	public String getPackageName() {
		return ensureDelegateType().getPackageName();
	}

	@Override
	public String getSimpleName() {
		return ensureDelegateType().getSimpleName();
	}

	@Override
	public String getCanonicalName() {
		return ensureDelegateType().getCanonicalName();
	}

	@Override
	public String getQualifiedName() {
		return ensureDelegateType().getQualifiedName();
	}

	@Override
	public TypeMirror asType() {
		return ensureDelegateType().asType();
	}

	@Override
	public String toString(ClassSerializer serializer) {
		return ensureDelegateType().toString(serializer);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return ensureDelegateType().toString(serializer, typed);
	}

	@Override
	public void annotateWith(AnnotationMirror annotationMirror) {
		ensureDelegateType().annotateWith(annotationMirror);
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		return ensureDelegateType().getAnnotations();
	}

	@Override
	public HasTypeParameters addType(TypeParameter typeParameter) {
		return ensureDelegateType().addType(typeParameter);
	}

	@Override
	public ImmutableType setName(String name) {
		return ensureDelegateType().setName(name);
	}

	@Override
	public ImmutableType addClassSufix(String sufix) {
		return ensureDelegateType().addClassSufix(sufix);
	}

	@Override
	public ImmutableType addClassPrefix(String prefix) {
		return ensureDelegateType().addClassPrefix(prefix);
	}

	@Override
	public ImmutableType addPackageSufix(String sufix) {
		return ensureDelegateType().addPackageSufix(sufix);
	}

	@Override
	public ImmutableType changePackage(String packageName) {
		return ensureDelegateType().changePackage(packageName);
	}

	@Override
	public ImmutableType changePackage(PackageValidator packageValidator) {
		return ensureDelegateType().changePackage(packageValidator);
	}

	@Override
	public TypeParameter[] getTypeParameters() {
		if (ensureDelegateType() instanceof HasTypeParameters) {
			return ((HasTypeParameters)ensureDelegateType()).getTypeParameters();
		}
		return null;
	}

	@Override
	public ImmutableType stripTypeParameters() {
		if (ensureDelegateType() instanceof HasTypeParameters) {
			return ((HasTypeParameters)ensureDelegateType()).stripTypeParameters();
		}
		return ensureDelegateType();
	}
	
	@Override
	public String toString() {
		return ensureDelegateType().toString();
	}

	@Override
	public int hashCode() {
		return ensureDelegateType().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DelegateImmutableType) {
			return ensureDelegateType().equals(((DelegateImmutableType)obj).ensureDelegateType());
		}
		return ensureDelegateType().equals(obj);
	}
	
	@Override
	public ImmutableType setEnclosedClass(NamedType type) {
		return ensureDelegateType().setEnclosedClass(type);
	}
	
	@Override
	public NamedType getEnclosedClass() {
		return ensureDelegateType().getEnclosedClass();
	}
	
	@Override
	public ImmutableType removeClassSuffix(String originalSuffix) {
		return ensureDelegateType().removeClassSuffix(originalSuffix);
	}

	@Override
	public ImmutableType removeClassPrefix(String originalPrefix) {
		return ensureDelegateType().removeClassPrefix(originalPrefix);
	}

	@Override
	public ImmutableType replaceClassSuffix(String originalSuffix, String newSuffix) {
		return ensureDelegateType().replaceClassSuffix(originalSuffix, newSuffix);
	}
	
	@Override
	public ImmutableType replaceClassPrefix(String originalPrefix, String newPrefix) {
		return ensureDelegateType().replaceClassPrefix(originalPrefix, newPrefix);
	}
}