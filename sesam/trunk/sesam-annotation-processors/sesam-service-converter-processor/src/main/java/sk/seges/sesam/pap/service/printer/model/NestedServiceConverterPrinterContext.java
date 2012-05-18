package sk.seges.sesam.pap.service.printer.model;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

public class NestedServiceConverterPrinterContext {

	private final DomainDeclaredType rawDomain;
	private final DomainDeclaredType domain;

	private final DtoDeclaredType rawDto;
	private final DtoDeclaredType dto;
	
	private final ConverterTypeElement converterType;
	private final ExecutableElement localMethod;
	
	public NestedServiceConverterPrinterContext(DtoDeclaredType dtoType, ExecutableElement localMethod) {
		this.rawDomain = (DomainDeclaredType)dtoType.getDomain();
		this.domain = this.rawDomain;
		this.rawDto = dtoType.getConverter().getConfiguration().getRawDto();
		this.converterType = dtoType.getConverter();
		this.localMethod = localMethod;
		this.dto = dtoType.getConverter().getConfiguration().getDto();
	}
	
	public NestedServiceConverterPrinterContext(DomainDeclaredType domainType, ExecutableElement localMethod) {
		DtoType dto = domainType.getDto();
		this.rawDomain = domainType;
		this.domain = (DomainDeclaredType) dto.getDomain();
		this.rawDto = domainType.getConverter().getConfiguration().getRawDto();
		this.dto = (DtoDeclaredType) dto;
		this.converterType = dto.getConverter();
		this.localMethod = localMethod;
	}
	
	protected NestedServiceConverterPrinterContext(DomainDeclaredType rawDomain, DomainDeclaredType domain, DtoDeclaredType rawDto, 
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