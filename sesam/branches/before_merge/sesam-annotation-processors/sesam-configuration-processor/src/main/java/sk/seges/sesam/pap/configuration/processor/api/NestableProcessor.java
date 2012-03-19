package sk.seges.sesam.pap.configuration.processor.api;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public interface NestableProcessor {

	public void processAnnotation(TypeElement typeElement, MutableDeclaredType outputName, FormattedPrintWriter pw);
}
