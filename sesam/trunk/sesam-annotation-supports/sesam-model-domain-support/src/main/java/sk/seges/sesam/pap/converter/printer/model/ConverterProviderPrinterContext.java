package sk.seges.sesam.pap.converter.printer.model;

import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

public class ConverterProviderPrinterContext {

	private final DomainDeclaredType rawDomain;
	private final DomainDeclaredType domain;

	private final DtoDeclaredType rawDto;
	private final DtoDeclaredType dto;
	
	private final ConverterTypeElement converterType;
	
	public ConverterProviderPrinterContext(DtoDeclaredType dtoType) {
		this.rawDomain = (DomainDeclaredType)dtoType.getDomain();
		this.domain = this.rawDomain;
		if (dtoType.getConverter() == null) {
			if (dtoType.getConfigurations().size() == 0) {
				this.rawDto = dtoType;
				this.dto = dtoType;
			} else {
				this.rawDto = dtoType.getConfigurations().get(0).getRawDto();
				this.dto = dtoType.getConfigurations().get(0).getDto();
			}
		} else {
			this.rawDto = dtoType.getConverter().getConfiguration().getRawDto();
			this.dto = dtoType.getConverter().getConfiguration().getDto();
		}
		this.converterType = dtoType.getConverter();
	}
	
	public ConverterProviderPrinterContext(DomainDeclaredType domainType) {
		DtoType dto = domainType.getDto();
		this.rawDomain = domainType;
		this.domain = (DomainDeclaredType) dto.getDomain();
		if (domainType.getConverter() == null) {
			if (domainType.getConfigurations().size() == 0) {
				this.rawDto = (DtoDeclaredType) dto;
			} else {
				this.rawDto = domainType.getConfigurations().get(0).getRawDto();
			}
		} else {
			this.rawDto = domainType.getConverter().getConfiguration().getRawDto();
		}
		this.dto = (DtoDeclaredType) dto;
		this.converterType = dto.getConverter();
	}
	
	protected ConverterProviderPrinterContext(DomainDeclaredType rawDomain, DomainDeclaredType domain, DtoDeclaredType rawDto, 
			DtoDeclaredType dto, ConverterTypeElement converterType) {
		this.rawDomain = rawDomain;
		this.rawDto = rawDto;
		this.converterType = converterType;
		this.dto = dto;
		this.domain = domain;
	}
	
	public ConverterTypeElement getConverterType() {
		return converterType;
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