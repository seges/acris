package sk.seges.sesam.pap.model.hibernate.resolver;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.model.hibernate.MappingType;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class HibernateEntityResolver implements EntityResolver {

	private ProcessingEnvironment processingEnv;
	private MethodHelper methodHelper;
	
	public HibernateEntityResolver(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.methodHelper = new MethodHelper(processingEnv, new NameTypesUtils(processingEnv));
	}
	
	@Override
	public TypeMirror getTargetEntityType(Element element) {
		switch (element.getKind()) {
		case METHOD:
			return getTargetEntityType((ExecutableElement)element);
		}

		TypeMirror targetEntityType =  getTargetEntityType(element, element.asType());

		if (targetEntityType != null) {
			return targetEntityType;
		}

		return element.asType();
	}

	private TypeMirror getTargetEntityType(ExecutableElement method) {
		TypeMirror targetEntityType = getTargetEntityType(method, method.getReturnType());
		
		if (targetEntityType != null) {
			return targetEntityType;
		}
		
		Element field = methodHelper.getField(method);

		if (field != null) {
			targetEntityType =  getTargetEntityType(field, field.asType());

			if (targetEntityType != null) {
				return targetEntityType;
			}
		}
		
		return method.getReturnType();
	}
	
	private TypeMirror replaceParameterType(TypeMirror type, TypeMirror replacement) {
		TypeElement collectionElement = processingEnv.getElementUtils().getTypeElement(Collection.class.getCanonicalName());
		
		if (ProcessorUtils.implementsType(type, collectionElement.asType())) {
			return processingEnv.getTypeUtils().getDeclaredType((TypeElement)((DeclaredType)type).asElement(), replacement);	
		}
		
		return replacement;
	}

	private TypeMirror getTargetEntityType(Element element, TypeMirror type) {
		for (final MappingType mappingType: MappingType.values()) {
			Annotation annotation = element.getAnnotation(mappingType.getAnnotationClass());
			
			if (annotation != null) {
				TypeElement targetEntity = AnnotationClassPropertyHarvester.getTypeOfClassProperty(annotation, new AnnotationClassProperty<Annotation>() {

					@Override
					public Class<?> getClassProperty(Annotation annotation) {
						return mappingType.getTargetEntityFromAnnotation(annotation);
					}
				});
				
				if (targetEntity != null) {
					return replaceParameterType(type, targetEntity.asType());
				}
			}
		}

		return null;
	}
}