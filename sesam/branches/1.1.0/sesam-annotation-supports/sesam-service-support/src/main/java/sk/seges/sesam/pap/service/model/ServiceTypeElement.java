package sk.seges.sesam.pap.service.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.DelegateImmutableType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;

public class ServiceTypeElement extends DelegateImmutableType {

	private final TypeElement service;
	private final NameTypesUtils namedTypesUtils;
	private final ProcessingEnvironment processingEnv;
	
	public ServiceTypeElement(TypeElement service, ProcessingEnvironment processingEnv) {
		this.service = service;
		this.namedTypesUtils = new NameTypesUtils(processingEnv);
		this.processingEnv = processingEnv;
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		return namedTypesUtils.toImmutableType(service);
	}

	public List<LocalServiceTypeElement> getLocalServiceInterfaces() {
		return getLocalServiceInterfaces(service);
	}
	
	private List<LocalServiceTypeElement> getLocalServiceInterfaces(TypeElement element) {
		List<LocalServiceTypeElement> result = new ArrayList<LocalServiceTypeElement>();
		List<? extends TypeMirror> interfaces = element.getInterfaces();
		
		for (TypeMirror localServiceInterface: interfaces) {
			if (localServiceInterface.getKind().equals(TypeKind.DECLARED)) {
				TypeElement interfaceElement = (TypeElement)((DeclaredType)localServiceInterface).asElement();
				
				LocalServiceTypeElement localServiceTypeElement = new LocalServiceTypeElement(interfaceElement, processingEnv);
				
				if (localServiceTypeElement.isValid()) {
					result.add(localServiceTypeElement);
				}
				result.addAll(getLocalServiceInterfaces(interfaceElement));
			}
		}


		if (element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			TypeElement superclassElement = (TypeElement)((DeclaredType)element.getSuperclass()).asElement();
			result.addAll(getLocalServiceInterfaces(superclassElement));
		}
		
		return result;
	}
}
