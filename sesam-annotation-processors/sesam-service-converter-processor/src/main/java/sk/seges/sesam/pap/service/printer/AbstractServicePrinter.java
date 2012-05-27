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

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.ConverterParameterFilter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class AbstractServicePrinter {

	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final ConverterConstructorParametersResolver parametersResolver;
	
	protected AbstractServicePrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolver parametersResolver) {
		this.processingEnv = processingEnv;
		this.parametersResolver = parametersResolver;
	}
	
	/**
	 * Collects all converters from the remote service interface methods return types and method parameters and returns all parameters
	 * required by these converters.
	 * This parameters should be passed to the service exporter constructor and initialized by dependency injection of in the upper layer.
	 */
	protected List<ConverterParameter> getConverterParameters(ServiceTypeElement serviceTypeElement, LocalServiceTypeElement localInterface) {
		RemoteServiceTypeElement remoteServiceInterface = localInterface.getRemoteServiceInterface();

		List<ConverterParameter> parameters = new LinkedList<ConverterParameter>();

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
				
				DtoType dtoReturnType = processingEnv.getTransferObjectUtils().getDtoType(remoteMethod.getReturnType());

				ConverterTypeElement converter = dtoReturnType.getConverter();
			
				if (converter != null && !converters.contains(converter)) {
					parameters.addAll(converter.getConverterParameters(parametersResolver, ConverterInstancerType.SERVICE_CONVERETR_INSTANCER));
					converters.add(converter);
				}
			}

			for (int index = 0; index < localMethod.getParameters().size(); index++) {
				TypeMirror dtoType = remoteMethod.getParameters().get(index).asType();

				DtoType dtoReturnType = processingEnv.getTransferObjectUtils().getDtoType(dtoType);

				ConverterTypeElement converter = dtoReturnType.getConverter();

				if (converter != null && !converters.contains(converter)) {
					parameters.addAll(converter.getConverterParameters(parametersResolver, ConverterInstancerType.SERVICE_CONVERETR_INSTANCER));
					converters.add(converter);
				}
			}
		}

		return parameters;
	}

	protected ExecutableElement getDomainMethodPair(ExecutableElement remoteMethod, ServiceTypeElement serviceTypeElement) {
		
		for (LocalServiceTypeElement localServiceInterface: serviceTypeElement.getLocalServiceInterfaces()) {
		
			List<ExecutableElement> methods = ElementFilter.methodsIn(localServiceInterface.asElement().getEnclosedElements());
	
			for (ExecutableElement method : methods) {
				
				method = ProcessorUtils.getOverrider(serviceTypeElement.asElement(), method, processingEnv);
				
				boolean pairMethod = false;
	
				if (method.getSimpleName().toString().equals(remoteMethod.getSimpleName().toString())
						&& method.getParameters().size() == remoteMethod.getParameters().size()) {
					pairMethod = true;
					int index = 0;
					for (VariableElement dtoParameter : remoteMethod.getParameters()) {
						
						DtoType parameterDtoType = processingEnv.getTransferObjectUtils().getDtoType(dtoParameter.asType());
						DomainType parameterDomainType = parameterDtoType.getDomain();
	
						if (!processingEnv.getTypeUtils().isSameType(parameterDomainType, processingEnv.getTypeUtils().toMutableType(method.getParameters().get(index).asType()))) {
							pairMethod = false;
							break;
						}
						index++;
					}
				}
	
				if (pairMethod) {
					
					DtoType returnDtoType = processingEnv.getTransferObjectUtils().getDtoType(method.getReturnType());
					DomainType returnDomainType = returnDtoType.getDomain();
	
					if (processingEnv.getTypeUtils().isSameType(returnDomainType, returnDtoType)) {
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
	
	protected List<ConverterParameter> unifyParameterNames(List<ConverterParameter> allPparameters, List<ConverterParameter> parameters) {

		for (ConverterParameter parameter : parameters) {
			int index = 1;
			String name = parameter.getName();
			while (ConverterParameterFilter.NAME.filterBy(allPparameters, parameter).size() > 0) {
				parameter.setName(name + index++);
			}
			allPparameters.add(parameter);
		}

		return allPparameters;
	}

	protected List<ConverterParameter> mergeSameParams(List<ConverterParameter> converterParameters) {
		List<ConverterParameter> result = new LinkedList<ConverterParameter>();

		for (ConverterParameter parameter : converterParameters) {
			ConverterParameter sameParameterByType = getSameByType(parameter.getType(), result);
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
	private ConverterParameter getSameByType(MutableTypeMirror typeParameter, Collection<ConverterParameter> parameters) {
		for (ConverterParameter parameter : parameters) {
			if (parameter.getType().toString(ClassSerializer.CANONICAL).equals(typeParameter.toString(ClassSerializer.CANONICAL))) {
				return parameter;
			}
		}

		return null;
	}
}