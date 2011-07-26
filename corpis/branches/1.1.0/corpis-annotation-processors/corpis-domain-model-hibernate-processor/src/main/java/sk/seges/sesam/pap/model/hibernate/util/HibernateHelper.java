package sk.seges.sesam.pap.model.hibernate.util;

import java.lang.annotation.Annotation;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Embeddable;

import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.model.hibernate.MappingType;

public class HibernateHelper {

	public TypeMirror getTargetEntityType(ExecutableElement method) {
		for (final MappingType mappingType: MappingType.values()) {
			Annotation annotation = method.getAnnotation(mappingType.getAnnotationClass());
			
			if (annotation != null) {
				TypeElement targetEntity = AnnotationClassPropertyHarvester.getTypeOfClassProperty(annotation, new AnnotationClassProperty<Annotation>() {

					@Override
					public Class<?> getClassProperty(Annotation annotation) {
						return mappingType.getTargetEntityFromAnnotation(annotation);
					}
				});
				
				if (targetEntity != null) {
					return targetEntity.asType();
				}
			}
		}
		
		return method.getReturnType();
	}

	public boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return domainElement.getAnnotation(Embeddable.class) == null;
	}

}
