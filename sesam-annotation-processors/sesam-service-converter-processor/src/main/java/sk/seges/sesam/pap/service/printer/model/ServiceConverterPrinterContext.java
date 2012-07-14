package sk.seges.sesam.pap.service.printer.model;

import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ServiceConverterPrinterContext {

	private LocalServiceTypeElement localService;
	private String localServiceFieldName;
	private ServiceTypeElement service;
	
	private ConverterProviderElementPrinter nestedPrinter;
	
	public LocalServiceTypeElement getLocalServiceInterface() {
		return localService;
	}
	
	public void setLocalServiceInterface(LocalServiceTypeElement localService) {
		this.localService = localService;
	}

	public void setNestedPrinter(ConverterProviderElementPrinter nestedPrinter) {
		this.nestedPrinter = nestedPrinter;
	}
	
	public ConverterProviderElementPrinter getNestedPrinter() {
		return nestedPrinter;
	}
	
	public String getLocalServiceFieldName() {
		return localServiceFieldName;
	}

	public void setLocalServiceFieldName(String localServiceFieldName) {
		this.localServiceFieldName = localServiceFieldName;
	}

	public ServiceTypeElement getService() {
		return service;
	}

	public void setService(ServiceTypeElement service) {
		this.service = service;
	}
}
