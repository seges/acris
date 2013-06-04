package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class ConverterParameterElement extends ParameterElement {

	public ConverterParameterElement(MutableTypeMirror type, String name, boolean isPropagated) {
		super(type, name, isPropagated);
	}

	@Override
	public boolean isConverter() {
		return true;
	}
}
