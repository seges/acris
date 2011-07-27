package sk.seges.sesam.core.pap.model;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.NamedType;

public class OutputClass extends InputClass {

	public OutputClass(String packageName, String simpleClassName) {
		super(packageName, simpleClassName);
	}

	public OutputClass(TypeMirror type, String packageName, String simpleClassName) {
		super(type, packageName, simpleClassName);
	}

	public OutputClass(TypeMirror type, NamedType enclosedClass, String simpleClassName) {
		super(type, enclosedClass, simpleClassName);
	}

	@Override
	protected OutputClass clone() {
		return new OutputClass(asType(), getPackageName(), getClassName());
	}
}