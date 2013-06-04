package sk.seges.sesam.pap.configuration.model;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.AbstractElementPrinter;

public abstract class AbstractParameterHandler {

	public abstract boolean handle(AbstractElementPrinter<SettingsContext> printer);

	//We have to use elements instead of types, because types does not 
	//reflect enclosing elements correctly
	protected Element getEnclosingElement(Element element) {
		Element enclosingElement = element.getEnclosingElement();
		while (enclosingElement.getKind().equals(ElementKind.ANNOTATION_TYPE)) {
			element = enclosingElement;
			enclosingElement = element.getEnclosingElement();
		}
		
		return element;
	}
	

}