package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

abstract class TestTypeMirror implements TypeMirror {

	protected TypeKind kind;

	public TestTypeMirror(TypeKind kind) {
		this.kind = kind;
	}

	@Override
	public TypeKind getKind() {
		return kind;
	}
}