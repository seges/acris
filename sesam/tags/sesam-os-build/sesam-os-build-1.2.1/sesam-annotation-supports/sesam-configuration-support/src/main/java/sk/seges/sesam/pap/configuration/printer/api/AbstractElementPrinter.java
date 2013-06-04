package sk.seges.sesam.pap.configuration.printer.api;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.configuration.model.parameter.ParameterContext;

public interface AbstractElementPrinter<T extends ParameterContext> {

	void initialize(TypeElement type, MutableDeclaredType outputName);

	void print(T context);

	void finish(TypeElement type);

}
