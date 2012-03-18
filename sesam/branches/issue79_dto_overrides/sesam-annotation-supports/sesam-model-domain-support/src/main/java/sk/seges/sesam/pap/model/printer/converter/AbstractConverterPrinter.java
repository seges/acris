package sk.seges.sesam.pap.model.printer.converter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;

public class AbstractConverterPrinter {

	protected final TransferObjectProcessingEnvironment processingEnv;

	protected AbstractConverterPrinter(TransferObjectProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected String getConstructorParameterName(MutableTypeMirror type) {
		TypeElement cachedConverterType = processingEnv.getElementUtils().getTypeElement(BasicCachedConverter.class.getCanonicalName());
		
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(cachedConverterType.getEnclosedElements());

		if (constructors.size() > 0) {
			ExecutableElement constructor = constructors.iterator().next();
			
			for (VariableElement parameter: constructor.getParameters()) {
				String name = parameter.getSimpleName().toString();
				
				if (processingEnv.getTypeUtils().implementsType(processingEnv.getTypeUtils().toMutableType(parameter.asType()),type)) {
					return name;
				}
			}
		}
		
		return null;
	}
}