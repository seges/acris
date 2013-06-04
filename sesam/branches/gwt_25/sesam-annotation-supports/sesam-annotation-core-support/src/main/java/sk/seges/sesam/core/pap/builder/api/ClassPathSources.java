package sk.seges.sesam.core.pap.builder.api;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.Source;

public interface ClassPathSources {

	Source getElementSourceFile(TypeElement a);

}