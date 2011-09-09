package sk.seges.sesam.pap.configuration.printer.api;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;

public interface ElementPrinter {

	void initialize(TypeElement type, NamedType outputName);

	void print(ProcessorContext context);

	void finish(TypeElement type);
}