package sk.seges.corpis.appscaffold.model.pap.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.corpis.appscaffold.shared.annotation.DomainData;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
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
		
		if (!domainDeclared.toString(ClassSerializer.CANONICAL, false).equals(dataType.toString(ClassSerializer.CANONICAL, false))) {
			
			Element domainElement = domainDeclared.asElement();
			
			if (domainElement == null) {
				TypeMirror fromMutableType = envContext.getProcessingEnv().getTypeUtils().fromMutableType(domainDeclared);
				domainElement = ((DeclaredType)fromMutableType).asElement();
			}
			
			if (domainElement == null) {
				return false;
			}
			
			Element dataElement = dataType.asElement();
	
			if (dataElement == null) {
				TypeMirror fromMutableType = envContext.getProcessingEnv().getTypeUtils().fromMutableType(dataType);
				dataElement = ((DeclaredType)fromMutableType).asElement();
			}
	
			if (dataElement == null) {
				return false;
			}
			
			List<ExecutableElement> methods = ElementFilter.methodsIn(domainElement.getEnclosedElements());
	
			for (ExecutableElement method: methods) {
				boolean isGetter = MethodHelper.isGetterMethod(method);
				boolean isPublic = method.getModifiers().contains(Modifier.PUBLIC);
	
				if (isGetter && isPublic) {
					if (!ProcessorUtils.hasMethod(method.getSimpleName().toString(), dataElement)) {
						return true;
					}
				}
			}
			
			return false;
			
		} else if (domainDeclared.hasTypeParameters() && dataType.hasTypeParameters()) {
			
			if (domainDeclared.getTypeVariables().size() != dataType.getTypeVariables().size()) {
				envContext.getProcessingEnv().getMessager().printMessage(Kind.ERROR, "Domain type " + domainDeclared + " has invalid data type found: " + dataType + ". It seems like a bug in the processors!");
				return true;
			}
			
			for (int i = 0; i < domainDeclared.getTypeVariables().size(); i++) {
				MutableTypeVariable domainTypeVariable = domainDeclared.getTypeVariables().get(i);
				MutableTypeVariable dataTypeVariable = dataType.getTypeVariables().get(i);
				
				if (domainTypeVariable.getLowerBounds().size() != dataTypeVariable.getLowerBounds().size()) {
					envContext.getProcessingEnv().getMessager().printMessage(Kind.ERROR, "Domain type " + domainDeclared + " has invalid data type found: " + dataType + ". It seems like a bug in the processors!");
					return true;
				}

				if (domainTypeVariable.getUpperBounds().size() != dataTypeVariable.getUpperBounds().size()) {
					envContext.getProcessingEnv().getMessager().printMessage(Kind.ERROR, "Domain type " + domainDeclared + " has invalid data type found: " + dataType + ". It seems like a bug in the processors!");
					return true;
				}
				
				if (processBounds(domainTypeVariable.getLowerBounds(), dataTypeVariable.getLowerBounds())) {
					return true;
				}

				if (processBounds(domainTypeVariable.getLowerBounds(), dataTypeVariable.getLowerBounds())) {
					return true;
				}

			}
		}
		
		return false;
	}

	private boolean processBounds(Set<? extends MutableTypeMirror> domainBounds, Set<? extends MutableTypeMirror> dataBounds) {
		if (domainBounds.size() > 0) {
			Iterator<? extends MutableTypeMirror> domainIterator = domainBounds.iterator();
			Iterator<? extends MutableTypeMirror> dataIterator = dataBounds.iterator();
			
			while (domainIterator.hasNext()) {
				MutableTypeMirror domain = domainIterator.next();
				
				DomainDeclaredType domainDeclaredType = null;
				
				if (!(domain instanceof DomainDeclaredType)) {
					domainDeclaredType = (DomainDeclaredType) envContext.getProcessingEnv().getTransferObjectUtils().getDomainType(domain);
				} else {
					domainDeclaredType = (DomainDeclaredType) domain;
				}
				
				if (hasCustomProperties(domainDeclaredType, (MutableDeclaredType)dataIterator.next())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public DomainDeclaredType getDomain() {
		
		if (!domainTypeInitialized) {
			this.domainType = getDomainDataType(super.getDomain());
			this.domainTypeInitialized = true;
		}
		
		return domainType;
	}
	
	private Set<? extends MutableTypeMirror> processBounds(Set<? extends MutableTypeMirror> bounds) {
		if (bounds.size() > 0) {
			Set<MutableTypeMirror> newBounds = new HashSet<MutableTypeMirror>(); 
			Iterator<? extends MutableTypeMirror> iterator = bounds.iterator();
			
			while (iterator.hasNext()) {
				MutableTypeMirror next = iterator.next();
				
				if (next.getKind().isDeclared()) {
					
					List<MutableDeclaredType> dataInterfaces = new ArrayList<MutableDeclaredType>();
					findDomainData(((MutableDeclaredType)next), dataInterfaces);
					
					if (dataInterfaces.size() > 0) {
						MutableDeclaredType dtoType = (MutableDeclaredType)envContext.getProcessingEnv().getTransferObjectUtils().getDomainType(next).getDto();
						DomainDeclaredType domainDeclared = new DomainDeclared(dataInterfaces.get(0), dtoType, envContext, configurationContext);
						domainDeclared.setTypeVariables(new MutableTypeVariable[]{});
						
						if (!hasCustomProperties(domainDeclared, (MutableDeclaredType) next)) {
							newBounds.add(domainDeclared);
						} else {
							newBounds.add(next);
						}
						
					} else {
						newBounds.add(next);
					}
				} else {
					newBounds.add(next);
				}
			}
			
			return newBounds;
		}

		return bounds;
	}
	
	private DomainDeclaredType getDomainDataType(DomainDeclaredType domainType) {
		List<MutableDeclaredType> result = new ArrayList<MutableDeclaredType>();
		findDomainData(domainType, result);
		
		DomainDeclaredType domainDeclared = domainType;
		
		if (result.size() > 0) {
			domainDeclared = new DomainDeclared(result.get(0), dtoType, envContext, configurationContext);
			domainDeclared = replaceTypeParamsByWildcard(domainDeclared);
		} else {
			List<MutableTypeVariable> typeParams = new LinkedList<MutableTypeVariable>();
			
			for (MutableTypeVariable typeVariable: domainType.getTypeVariables()) {

				Set<? extends MutableTypeMirror> lbounds = new HashSet<MutableTypeMirror>();
				
				if (typeVariable.getLowerBounds().size() > 0) {
					lbounds = processBounds(typeVariable.getLowerBounds());
				}

				Set<? extends MutableTypeMirror> ubounds = new HashSet<MutableTypeMirror>();

				if (typeVariable.getUpperBounds().size() > 0) {
					ubounds = processBounds(typeVariable.getUpperBounds());
				}
				typeParams.add(envContext.getProcessingEnv().getTypeUtils().getTypeVariable(typeVariable.getVariable(), ubounds.toArray(new MutableTypeMirror[] {}), lbounds.toArray(new MutableTypeMirror[] {})));
			}
			
			DomainDeclaredType superClass = domainType.getSuperClass();
			List<? extends MutableTypeMirror> interfaces = domainDeclared.getInterfaces();
			
			domainType.setTypeVariables(typeParams.toArray(new MutableTypeVariable[] {}));
			
			domainType.setSuperClass(superClass);
			domainDeclared.setInterfaces(interfaces);
			
			domainDeclared = domainType;
		}
		
		if (!hasCustomProperties(domainType, domainDeclared)) {
			return domainDeclared;
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

		MutableDeclaredType superClass = null;
		
		if (declaredType instanceof DelegateMutableDeclaredType) {
			superClass = ((DelegateMutableDeclaredType)declaredType).ensureDelegateType().getSuperClass();
		} else {
			superClass = declaredType.getSuperClass();
		}

		if (superClass != null) {
			findDomainData(superClass, domainDataTypes);
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