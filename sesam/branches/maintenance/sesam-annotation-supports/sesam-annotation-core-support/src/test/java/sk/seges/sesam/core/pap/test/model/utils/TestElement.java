package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

abstract class TestElement implements Element {

	private ElementKind kind;
	
	protected TestElement(ElementKind kind) {
		this.kind = kind;
	}
	
	@Override
	public ElementKind getKind() {
		return kind;
	}
}