package sk.seges.sesam.pap.model.printer.converter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;

public class AbstractConverterPrinter {

	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final ConverterConstructorParametersResolverProvider parametersResolverProvider;
	
	protected AbstractConverterPrinter(ConverterConstructorParametersResolverProvider parametersResolverProvider, TransferObjectProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.parametersResolverProvider = parametersResolverProvider;
	}
	
	private String getParameterName(VariableElement parameter, ParameterElement... additionalParameters) {
		
		MutableTypes typeUtils = processingEnv.getTypeUtils();
				
		for (ParameterElement additionalParameter: additionalParameters) {
			if (/*additionalParameter.isPropagated() &&*/ typeUtils.isSameType(typeUtils.toMutableType(parameter.asType()), additionalParameter.getType())) {
				return additionalParameter.getName();
			}
		}
		
		return parameter.getSimpleName().toString();
	}

	protected String getConstructorParameterName(MutableTypeMirror type) {
		TypeElement cachedConverterType = processingEnv.getElementUtils().getTypeElement(BasicCachedConverter.class.getCanonicalName());
		
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(cachedConverterType.getEnclosedElements());

		if (constructors.size() > 0) {
			ExecutableElement constructor = constructors.iterator().next();
			
			ParameterElement[] constructorAditionalParameters = parametersResolverProvider.getParameterResolver(UsageType.DEFINITION).getConstructorAditionalParameters();
			
			for (VariableElement parameter: constructor.getParameters()) {
				String name = getParameterName(parameter, constructorAditionalParameters);
				
				if (processingEnv.getTypeUtils().implementsType(processingEnv.getTypeUtils().toMutableType(parameter.asType()),type)) {
					return name;
				}
			}
		}
		
		return null;
	}
}