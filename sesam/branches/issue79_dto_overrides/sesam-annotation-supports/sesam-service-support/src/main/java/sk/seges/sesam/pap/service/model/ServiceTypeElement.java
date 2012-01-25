package sk.seges.sesam.pap.service.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ServiceTypeElement extends DelegateMutableDeclaredType {

	private final TypeElement service;
	private final MutableProcessingEnvironment processingEnv;
	
	private ServiceConverterTypeElement serviceConverter;
	
	public ServiceTypeElement(TypeElement service, MutableProcessingEnvironment processingEnv) {
		this.service = service;
		this.processingEnv = processingEnv;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(service.asType());
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
	
	public TypeElement asElement() {
		return service;
	}
	
	public ServiceConverterTypeElement getServiceConverter() {
		if (serviceConverter == null) {
			serviceConverter = new ServiceConverterTypeElement(this, processingEnv);
		}
		
		return serviceConverter;
	}
}