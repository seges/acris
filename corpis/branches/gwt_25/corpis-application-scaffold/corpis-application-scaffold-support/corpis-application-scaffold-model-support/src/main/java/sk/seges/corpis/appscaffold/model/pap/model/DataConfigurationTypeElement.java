package sk.seges.corpis.appscaffold.model.pap.model;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;

public class DataConfigurationTypeElement extends ConfigurationTypeElement {
	
//	private DomainDeclaredType domainType;
//	private boolean domainTypeInitialized = false;
	private DataTypeResolver dataTypeResolver;
	
	public DataConfigurationTypeElement(Element configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(configurationElement, envContext, configurationContext);
		dataTypeResolver = new DataTypeResolver(envContext);
	}

	public DataConfigurationTypeElement(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext,
			ConfigurationContext configurationContext) {
		super(configurationElementMethod, returnType, envContext, configurationContext);
		dataTypeResolver = new DataTypeResolver(envContext);
	}

	public DataConfigurationTypeElement(MutableDeclaredType domainType, MutableDeclaredType dtoType, TypeElement configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext,
			ConfigurationContext configurationContext) {
		super(domainType, dtoType, configurationElement, envContext, configurationContext);
		dataTypeResolver = new DataTypeResolver(envContext);
	}

	@Override
	protected ConfigurationTypeElement getConfigurationTypeElement(Element configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		return new DataConfigurationTypeElement(transferObjectConfiguration.getConfiguration(), envContext, configurationContext);
	}

	@Override
	public DomainDeclaredType getInstantiableDomain() {
		return new DataDomainDeclared(super.getInstantiableDomain(), envContext, configurationContext);
	}
	
	@Override
	public DomainDeclaredType getInstantiableDomain(MutableDeclaredType domainType, MutableDeclaredType dtoType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		return new DataDomainDeclared(domainType, dtoType, envContext, configurationContext);
	}

	@Override
	protected DomainDeclaredType getDomain(MutableDeclaredType domainType, MutableDeclaredType dtoType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		return dataTypeResolver.getDomainDataType(super.getDomain(domainType, dtoType, envContext, configurationContext), domainType, configurationContext);
	}

	@Override
	public boolean appliesForDomainType(MutableTypeMirror domainType) {
		TypeElement declaredDomainType = transferObjectConfiguration.getDomain();
		if (declaredDomainType != null) {
			List<MutableDeclaredType> mutableDomainData = dataTypeResolver.getDomainData(getTypeUtils().toMutableType(declaredDomainType));
			if (mutableDomainData.size() > 0) {
				for (MutableDeclaredType mutableDomain: mutableDomainData) {
					if (getTypeUtils().isSameType(mutableDomain, domainType)) {
						return true;
					}
				}
			}
		}
		return super.appliesForDomainType(domainType);
	}

}