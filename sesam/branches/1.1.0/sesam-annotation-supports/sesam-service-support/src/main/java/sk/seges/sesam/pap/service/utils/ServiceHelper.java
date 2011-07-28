package sk.seges.sesam.pap.service.utils;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.service.annotation.LocalService;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

public class ServiceHelper {

	/**
	 * Returns remote service interface (that one that uses DTO) for the local service interface (that uses domain classes)
	 */
	public Element getRemoteServiceInterface(Element localServiceInterfaceElement) {
		LocalServiceDefinition localServiceDefinition = localServiceInterfaceElement.getAnnotation(LocalServiceDefinition.class);

		if (localServiceDefinition == null) {
			return null;
		}
		
		return AnnotationClassPropertyHarvester.getTypeOfClassProperty(localServiceDefinition, new AnnotationClassProperty<LocalServiceDefinition>() {

			@Override
			public Class<?> getClassProperty(LocalServiceDefinition annotation) {
				return annotation.remoteService();
			}
		});
	}

	/**
	 * Returns local service interfaces implemented by service element specified as a parameter
	 */
	public Element[] getLocalServiceInterfaces(TypeElement serviceElement) {
		Set<Element> result = new HashSet<Element>();
		getLocalServiceInterfaces(serviceElement, result);
		return result.toArray(new Element[] {});
	}
	
	private void getLocalServiceInterfaces(TypeElement serviceElement, Set<Element> result) {

		LocalService annotation = serviceElement.getAnnotation(LocalService.class);
		
		if (annotation != null) {
			TypeElement typeOfClassProperty = AnnotationClassPropertyHarvester.getTypeOfClassProperty(annotation, new AnnotationClassProperty<LocalService>() {
	
				@Override
				public Class<?> getClassProperty(LocalService annotation) {
					return annotation.localInterace();
				}
			});
		
			if (typeOfClassProperty != null && typeOfClassProperty.getKind().equals(TypeKind.DECLARED)) {
				result.add(typeOfClassProperty);
			}
		}
		
		for (TypeMirror interfaceType: serviceElement.getInterfaces()) {
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				Element interfaceElement = (((DeclaredType)interfaceType).asElement());
				LocalServiceDefinition localServiceDefinition = interfaceElement.getAnnotation(LocalServiceDefinition.class);
				
				if (localServiceDefinition != null) {
					result.add(interfaceElement);
				}
			}
		}
		
		if (serviceElement.getSuperclass() != null && serviceElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			getLocalServiceInterfaces((TypeElement)((DeclaredType)serviceElement.getSuperclass()).asElement(), result);
		}
	}	
}