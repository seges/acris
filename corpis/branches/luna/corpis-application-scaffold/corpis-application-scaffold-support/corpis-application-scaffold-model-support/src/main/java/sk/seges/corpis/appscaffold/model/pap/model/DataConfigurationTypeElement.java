package sk.seges.corpis.appscaffold.model.pap.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.shared.annotation.DomainData;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
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
		return super.getInstantiableDomain();
	}

	private boolean hasCustomProperties(DomainDeclaredType domainDeclared, MutableDeclaredType dataType) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(domainDeclared.asElement().getEnclosedElements());

		for (ExecutableElement method: methods) {
			boolean isGetter = MethodHelper.isGetterMethod(method);
			boolean isPublic = method.getModifiers().contains(Modifier.PUBLIC);

			if (isGetter && isPublic) {
				if (!ProcessorUtils.hasMethod(method.getSimpleName().toString(), dataType.asElement())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public DomainDeclaredType getDomain() {
		
		if (!domainTypeInitialized) {
			DomainDeclaredType domainDeclared = super.getDomain();
			List<MutableDeclaredType> result = new ArrayList<MutableDeclaredType>();
			findDomainData(domainDeclared.asMutable(), result);
			if (result.size() == 1) {
				domainDeclared = new DomainDeclared(result.get(0), dtoType, envContext, configurationContext);
				domainDeclared = replaceTypeParamsByWildcard(domainDeclared);
			}
			
			if (!hasCustomProperties(super.getDomain(), domainDeclared)) {
				this.domainType = domainDeclared;
			} else {
				this.domainType = super.getDomain();
			}
			
			this.domainTypeInitialized = true;
		}
		
		return domainType;
	}
	
	private void findDomainData(MutableDeclaredType declaredType, List<MutableDeclaredType> domainDataTypes) {
		
		if (declaredType.getAnnotation(DomainData.class) != null) {
			domainDataTypes.add(declaredType);
		}

		//We need to iterate over interfaces firstly - for cases that some data interfaces will be in hierarchy we have
		//to find most specified data interface
		for (MutableTypeMirror interfaces : declaredType.getInterfaces()) {
			findDomainData((MutableDeclaredType)interfaces, domainDataTypes);
		}

		if (declaredType.getSuperClass() != null) {
			findDomainData(declaredType.getSuperClass(), domainDataTypes);
		}
	}
	
	@Override
	public boolean appliesForDomainType(MutableTypeMirror domainType) {
		TypeElement declaredDomainType = transferObjectConfiguration.getDomain();
		if (declaredDomainType != null) {
			List<MutableDeclaredType> mutableDomainData = new ArrayList<MutableDeclaredType>(); 
			findDomainData(getTypeUtils().toMutableType(declaredDomainType), mutableDomainData);
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