package sk.seges.sesam.core.pap.test.model.utils;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Types;

public class TestTypes implements Types {

	@Override
	public Element asElement(TypeMirror t) {
		return null;
	}

	@Override
	public boolean isSameType(TypeMirror t1, TypeMirror t2) {
		return false;
	}

	@Override
	public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(TypeMirror t1, TypeMirror t2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<? extends TypeMirror> directSupertypes(TypeMirror t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeMirror erasure(TypeMirror t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeElement boxedClass(PrimitiveType p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrimitiveType unboxedType(TypeMirror t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeMirror capture(TypeMirror t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrimitiveType getPrimitiveType(TypeKind kind) {
		return new TestPrimitiveType(kind);
	}

	@Override
	public NullType getNullType() {
		return new TestNullType();
	}

	@Override
	public NoType getNoType(TypeKind kind) {
		return new TestNoType(kind) {};
	}

	@Override
	public ArrayType getArrayType(TypeMirror componentType) {
		return new TestArrayType(componentType);
	}

	@Override
	public WildcardType getWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
		return new TestWildcardType(extendsBound, superBound);
	}

	@Override
	public DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
		return new TestDeclaredType(typeElem, typeArgs);
	}

	@Override
	public DeclaredType getDeclaredType(DeclaredType containing, TypeElement typeElem, TypeMirror... typeArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeMirror asMemberOf(DeclaredType containing, Element element) {
		// TODO Auto-generated method stub
		return null;
	}

}
