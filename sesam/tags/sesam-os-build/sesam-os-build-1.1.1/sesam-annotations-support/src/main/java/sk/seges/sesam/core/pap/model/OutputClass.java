package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.api.NamedType;

public class OutputClass extends InputClass {

	public OutputClass(String packageName, String simpleClassName) {
		super(packageName, simpleClassName);
	}

	public OutputClass(NamedType enclosedClass, String simpleClassName) {
		super(enclosedClass, simpleClassName);
	}

	@Override
	protected OutputClass clone() {
		return new OutputClass(getPackageName(), getClassName());
	}
}