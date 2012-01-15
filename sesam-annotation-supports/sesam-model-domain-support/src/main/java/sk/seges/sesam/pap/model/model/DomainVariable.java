package sk.seges.sesam.pap.model.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.pap.model.model.api.domain.DomainTypeVariable;
import sk.seges.sesam.pap.model.model.api.dto.DtoTypeVariable;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

class DomainVariable extends TomBaseVariable implements DomainTypeVariable {

	private MutableTypeVariable domainTypeVariable;
	
	public DomainVariable(MutableTypeVariable domainTypeVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv,
			ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv, configurationProviders);
		this.domainTypeVariable = domainTypeVariable;
	}

	public DomainVariable(TypeVariable domainTypeVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv,
			ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv, configurationProviders);
		this.domainTypeVariable = (MutableTypeVariable)processingEnv.getTypeUtils().toMutableType(domainTypeVariable);
	}

	public DomainVariable(WildcardType wildcardType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv,
			ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv, configurationProviders);
		this.domainTypeVariable = (MutableTypeVariable)processingEnv.getTypeUtils().toMutableType(wildcardType);
	}

	@Override
	protected List<ConfigurationTypeElement> getConfigurationsForType() {
		return getConfigurations(domainTypeVariable);
	}
	
	private List<ConfigurationTypeElement> getConfigurations(MutableTypeMirror domainType) {
		for (ConfigurationProvider configurationProvider: configurationProviders) {
			List<ConfigurationTypeElement> configurationsForDomain = configurationProvider.getConfigurationsForDomain(domainType);
			if (configurationsForDomain != null && configurationsForDomain.size() > 0) {
				for (ConfigurationTypeElement configurationForDomain: configurationsForDomain) {
					configurationForDomain.setConfigurationProviders(configurationProviders);
				}
				return configurationsForDomain;
			}
		}
		
		return new ArrayList<ConfigurationTypeElement>();
	}

	@Override
	public ConverterTypeElement getConverter() {
		
		ConfigurationTypeElement converterDefinitionConfiguration = getConverterDefinitionConfiguration();
		
		if (converterDefinitionConfiguration == null) {
			return null;
		}
		
		return converterDefinitionConfiguration.getConverter();
	}


	@Override
	public DtoTypeVariable getDto() {
		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();
		
		if (domainDefinitionConfiguration == null) {
			List<MutableTypeMirror> dtoUpperBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getUpperBounds()) {
				dtoUpperBounds.add(processingEnv.getTransferObjectUtils().getDomainType(bound).getDto());
			}
			List<MutableTypeMirror> dtoLowerBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getLowerBounds()) {
				dtoLowerBounds.add(processingEnv.getTransferObjectUtils().getDomainType(bound).getDto());
			}
			String variable = getVariable();
			if (variable != null && !variable.equals(MutableWildcardType.WILDCARD_NAME)) {
				variable = ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "_" + getVariable();
			}
			return new DtoVariable(processingEnv.getTypeUtils().getTypeVariable(variable, dtoUpperBounds.toArray(new MutableTypeMirror[] {}), dtoLowerBounds.toArray(new MutableTypeMirror[]{})), processingEnv, roundEnv);
		}
		
		return (DtoTypeVariable) domainDefinitionConfiguration.getDto();
	}

	@Override
	protected MutableTypeVariable getDelegate() {
		return domainTypeVariable;
	}
}