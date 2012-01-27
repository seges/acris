package sk.seges.sesam.pap.service.printer.model;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;

public class NestedServiceConverterPrinterContext {

	private DomainDeclaredType rawDomain;
	private DtoDeclaredType rawDto;
	
	private ConverterTypeElement converterType;
	private ExecutableElement localMethod;
	
	public NestedServiceConverterPrinterContext(DomainDeclaredType rawDomain, DtoDeclaredType rawDto, ConverterTypeElement converterType, ExecutableElement localMethod) {
		this.rawDomain = rawDomain;
		this.rawDto = rawDto;
		this.converterType = converterType;
		this.localMethod = localMethod;
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
}