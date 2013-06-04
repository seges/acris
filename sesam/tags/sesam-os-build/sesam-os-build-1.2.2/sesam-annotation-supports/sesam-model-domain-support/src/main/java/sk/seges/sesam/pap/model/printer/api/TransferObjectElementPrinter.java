package sk.seges.sesam.pap.model.printer.api;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public interface TransferObjectElementPrinter {

	public static final String DOMAIN_NAME = "_domain";
	public static final String RESULT_NAME = "_result";
	public static final String DTO_NAME = "_dto";

	void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName);
	
	void print(TransferObjectContext context);
	
	void finish(ConfigurationTypeElement configurationTypeElement);
}