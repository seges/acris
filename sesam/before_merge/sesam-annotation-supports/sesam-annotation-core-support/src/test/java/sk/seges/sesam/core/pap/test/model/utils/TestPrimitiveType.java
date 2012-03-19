package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVisitor;

import sk.seges.sesam.core.pap.utils.PAPReflectionUtils;

public class TestPrimitiveType extends TestTypeMirror implements PrimitiveType {

	
	public TestPrimitiveType(Class<?> clazz) {
		super(PAPReflectionUtils.toPrimitive(clazz));
	}
	
	public TestPrimitiveType(TypeKind kind) {
		super(kind);
	}

	@Override
	public <R, P> R accept(TypeVisitor<R, P> v, P p) {
		return v.visitPrimitive(this, p);
	}

}
