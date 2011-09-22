package sk.seges.sesam.pap.model.printer.api;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;


public interface ElementPrinter {

	public static final String DOMAIN_NAME = "_domain";
	public static final String RESULT_NAME = "_result";
	public static final String DTO_NAME = "_dto";

	void initialize(ConfigurationTypeElement configurationTypeElement, NamedType outputName);
	
	void print(ProcessorContext context);
	
	void finish(ConfigurationTypeElement configuratioTypeElement);
}