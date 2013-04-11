package sk.seges.sesam.core.pap.model.api;

import javax.lang.model.element.ExecutableElement;

public interface Source {

	String getMethodBody(ExecutableElement element);
}
