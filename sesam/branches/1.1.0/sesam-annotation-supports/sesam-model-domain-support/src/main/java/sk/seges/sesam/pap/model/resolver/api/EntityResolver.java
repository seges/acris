package sk.seges.sesam.pap.model.resolver.api;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

public interface EntityResolver {

	TypeMirror getTargetEntityType(Element element);

}