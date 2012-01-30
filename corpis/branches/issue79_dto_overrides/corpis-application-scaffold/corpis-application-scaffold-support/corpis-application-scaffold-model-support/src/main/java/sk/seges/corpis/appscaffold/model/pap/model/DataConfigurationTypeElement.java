package sk.seges.corpis.appscaffold.model.pap.model;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.appscaffold.shared.annotation.DomainData;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainDeclared;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class DataConfigurationTypeElement extends ConfigurationTypeElement {
	
	private DomainDeclaredType domainType;
	private boolean domainTypeInitialized = false;
	
	public DataConfigurationTypeElement(MutableDeclaredType domainType, MutableDeclaredType dtoType, TypeElement configurationElement, 
			TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(domainType, dtoType, configurationElement, processingEnv, roundEnv, configurationProviders);
	} 

	public DataConfigurationTypeElement(Element configurationElement, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(configurationElement, processingEnv, roundEnv, configurationProviders);
	}

	public DomainDeclaredType getDomainEntity() {
		return super.getDomain();
	}
	
	@Override
	public DomainDeclaredType getDomain() {
		
		if (!domainTypeInitialized) {
			DomainDeclaredType domainDeclared = super.getDomain();
			MutableDeclaredType result = findDomainData(domainDeclared.asMutable());
			if (result != null) {
				domainDeclared = new DomainDeclared(result, dtoType, new ConfigurationTypeElement[] { this }, processingEnv, roundEnv, configurationProviders);
				domainDeclared = replaceTypeParamsByWildcard(domainDeclared);
			}
			
			this.domainType = domainDeclared;
			this.domainTypeInitialized = true;
		}
		
		return domainType;
	}
	
	private MutableDeclaredType findDomainData(MutableDeclaredType declaredType) {
		
		if (declaredType.getAnnotation(DomainData.class) != null) {
			return declaredType;
		}
		
		if (declaredType.getSuperClass() != null) {
			MutableDeclaredType result = findDomainData(declaredType.getSuperClass());
			if (result != null) {
				return result;
			}
		}
		
		for (MutableTypeMirror interfaces : declaredType.getInterfaces()) {
			MutableDeclaredType result = findDomainData((MutableDeclaredType)interfaces);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	@Override
	public boolean appliesForDomainType(MutableTypeMirror domainType) {
		TypeElement declaredDomainType = transferObjectConfiguration.getDomain();
		if (declaredDomainType != null) {
			MutableDeclaredType mutableDomainData = findDomainData(processingEnv.getTypeUtils().toMutableType(declaredDomainType));
			if (mutableDomainData != null && processingEnv.getTypeUtils().isSameType(mutableDomainData, domainType)) {
				return true;
			}
		}
		return super.appliesForDomainType(domainType);
	}
	
	@Override
	public boolean appliesForDtoType(MutableTypeMirror dtoType) {
		// TODO Auto-generated method stub
		return super.appliesForDtoType(dtoType);
	}
	
	private DomainDeclaredType replaceTypeParamsByWildcard(DomainDeclaredType domainDeclared) {
		if (domainDeclared.getTypeVariables().size() > 0) {
			MutableTypeVariable[] typeVariables = new MutableTypeVariable[domainDeclared.getTypeVariables().size()];
			for (int i = 0; i < domainDeclared.getTypeVariables().size(); i++) {
				typeVariables[i] = processingEnv.getTypeUtils().getTypeVariable(MutableWildcardType.WILDCARD_NAME);
			}
			domainDeclared.setTypeVariables(typeVariables);
		}
		return domainDeclared;
	}
}
