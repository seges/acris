package sk.seges.sesam.pap.model.printer.api;

import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;


public interface ElementPrinter {
	
	void initialize(ConfigurationTypeElement configurationTypeElement);
	
	void print(ProcessorContext context);
	
	void finish(ConfigurationTypeElement configuratioTypeElement);
}