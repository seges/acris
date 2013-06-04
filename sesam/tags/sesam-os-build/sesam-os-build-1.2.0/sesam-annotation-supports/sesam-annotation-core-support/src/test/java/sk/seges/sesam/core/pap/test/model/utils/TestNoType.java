package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVisitor;

abstract class TestNoType extends TestTypeMirror implements NoType {

	TestNoType(TypeKind kind) {
		super(kind);
	}

	@Override
	public <R, P> R accept(TypeVisitor<R, P> v, P p) {
		return v.visitNoType(this, p);
	}

}
