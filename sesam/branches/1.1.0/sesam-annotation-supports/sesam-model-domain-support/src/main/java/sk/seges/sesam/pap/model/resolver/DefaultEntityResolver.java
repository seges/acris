package sk.seges.sesam.pap.model.resolver;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class DefaultEntityResolver implements EntityResolver {

	@Override
	public TypeMirror getTargetEntityType(Element element) {
		switch (element.getKind()) {
		case METHOD:
			return ((ExecutableElement)element).getReturnType();
		}
		return element.asType();
	}
}