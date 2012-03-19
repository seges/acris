package sk.seges.sesam.core.pap.test.model.utils;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;

public class TestExecutableType extends TestTypeMirror implements ExecutableType {

	private ExecutableElement element;
	private List<TypeMirror> typeParameters = null;
	
	TestExecutableType(ExecutableElement element) {
		super(TypeKind.EXECUTABLE);
	}

	@Override
	public <R, P> R accept(TypeVisitor<R, P> v, P p) {
		return v.visitExecutable(this, p);
	}

	@Override
	public List<? extends TypeVariable> getTypeVariables() {
		return null;
	}

	@Override
	public TypeMirror getReturnType() {
		return element.getReturnType();
	}

	@Override
	public List<? extends TypeMirror> getParameterTypes() {
		if (typeParameters != null) {
			return typeParameters;
		}
		
		typeParameters = new ArrayList<TypeMirror>();
		
		for (TypeParameterElement typeParameter: element.getTypeParameters()) {
			typeParameters.add(typeParameter.asType());
		}
		
		return this.typeParameters;
	}

	@Override
	public List<? extends TypeMirror> getThrownTypes() {
		return null;
	}
}