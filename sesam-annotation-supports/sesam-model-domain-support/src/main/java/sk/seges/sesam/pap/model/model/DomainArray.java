package sk.seges.sesam.pap.model.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableArray;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainArrayType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

class DomainArray extends DelegateMutableArray implements DomainArrayType {

	private final DomainType domainType;
	private final MutableProcessingEnvironment processingEnv;
	
	public DomainArray(DomainType domainType, MutableProcessingEnvironment processingEnv) {
		this.domainType = domainType;
		this.processingEnv = processingEnv;
	}

	@Override
	protected MutableArrayType getDelegate() {
		return processingEnv.getTypeUtils().getArrayType(domainType);
	}

	@Override
	public ConfigurationTypeElement getConfiguration() {
		return domainType.getConfiguration();
	}

	@Override
	public ConverterTypeElement getConverter() {
		return domainType.getConverter();
	}

	@Override
	public DtoType getDto() {
		return new DtoArray(domainType.getDto(), processingEnv);
	}
}