package sk.seges.sesam.pap.service.printer.model;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;

public class ServiceConverterProviderPrinterContext extends ConverterProviderPrinterContext {

	private final ExecutableElement localMethod;
	
	public ServiceConverterProviderPrinterContext(DtoDeclaredType dtoType, ExecutableElement localMethod, ConfigurationTypeElement configurationType) {
		super(dtoType, configurationType);
		this.localMethod = localMethod;
	}
	
	public ServiceConverterProviderPrinterContext(DomainDeclaredType domainType, ExecutableElement localMethod) {
		super(domainType);
		this.localMethod = localMethod;
	}
	
	protected ServiceConverterProviderPrinterContext(DomainDeclaredType rawDomain, DomainDeclaredType domain, DtoDeclaredType rawDto, 
			DtoDeclaredType dto, ConverterTypeElement converterType, ExecutableElement localMethod) {
		super(rawDomain, domain, rawDto, dto, converterType);
		this.localMethod = localMethod;
	}
	
	public ExecutableElement getLocalMethod() {
		return localMethod;
	}
}