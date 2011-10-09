package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.type.NullType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVisitor;

public class TestNullType extends TestTypeMirror implements NullType {

	public TestNullType() {
		super(TypeKind.NULL);
	}

	@Override
	public <R, P> R accept(TypeVisitor<R, P> v, P p) {
		return v.visitNull(this, p);
	}

}
