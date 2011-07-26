package sk.seges.sesam.pap.model.hibernate.util;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Embeddable;

import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.hibernate.MappingType;

public class HibernateHelper {

	private TypeMirror replaceParameterType(TypeMirror type, TypeMirror replacement, ProcessingEnvironment processingEnv) {
		TypeElement collectionElement = processingEnv.getElementUtils().getTypeElement(Collection.class.getCanonicalName());
		
		if (ProcessorUtils.implementsType(type, collectionElement.asType())) {
			return processingEnv.getTypeUtils().getDeclaredType(collectionElement, replacement);	
		}
		
		return replacement;
	}
	
	public TypeMirror getTargetEntityType(ExecutableElement method, ProcessingEnvironment processingEnv) {
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
					return replaceParameterType(method.getReturnType(), targetEntity.asType(), processingEnv);
				}
			}
		}
		
		return method.getReturnType();
	}

	public boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return domainElement.getAnnotation(Embeddable.class) == null;
	}

}
