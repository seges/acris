package sk.seges.sesam.pap.service.printer.model;

import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ServiceConverterPrinterContext {

	private LocalServiceTypeElement localService;
	private String localServiceFieldName;
	private ServiceTypeElement service;
	
	public LocalServiceTypeElement getLocalServiceInterface() {
		return localService;
	}
	
	public void setLocalServiceInterface(LocalServiceTypeElement localService) {
		this.localService = localService;
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
