package sk.seges.sesam.pap.service.printer.model;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;

public class NestedServiceConverterPrinterContext {

	private final DomainDeclaredType rawDomain;
	private final DomainDeclaredType domain;

	private final DtoDeclaredType rawDto;
	private final DtoDeclaredType dto;
	
	private final ConverterTypeElement converterType;
	private final ExecutableElement localMethod;
	
	public NestedServiceConverterPrinterContext(DomainDeclaredType rawDomain, DomainDeclaredType domain, DtoDeclaredType rawDto, 
			DtoDeclaredType dto, ConverterTypeElement converterType, ExecutableElement localMethod) {
		this.rawDomain = rawDomain;
		this.rawDto = rawDto;
		this.converterType = converterType;
		this.localMethod = localMethod;
		this.dto = dto;
		this.domain = domain;
	}
	
	public ConverterTypeElement getConverterType() {
		return converterType;
	}
	
	public ExecutableElement getLocalMethod() {
		return localMethod;
	}
	
	public DomainDeclaredType getRawDomain() {
		return rawDomain;
	}
	
	public DtoDeclaredType getRawDto() {
		return rawDto;
	}
	
	public DomainDeclaredType getDomain() {
		return domain;
	}
	
	public DtoDeclaredType getDto() {
		return dto;
	}
}