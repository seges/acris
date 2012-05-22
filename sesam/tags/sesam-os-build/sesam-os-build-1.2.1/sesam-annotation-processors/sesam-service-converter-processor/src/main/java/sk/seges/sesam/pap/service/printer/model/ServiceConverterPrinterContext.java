package sk.seges.sesam.pap.service.printer.model;

import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.NestedServiceConverterElementPrinter;

public class ServiceConverterPrinterContext {

	private LocalServiceTypeElement localService;
	private String localServiceFieldName;
	private ServiceTypeElement service;
	
	private NestedServiceConverterElementPrinter nestedPrinter;
	
	public LocalServiceTypeElement getLocalServiceInterface() {
		return localService;
	}
	
	public void setLocalServiceInterface(LocalServiceTypeElement localService) {
		this.localService = localService;
	}

	public void setNestedPrinter(NestedServiceConverterElementPrinter nestedPrinter) {
		this.nestedPrinter = nestedPrinter;
	}
	
	public NestedServiceConverterElementPrinter getNestedPrinter() {
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
