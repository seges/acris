package sk.seges.sesam.pap.service.printer;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.model.ConverterParameterFilter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class AbstractServicePrinter {

	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final ConverterConstructorParametersResolverProvider parametersResolverProvider;
	
	protected AbstractServicePrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolverProvider parametersResolverProvider) {
		this.processingEnv = processingEnv;
		this.parametersResolverProvider = parametersResolverProvider;
	}
	
	private void collectTypeConverters(TypeMirror type, Set<ConverterTypeElement> converters, List<ConverterConstructorParameter> parameters) {
		collectTypeConverters(processingEnv.getTypeUtils().toMutableType(type), converters, parameters);
	}
	
	private void collectTypeConverters(MutableTypeMirror type, Set<ConverterTypeElement> converters, List<ConverterConstructorParameter> parameters) {

		DtoType dtoType = processingEnv.getTransferObjectUtils().getDtoType(type);

		ConverterTypeElement converter = dtoType.getConverter();
	
		if (converter != null && !converters.contains(converter)) {
			parameters.addAll(converter.getConverterParameters(parametersResolverProvider.getParameterResolver(UsageType.DEFINITION), ConverterInstancerType.SERVICE_CONVERETR_INSTANCER));
			converters.add(converter);
			
			if (dtoType.getKind().equals(MutableTypeKind.CLASS) || dtoType.getKind().equals(MutableTypeKind.INTERFACE)) {
				for (MutableTypeVariable typeVariable: ((MutableDeclaredType)dtoType).getTypeVariables()) {
					for (MutableTypeMirror lowerBound: typeVariable.getLowerBounds()) {
						collectTypeConverters(lowerBound, converters, parameters);
					}
					for (MutableTypeMirror upperBound: typeVariable.getUpperBounds()) {
						collectTypeConverters(upperBound, converters, parameters);
					}
				}
			}
		}
	}
	
	/**
	 * Collects all converters from the remote service interface methods return types and method parameters and returns all parameters
	 * required by these converters.
	 * This parameters should be passed to the service exporter constructor and initialized by dependency injection of in the upper layer.
	 */
	protected List<ConverterConstructorParameter> getConverterParameters(ServiceTypeElement serviceTypeElement, LocalServiceTypeElement localInterface) {
		RemoteServiceTypeElement remoteServiceInterface = localInterface.getRemoteServiceInterface();

		List<ConverterConstructorParameter> parameters = new LinkedList<ConverterConstructorParameter>();

		if (remoteServiceInterface == null) {
			return parameters;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.asElement().getEnclosedElements());

		Set<ConverterTypeElement> converters = new HashSet<ConverterTypeElement>();

		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, serviceTypeElement);
			
			//If the remote method does not have local method pair then invalid service interface are used
			if (localMethod == null) {
				continue;
			}

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				collectTypeConverters(remoteMethod.getReturnType(), converters, parameters);
			}

			for (int index = 0; index < localMethod.getParameters().size(); index++) {
				TypeMirror dtoType = remoteMethod.getParameters().get(index).asType();
				collectTypeConverters(dtoType, converters, parameters);
			}
		}

		return parameters;
	}

	protected ExecutableElement getDomainMethodPair(ExecutableElement remoteMethod, ServiceTypeElement serviceTypeElement) {
		
		for (LocalServiceTypeElement localServiceInterface: serviceTypeElement.getLocalServiceInterfaces()) {
		
			List<ExecutableElement> methods = ElementFilter.methodsIn(localServiceInterface.asElement().getEnclosedElements());
	
			for (ExecutableElement method : methods) {
				
				ExecutableElement overriderMethod = ProcessorUtils.getOverrider(serviceTypeElement.asElement(), method, processingEnv);
				
				boolean pairMethod = false;
	
				if (overriderMethod.getSimpleName().toString().equals(remoteMethod.getSimpleName().toString())
						&& overriderMethod.getParameters().size() == remoteMethod.getParameters().size()) {
					pairMethod = true;
					int index = 0;
					for (VariableElement dtoParameter : remoteMethod.getParameters()) {
						
						DtoType parameterDtoType = processingEnv.getTransferObjectUtils().getDtoType(dtoParameter.asType());
						DomainType parameterDomainType = parameterDtoType.getDomain();
	
						if (!processingEnv.getTypeUtils().isSameType(parameterDomainType, processingEnv.getTypeUtils().toMutableType(overriderMethod.getParameters().get(index).asType()))) {
							pairMethod = false;
							break;
						}
						index++;
					}
				}
	
				if (pairMethod) {
					
					DomainType returnDomainType = processingEnv.getTransferObjectUtils().getDomainType(overriderMethod.getReturnType());
					DtoType returnDtoType = returnDomainType.getDto();
					
					MutableTypeMirror mutableRemoteReturnType = processingEnv.getTypeUtils().toMutableType(remoteMethod.getReturnType());
					
					if (processingEnv.getTypeUtils().isSameType(returnDtoType, mutableRemoteReturnType)) {
						return method;
					}
					
					processingEnv.getMessager().printMessage(Kind.ERROR,
							"[ERROR] Service method return type does not match the remote interface definition " + remoteMethod.toString()
									+ ". This should have never happened, you are probably a magician or there is a bug in the sesam processor itself.",
							serviceTypeElement.asElement());
				}
			}
		}
		
		return null;
	}
	
	protected List<ConverterConstructorParameter> unifyParameterNames(List<ConverterConstructorParameter> allPparameters, List<ConverterConstructorParameter> parameters) {

		for (ConverterConstructorParameter parameter : parameters) {
			int index = 1;
			String name = parameter.getName();
			while (ConverterParameterFilter.NAME.filterBy(allPparameters, parameter).size() > 0) {
				parameter.setName(name + index++);
			}
			allPparameters.add(parameter);
		}

		return allPparameters;
	}

	protected List<ConverterConstructorParameter> mergeSameParams(List<ConverterConstructorParameter> converterParameters) {
		List<ConverterConstructorParameter> result = new LinkedList<ConverterConstructorParameter>();

		for (ConverterConstructorParameter parameter : converterParameters) {
			ConverterConstructorParameter sameParameterByType = getSameByType(parameter.getType(), result);
			if (sameParameterByType == null) {
				result.add(parameter);
			} else {
				parameter.setSameParameter(sameParameterByType);
			}
		}

		return result;
	}

	// TODO If the converter has 2 same parameters, like Cache cache1, Cache cache2
	// and another converter has same 2 parameters, Cache cache1, Cache cache2, so the result won't be only unify cache parameter but 2, cache1 and
	// cache2
	private ConverterConstructorParameter getSameByType(MutableTypeMirror typeParameter, Collection<ConverterConstructorParameter> parameters) {
		for (ConverterConstructorParameter parameter : parameters) {
			if (parameter.getType().toString(ClassSerializer.CANONICAL).equals(typeParameter.toString(ClassSerializer.CANONICAL))) {
				return parameter;
			}
		}

		return null;
	}
}