/**
 * 
 */
package com.google.gwt.dev.javac.typemodel;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;


/**
 * Holding anything needed, e.g. methods not created by GWT but yourself.
 * 
 * @author eldzi
 */
public class JDummyClassType extends JClassType {

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#acceptSubtype(com.google.gwt.core.ext.typeinfo.JClassType)
	 */
	@Override
	protected void acceptSubtype(JClassType me) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#addConstructor(com.google.gwt.core.ext.typeinfo.JConstructor)
	 */
	@Override
	void addConstructor(JConstructor ctor) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#addField(com.google.gwt.core.ext.typeinfo.JField)
	 */
	@Override
	void addField(JField field) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#addImplementedInterface(com.google.gwt.core.ext.typeinfo.JClassType)
	 */
	@Override
	public void addImplementedInterface(JClassType intf) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#addMethod(com.google.gwt.core.ext.typeinfo.JMethod)
	 */
	@Override
	void addMethod(JMethod method) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#addModifierBits(int)
	 */
	@Override
	public void addModifierBits(int bits) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#addNestedType(com.google.gwt.core.ext.typeinfo.JClassType)
	 */
	@Override
	void addNestedType(JClassType type) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#findConstructor(com.google.gwt.core.ext.typeinfo.JType[])
	 */
	@Override
	public JConstructor findConstructor(JType[] paramTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#findField(java.lang.String)
	 */
	@Override
	public JField findField(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#findMethod(java.lang.String, com.google.gwt.core.ext.typeinfo.JType[])
	 */
	@Override
	public JMethod findMethod(String name, JType[] paramTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#findNestedType(java.lang.String)
	 */
	@Override
	public JClassType findNestedType(String typeName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#findNestedTypeImpl(java.lang.String[], int)
	 */
	@Override
	JClassType findNestedTypeImpl(String[] typeName, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getAnnotation(java.lang.Class)
	 */
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getAnnotations()
	 */
	@Override
	public Annotation[] getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getConstructor(com.google.gwt.core.ext.typeinfo.JType[])
	 */
	@Override
	public JConstructor getConstructor(JType[] paramTypes) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getConstructors()
	 */
	@Override
	public JConstructor[] getConstructors() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getDeclaredAnnotations()
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getEnclosingType()
	 */
	@Override
	public JClassType getEnclosingType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getErasedType()
	 */
	@Override
	public JClassType getErasedType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getField(java.lang.String)
	 */
	@Override
	public JField getField(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getFields()
	 */
	@Override
	public JField[] getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getImplementedInterfaces()
	 */
	@Override
	public JClassType[] getImplementedInterfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getMethod(java.lang.String, com.google.gwt.core.ext.typeinfo.JType[])
	 */
	@Override
	public JMethod getMethod(String name, JType[] paramTypes) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getMethods()
	 */
	@Override
	public JMethod[] getMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getModifierBits()
	 */
	@Override
	protected int getModifierBits() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getNestedType(java.lang.String)
	 */
	@Override
	public JClassType getNestedType(String typeName) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getNestedTypes()
	 */
	@Override
	public JClassType[] getNestedTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getOracle()
	 */
	@Override
	public TypeOracle getOracle() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getOverloads(java.lang.String)
	 */
	@Override
	public JMethod[] getOverloads(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getOverridableMethods()
	 */
	@Override
	public JMethod[] getOverridableMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getPackage()
	 */
	@Override
	public JPackage getPackage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getSubstitutedType(com.google.gwt.core.ext.typeinfo.JParameterizedType)
	 */
	@Override
	JClassType getSubstitutedType(JParameterizedType parameterizedType) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getSubtypes()
	 */
	@Override
	public JClassType[] getSubtypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#getSuperclass()
	 */
	@Override
	public JClassType getSuperclass() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isAbstract()
	 */
	@Override
	public boolean isAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isAnnotationPresent(java.lang.Class)
	 */
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isDefaultInstantiable()
	 */
	@Override
	public boolean isDefaultInstantiable() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isFinal()
	 */
	@Override
	public boolean isFinal() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isGenericType()
	 */
	@Override
	public JGenericType isGenericType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isInterface()
	 */
	@Override
	public JClassType isInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isMemberType()
	 */
	@Override
	public boolean isMemberType() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isPrivate()
	 */
	@Override
	public boolean isPrivate() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isProtected()
	 */
	@Override
	public boolean isProtected() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isPublic()
	 */
	@Override
	public boolean isPublic() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#isStatic()
	 */
	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#notifySuperTypes()
	 */
	@Override
	void notifySuperTypes() {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#notifySuperTypesOf(com.google.gwt.core.ext.typeinfo.JClassType)
	 */
	@Override
	protected void notifySuperTypesOf(JClassType me) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#removeFromSupertypes()
	 */
	@Override
	void removeFromSupertypes() {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#removeSubtype(com.google.gwt.core.ext.typeinfo.JClassType)
	 */
	@Override
	protected void removeSubtype(JClassType me) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JClassType#setSuperclass(com.google.gwt.core.ext.typeinfo.JClassType)
	 */
	@Override
	public void setSuperclass(JClassType type) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#getJNISignature()
	 */
	@Override
	public String getJNISignature() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#getQualifiedSourceName()
	 */
	@Override
	public String getQualifiedSourceName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#getSimpleSourceName()
	 */
	@Override
	public String getSimpleSourceName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#isArray()
	 */
	@Override
	public JArrayType isArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#isClass()
	 */
	@Override
	public JClassType isClass() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#isEnum()
	 */
	@Override
	public JEnumType isEnum() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#isParameterized()
	 */
	@Override
	public JParameterizedType isParameterized() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#isPrimitive()
	 */
	@Override
	public JPrimitiveType isPrimitive() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#isRawType()
	 */
	@Override
	public JRawType isRawType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.typeinfo.JType#isWildcard()
	 */
	@Override
	public JWildcardType isWildcard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQualifiedBinaryName() {
		return null;
	}

	@Override
	public JMethod[] getInheritableMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void getInheritableMethodsOnSuperclassesAndThisClass(Map<String, JMethod> methodsBySignature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getInheritableMethodsOnSuperinterfacesAndMaybeThisInterface(Map<String, JMethod> methodsBySignature) {
		// TODO Auto-generated method stub
		
	}

}
