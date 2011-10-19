package sk.seges.sesam.core.pap.model.mutable.delegate;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public abstract class DelegateMutableDeclaredType extends DelegateMutableType implements MutableDeclaredType {

	protected void setDelegate(MutableDeclaredType delegateType) {
		super.setDelegate(delegateType);
	}
	
	public MutableDeclaredType ensureDelegateType() {
		return (MutableDeclaredType) super.ensureDelegateType();
	}

	abstract protected MutableDeclaredType getDelegate();

	@Override
	public void annotateWith(AnnotationMirror annotationMirror) {
		ensureDelegateType().annotateWith(annotationMirror);
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		return ensureDelegateType().getAnnotations();
	}

	@Override
	public MutableDeclaredType getEnclosedClass() {
		return ensureDelegateType().getEnclosedClass();
	}

	@Override
	public MutableDeclaredType setEnclosedClass(MutableDeclaredType type) {
		return ensureDelegateType().setEnclosedClass(type);
	}

	@Override
	public MutableDeclaredType setSimpleName(String simpleName) {
		return ensureDelegateType().setSimpleName(simpleName);
	}

	@Override
	public String getSimpleName() {
		return ensureDelegateType().getSimpleName();
	}

	@Override
	public String getPackageName() {
		return ensureDelegateType().getPackageName();
	}

	@Override
	public List<? extends MutableTypeVariable> getTypeVariables() {
		return ensureDelegateType().getTypeVariables();
	}

	@Override
	public MutableDeclaredType cloneTypeVariables(MutableDeclaredType declaredType) {
		return ensureDelegateType().cloneTypeVariables(declaredType);
	}

	@Override
	public MutableDeclaredType setTypeVariables(MutableTypeVariable... mutableTypeVariables) {
		return ensureDelegateType().setTypeVariables(mutableTypeVariables);
	}

	@Override
	public MutableDeclaredType addTypeVariable(MutableTypeVariable typeVariable) {
		return ensureDelegateType().addTypeVariable(typeVariable);
	}

	@Override
	public MutableDeclaredType addClassSufix(String sufix) {
		return ensureDelegateType().addClassSufix(sufix);
	}

	@Override
	public MutableDeclaredType addClassPrefix(String prefix) {
		return ensureDelegateType().addClassPrefix(prefix);
	}

	@Override
	public MutableDeclaredType addPackageSufix(String sufix) {
		return ensureDelegateType().addPackageSufix(sufix);
	}

	@Override
	public MutableDeclaredType changePackage(String packageName) {
		return ensureDelegateType().changePackage(packageName);
	}

	@Override
	public MutableDeclaredType changePackage(PackageValidator packageValidator) {
		return ensureDelegateType().changePackage(packageValidator);
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
	public MutableDeclaredType setKind(MutableTypeKind kind) {
		return ensureDelegateType().setKind(kind);
	}
	
	@Override
	public Set<? extends MutableTypeMirror> getInterfaces() {
		return ensureDelegateType().getInterfaces();
	}

	@Override
	public MutableDeclaredType setInterfaces(Set<? extends MutableTypeMirror> interfaces) {
		return ensureDelegateType().setInterfaces(interfaces);
	}

	@Override
	public MutableDeclaredType getSuperClass() {
		return ensureDelegateType().getSuperClass();
	}

	@Override
	public MutableDeclaredType setSuperClass(MutableDeclaredType superClass) {
		return ensureDelegateType().setSuperClass(superClass);
	}

	@Override
	public MutableDeclaredType stripTypeParameters() {
		return ensureDelegateType().stripTypeParameters();
	}

	@Override
	public MutableDeclaredType prefixTypeParameter(String prefix) {
		return ensureDelegateType().prefixTypeParameter(prefix);
	}

	@Override
	public MutableDeclaredType renameTypeParameter(RenameActionType actionType, String parameter) {
		return ensureDelegateType().renameTypeParameter(actionType, parameter);
	}

	@Override
	public MutableDeclaredType renameTypeParameter(RenameActionType actionType, String parameter, String oldName, boolean recursive) {
		return ensureDelegateType().renameTypeParameter(actionType, parameter, oldName, recursive);
	}
	
	@Override
	public MutableDeclaredType clone() {
		return ensureDelegateType().clone();
	}
	
	@Override
	public boolean hasVariableParameterTypes() {
		return ensureDelegateType().hasVariableParameterTypes();
	}
	
	@Override
	public boolean hasTypeParameters() {
		return ensureDelegateType().hasTypeParameters();
	}
	
	@Override
	public MutableTypeVariable[] getVariableParameterTypes() {
		return ensureDelegateType().getVariableParameterTypes();
	}
	
	@Override
	public MutableDeclaredType stripTypeParametersTypes() {
		return ensureDelegateType().stripTypeParametersTypes();
	}
	
	@Override
	public MutableDeclaredType replaceClassSuffix(String oldSuffix, String newSuffix) {
		return ensureDelegateType().replaceClassSuffix(oldSuffix, newSuffix);
	}
	
	@Override
	public MutableDeclaredType replaceClassPrefix(String oldPrefix, String newPrefix) {
		return ensureDelegateType().replaceClassPrefix(oldPrefix, newPrefix);
	}

	@Override
	public MutableDeclaredType removeClassSuffix(String originalSuffix) {
		return ensureDelegateType().removeClassSuffix(originalSuffix);
	}
	
	@Override
	public MutableDeclaredType removeClassPrefix(String originalPrefix) {
		return ensureDelegateType().removeClassPrefix(originalPrefix);
	}
	
	@Override
	public MutableDeclaredType stripVariableTypeVariables() {
		return ensureDelegateType().stripVariableTypeVariables();
	}
}