package sk.seges.corpis.appscaffold.model.pap.model;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.appscaffold.shared.annotation.DomainData;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainDeclared;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;

public class DataConfigurationTypeElement extends ConfigurationTypeElement {
	
	private DomainDeclaredType domainType;
	private boolean domainTypeInitialized = false;
	
	public DataConfigurationTypeElement(Element configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(configurationElement, envContext, configurationContext);
	}

	public DataConfigurationTypeElement(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext,
			ConfigurationContext configurationContext) {
		super(configurationElementMethod, returnType, envContext, configurationContext);
	}

	public DataConfigurationTypeElement(MutableDeclaredType domainType, MutableDeclaredType dtoType, TypeElement configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext,
			ConfigurationContext configurationContext) {
		super(domainType, dtoType, configurationElement, envContext, configurationContext);
	}

	@Override
	public DomainDeclaredType getInstantiableDomain() {
		return super.getDomain();
	}

	@Override
	public DomainDeclaredType getDomain() {
		
		if (!domainTypeInitialized) {
			DomainDeclaredType domainDeclared = super.getDomain();
			MutableDeclaredType result = findDomainData(domainDeclared.asMutable());
			if (result != null) {
				domainDeclared = new DomainDeclared(result, dtoType, envContext, configurationContext);
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

		//We need to iterate over interfaces firstly - for cases that some data interfaces will be in hierarchy we have
		//to find most specified data interface
		for (MutableTypeMirror interfaces : declaredType.getInterfaces()) {
			MutableDeclaredType result = findDomainData((MutableDeclaredType)interfaces);
			if (result != null) {
				return result;
			}
		}

		if (declaredType.getSuperClass() != null) {
			MutableDeclaredType result = findDomainData(declaredType.getSuperClass());
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
			MutableDeclaredType mutableDomainData = findDomainData(getTypeUtils().toMutableType(declaredDomainType));
			if (mutableDomainData != null && getTypeUtils().isSameType(mutableDomainData, domainType)) {
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
				typeVariables[i] = getTypeUtils().getTypeVariable(MutableWildcardType.WILDCARD_NAME);
			}
			domainDeclared.setTypeVariables(typeVariables);
		}
		return domainDeclared;
	}
}
