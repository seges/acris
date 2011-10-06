package sk.seges.sesam.pap.model.model;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.pap.model.model.api.domain.DomainTypeVariable;
import sk.seges.sesam.pap.model.model.api.dto.DtoTypeVariable;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

class DomainVariable extends TomBaseVariable implements DomainTypeVariable {

	private MutableTypeVariable domainTypeVariable;
	
	private boolean configurationTypeInitialized = false;
	protected ConfigurationTypeElement configurationTypeElement;
	
	protected final ConfigurationProvider[] configurationProviders;

	public DomainVariable(MutableTypeVariable domainTypeVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv,
			ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.domainTypeVariable = domainTypeVariable;
	}

	public DomainVariable(TypeVariable domainTypeVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv,
			ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.domainTypeVariable = (MutableTypeVariable)processingEnv.getTypeUtils().toMutableType(domainTypeVariable);
	}

	public DomainVariable(WildcardType wildcardType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv,
			ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.domainTypeVariable = (MutableTypeVariable)processingEnv.getTypeUtils().toMutableType(wildcardType);
	}

	@Override
	public ConfigurationTypeElement getConfiguration() {
		if (!configurationTypeInitialized) {
			this.configurationTypeElement = getConfiguration(domainTypeVariable);
			this.configurationTypeInitialized = true;
		}
		return configurationTypeElement;
	}

	private ConfigurationTypeElement getConfiguration(MutableTypeMirror domainType) {
		for (ConfigurationProvider configurationProvider: configurationProviders) {
			ConfigurationTypeElement configurationForDomain = configurationProvider.getConfigurationForDomain(domainType);
			if (configurationForDomain != null) {
				configurationForDomain.setConfigurationProviders(configurationProviders);
				return configurationForDomain;
			}
		}
		
		return null;
	}

	@Override
	public ConverterTypeElement getConverter() {
		if (getConfiguration() == null) {
			return null;
		}
		
		return getConfiguration().getConverter();
	}


	@Override
	public DtoTypeVariable getDto() {
		if (getConfiguration() == null) {
			List<MutableTypeMirror> dtoUpperBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getUpperBounds()) {
				dtoUpperBounds.add(processingEnv.getTransferObjectUtils().getDomainType(bound).getDto());
			}
			List<MutableTypeMirror> dtoLowerBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getLowerBounds()) {
				dtoLowerBounds.add(processingEnv.getTransferObjectUtils().getDomainType(bound).getDto());
			}
			return new DtoVariable(processingEnv.getTypeUtils().getTypeVariable(getVariable(), dtoUpperBounds.toArray(new MutableTypeMirror[] {}), dtoLowerBounds.toArray(new MutableTypeMirror[]{})), processingEnv, roundEnv);
		}
		
		return (DtoTypeVariable) getConfiguration().getDto();
	}

	@Override
	protected MutableTypeVariable getDelegate() {
		return domainTypeVariable;
	}
}