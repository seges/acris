package sk.seges.sesam.pap.model.model;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping.NotDefinedConverter;
import sk.seges.sesam.pap.model.annotation.TransferObjectMappings;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

/**
 * Helper class that provides configuration parameters for the {@link TypeElement}. Type has to have
 * {@link TransferObjectMapping} or {@link TransferObjectMappings} annotation in order to read the configuration. If the
 * type does not holds any of these annotations it is considered as non configuration element and helper class would no
 * provide relevant results
 * 
 * @author Peter Simun (simun@seges.sk)
 */
public class TransferObjectMappingAccessor extends AnnotationAccessor {

	enum DtoParameterType {
		DTO(0), DOMAIN(1);

		private int index;

		private DtoParameterType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
	}
	
	private Set<TransferObjectMapping> mappings = new HashSet<TransferObjectMapping>();

	private TransferObjectMapping referenceMapping;
	private Element configurationHolderElement;
	
	/**
	 * TypeElement holds {@link TransferObjectMapping} or {@link TransferObjectMappings} annotation
	 */
	public TransferObjectMappingAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.configurationHolderElement = element;
		{
			TransferObjectMapping mapping = element.getAnnotation(TransferObjectMapping.class);
			if (mapping != null) {
				this.mappings.add(mapping);
			}
		}

		{
			TransferObjectMappings mappings = element.getAnnotation(TransferObjectMappings.class);
			if (mappings != null) {
				for (TransferObjectMapping mapping : mappings.value()) {
					this.mappings.add(mapping);
				}
			}
		}
		
		if (mappings.size() == 1) {
			referenceMapping = mappings.iterator().next();
		}
	}

	public boolean isValid() {
		return mappings.size() > 0;
	}
	
	TransferObjectMapping getReferenceMapping() {
		return referenceMapping;
	}
	
	Element getConfigurationHolderElement() {
		return configurationHolderElement;
	}
	
	void setReferenceMapping(TransferObjectMapping referenceMapping) {
		this.referenceMapping = referenceMapping;
	}
	
	public TypeElement getEvaluatedDomainType() {
		if (referenceMapping == null) {
			return null;
		}

		return getEvaluatedDomainType(referenceMapping);
	}
	
	public TransferObjectMapping getMappingForDto(MutableDeclaredType dtoType) {
		for (TransferObjectMapping mapping : mappings) {
			TypeElement annotationDtoType = getDto(mapping);
			MutableDeclaredType mutableAnnotationDtoType = null;

			if (annotationDtoType != null) {
				mutableAnnotationDtoType = processingEnv.getTypeUtils().toMutableType((DeclaredType)annotationDtoType.asType());
				if (processingEnv.getTypeUtils().isSameType(mutableAnnotationDtoType, dtoType)) {
					return mapping;
				}
			}

			annotationDtoType = getDtoInterface(mapping);

			if (annotationDtoType != null) {
				mutableAnnotationDtoType = processingEnv.getTypeUtils().toMutableType((DeclaredType)annotationDtoType.asType());
				if (processingEnv.getTypeUtils().implementsType(dtoType, mutableAnnotationDtoType)) {
					return mapping;
				}
			}
		}
		
		return null;
	}

	public TransferObjectMapping getMappingForDomain(MutableDeclaredType domainType) {
		for (TransferObjectMapping mapping : mappings) {
			TypeElement annotationDomainType = getDomain(mapping);
			MutableDeclaredType mutableAnnotationDomainType = null;
			
			if (annotationDomainType != null) {
				mutableAnnotationDomainType = processingEnv.getTypeUtils().toMutableType((DeclaredType)annotationDomainType.asType());
				if (processingEnv.getTypeUtils().isSameType(mutableAnnotationDomainType, domainType)) {
					return mapping;
				}
			}

			annotationDomainType = getDomainInterface(mapping);
			if (annotationDomainType != null) {
				mutableAnnotationDomainType = processingEnv.getTypeUtils().toMutableType((DeclaredType)annotationDomainType.asType());
				if (processingEnv.getTypeUtils().implementsType(domainType, mutableAnnotationDomainType)) {
					return mapping;
				}
			}
		}
		
		return null;
	}
	
	public TransferObjectMapping getMappingForDomain(DeclaredType domainType) {
		for (TransferObjectMapping mapping : mappings) {
			TypeElement annotationDomainType = getDomain(mapping);

			if (annotationDomainType != null) {
				if (processingEnv.getTypeUtils().erasure(annotationDomainType.asType()).equals(
					processingEnv.getTypeUtils().erasure(domainType))) {
					return mapping;
				}
			}

			annotationDomainType = getDomainInterface(mapping);

			if (annotationDomainType != null && ProcessorUtils.implementsType(domainType, annotationDomainType.asType())) {
				return mapping;
			}
		}
		
		return null;
	}

	protected TypeElement getEvaluatedDomainType(TransferObjectMapping mapping) {

		// getting the domain definition
		TypeElement domain = NullCheck.checkNull(getDomain(mapping));
		if (domain != null) {
			return domain;
		}

		// getting the converter definition
		TypeElement converter = getConverter(mapping);

		if (converter != null) {
			TypeElement domainElement = getDomainClassFromConverter(converter);

			if (domainElement != null) {
				return domainElement;
			} 
			
//			processingEnv.getMessager().printMessage(Kind.ERROR,
//						"[ERROR] Invalid converter specified in the mapping. Unable to find domain class type. "
//								+ converter.toString(), element);
		}

		// getting the configuration definition
		TypeElement configuration = getConfiguration(mapping);
		if (configuration != null) {
			
			TypeElement domainType = new TransferObjectMappingAccessor(configuration, processingEnv).getEvaluatedDomainType();
			
//			TypeElement domainClassType = new TransferObjectConfiguration(configuration, processingEnv).getDomain();

			if (domainType != null) {
				return domainType;
			}
			
//			processingEnv.getMessager().printMessage(Kind.ERROR,
//					"[ERROR] Invalid configuration specified in the mapping. Unable to find domain class type from configuration type "
//							+ configuration.toString(), element);
		}

		return null;
	}

	/**
	 * Returns domain object defined in the {@link TransferObjectMapping} annotation. If there are more the one
	 * {@link TransferObjectMapping} annotations null type is returned. If there is any DTO class specified in the
	 * {@link TransferObjectMapping} method will return null result. Method is used to get domain class only in the
	 * configurations that has no DTO specified.
	 */
	public TypeElement getDomain() {
		if (referenceMapping == null) {
			return null;
		}

		return getDomain(referenceMapping);
	}

	public TypeElement getDtoInterface() {
		if (referenceMapping == null) {
			return null;
		}

		return getDtoInterface(referenceMapping);
	}
	
	public TypeElement getDomainInterface() {
		if (referenceMapping == null) {
			return null;
		}

		return getDomainInterface(referenceMapping);
	}

	public TypeElement getConfiguration() {
		if (referenceMapping == null) {
			return null;
		}

		return getConfiguration(referenceMapping);
	}

	public TypeElement getConverter() {
		if (referenceMapping == null) {
			return null;
		}

		return getConverter(referenceMapping);
	}

	public TypeElement getDto() {
		if (referenceMapping == null) {
			return null;
		}

		return getDto(referenceMapping);
	}

	TypeElement getDomain(TransferObjectMapping mapping) {
		TypeElement domainType = NullCheck.checkNull(AnnotationClassPropertyHarvester.getTypeOfClassProperty(mapping,
				new AnnotationClassProperty<TransferObjectMapping>() {

					@Override
					public Class<?> getClassProperty(TransferObjectMapping annotation) {
						return annotation.domainClass();
					}
				}, processingEnv));
		
		if (domainType != null) {
			return domainType;
		}
		
		String domainClassName = NullCheck.checkNull(mapping.domainClassName());
		
		if (domainClassName != null) {
			return processingEnv.getElementUtils().getTypeElement(domainClassName);
		}
		
		return null;
	}

	TypeElement getDomainInterface(TransferObjectMapping mapping) {
		TypeElement domainInterfaceType = NullCheck.checkNull(AnnotationClassPropertyHarvester.getTypeOfClassProperty(mapping,
				new AnnotationClassProperty<TransferObjectMapping>() {

					@Override
					public Class<?> getClassProperty(TransferObjectMapping annotation) {
						return annotation.domainInterface();
					}
				}));
		
		if (domainInterfaceType != null) {
			return domainInterfaceType;
		}
		
		String domainInterfaceName = NullCheck.checkNull(mapping.domainInterfaceName());
		
		if (domainInterfaceName != null) {
			return processingEnv.getElementUtils().getTypeElement(domainInterfaceName);
		}
		
		return null;
	}

	TypeElement getDtoInterface(TransferObjectMapping mapping) {
		TypeElement dtoInterfaceType = NullCheck.checkNull(AnnotationClassPropertyHarvester.getTypeOfClassProperty(mapping,
				new AnnotationClassProperty<TransferObjectMapping>() {

					@Override
					public Class<?> getClassProperty(TransferObjectMapping annotation) {
						return annotation.dtoInterface();
					}
				}));
		
		if (dtoInterfaceType != null) {
			return dtoInterfaceType;
		}
		
		String dtoInterfaceName = NullCheck.checkNull(mapping.dtoInterfaceName());
		
		if (dtoInterfaceName != null) {
			return processingEnv.getElementUtils().getTypeElement(dtoInterfaceName);
		}
		
		return null;
	}

	TypeElement getDto(TransferObjectMapping mapping) {
		TypeElement dtoElement = NullCheck.checkNull(AnnotationClassPropertyHarvester.getTypeOfClassProperty(mapping,
				new AnnotationClassProperty<TransferObjectMapping>() {

					@Override
					public Class<?> getClassProperty(TransferObjectMapping annotation) {
						return annotation.dtoClass();
					}
				}));
		
		if (dtoElement != null) {
			return dtoElement;
		}
		
		String dtoClassName = NullCheck.checkNull(mapping.dtoClassName());
		
		if (dtoClassName != null) {
			return processingEnv.getElementUtils().getTypeElement(dtoClassName);
		}
		
		TypeElement delegatedConfiguration = getConfiguration();
		
		if (delegatedConfiguration != null) {
			return new TransferObjectMappingAccessor(delegatedConfiguration, processingEnv).getDto();
		}

		return null;
	}

	TypeElement getConverter(TransferObjectMapping mapping) {
		TypeElement converter = NullCheck.checkNull(AnnotationClassPropertyHarvester.getTypeOfClassProperty(mapping,
				new AnnotationClassProperty<TransferObjectMapping>() {

					@Override
					public Class<?> getClassProperty(TransferObjectMapping annotation) {
						return annotation.converter();
					}
				}), NotDefinedConverter.class);
		
		if (converter != null) {
			return converter;
		}
		
		String converterClassName = NullCheck.checkNull(mapping.converterClassName());
		
		if (converterClassName != null) {
			return processingEnv.getElementUtils().getTypeElement(converterClassName);
		}
		
		TypeElement delegatedConfiguration = getConfiguration();
		
		if (delegatedConfiguration != null) {
			return new TransferObjectMappingAccessor(delegatedConfiguration, processingEnv).getConverter();
		}
		
		return null;
	}

	TypeElement getConfiguration(TransferObjectMapping mapping) {
		TypeElement configurationClass = NullCheck.checkNull(AnnotationClassPropertyHarvester.getTypeOfClassProperty(mapping,
				new AnnotationClassProperty<TransferObjectMapping>() {

					@Override
					public Class<?> getClassProperty(TransferObjectMapping annotation) {
						return annotation.configuration();
					}
				}));
		
		if (configurationClass != null) {
			return configurationClass;
		}
		
		String configurationClassName = NullCheck.checkNull(mapping.configurationClassName());
		
		if (configurationClassName != null) {
			return processingEnv.getElementUtils().getTypeElement(configurationClassName);
		}
		
		return null;
	}

	protected TypeElement getDomainClassFromConverter(TypeElement converterType) {
		TypeElement domainClass = erasure(converterType, DtoConverter.class, DtoParameterType.DOMAIN.getIndex());
//		if (domainClass == null) {
//			processingEnv.getMessager().printMessage(
//					Kind.ERROR,
//					"Invalid converter specified, unable to extract domain class. Please make sure that"
//							+ " converter implements " + DtoConverter.class.getCanonicalName()
//							+ " interface with properly specified type variables.", element);
//		}
		return domainClass;
	}

	protected TypeElement erasure(TypeElement rootElement, Class<?> parameterOwnerClass, int parameterIndex) {
		TypeElement domainTypeElement = null;

		TypeElement parameterHolderType = processingEnv.getElementUtils().getTypeElement(
				parameterOwnerClass.getCanonicalName());

		while (domainTypeElement == null) {

			TypeElement owner = null;
			TypeElement holderElement = null;

			if (parameterHolderType.getKind().equals(ElementKind.INTERFACE)) {
				owner = getTypeThatImplements(rootElement, parameterHolderType);
			} else if (parameterHolderType.getKind().equals(ElementKind.CLASS)) {
				owner = getTypeThatExtends(rootElement, parameterHolderType);
			}

			if (owner == null) {
//				processingEnv.getMessager().printMessage(
//						Kind.ERROR,
//						"Invalid converter specified, unable to extract domain class. Please make sure that"
//								+ " converter implements " + DtoConverter.class.getCanonicalName()
//								+ " interface with properly specified type variables.", element);
				return null;
			}

			if (parameterHolderType.getKind().equals(ElementKind.INTERFACE)) {
				holderElement = getImplementedInterface(owner, parameterHolderType);
			} else if (parameterHolderType.getKind().equals(ElementKind.CLASS)) {
				holderElement = (TypeElement) ((DeclaredType) owner.getSuperclass()).asElement();
			}

			TypeParameterElement typeParameterElement = holderElement.getTypeParameters().get(parameterIndex);

			if (typeParameterElement.asType().getKind().equals(TypeKind.DECLARED)) {
				return (TypeElement) ((DeclaredType) typeParameterElement.asType()).asElement();
			} else if (typeParameterElement.asType().getKind().equals(TypeKind.TYPEVAR)) {
				parameterHolderType = owner;
				parameterIndex = getPrameterIndex(owner, ((TypeVariable) typeParameterElement.asType()).asElement()
						.getSimpleName().toString());
			}
		}

		return null;
	}

	protected int getPrameterIndex(TypeElement owner, String name) {
		int index = 0;
		for (TypeParameterElement ownerParameter : owner.getTypeParameters()) {
			if (ownerParameter.getSimpleName().toString().equals(name)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	protected TypeElement getImplementedInterface(TypeElement typeElement, TypeElement interfaceElement) {
		for (TypeMirror implementedInterfaceType : typeElement.getInterfaces()) {
			if (implementedInterfaceType.getKind().equals(TypeKind.DECLARED)) {
				DeclaredType interfaceDeclaredType = (DeclaredType) implementedInterfaceType;
				if (interfaceDeclaredType.asElement().getSimpleName().toString()
						.equals(interfaceElement.getSimpleName().toString())) {
					if (processingEnv.getElementUtils().getPackageOf(interfaceDeclaredType.asElement()).getQualifiedName().toString().equals(
						processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString())) {
						return (TypeElement) interfaceDeclaredType.asElement();
					}
				}
			}
		}

		return null;
	}

	protected TypeElement getTypeThatExtends(TypeElement rootElement, TypeElement type) {
		TypeMirror superclass = rootElement.getSuperclass();
		if (superclass != null && superclass.getKind().equals(TypeKind.DECLARED)) {

			TypeElement superclassType = (TypeElement) ((DeclaredType) superclass).asElement();

			if (type.getQualifiedName().toString().equals(superclassType.getQualifiedName().toString())) {
				return rootElement;
			}

			return getTypeThatExtends(superclassType, type);
		}
		return null;
	}

	protected TypeElement getTypeThatImplements(TypeElement rootElement, TypeElement interfaceElement) {
		for (TypeMirror implementedInterfaceType : rootElement.getInterfaces()) {
			if (implementedInterfaceType.getKind().equals(TypeKind.DECLARED)) {
				DeclaredType interfaceDeclaredType = (DeclaredType) implementedInterfaceType;
				if (interfaceDeclaredType.asElement().getSimpleName().toString()
						.equals(interfaceElement.getSimpleName().toString())) {
					if (processingEnv.getElementUtils().getPackageOf(interfaceDeclaredType.asElement()).getQualifiedName().toString().equals(
						processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString())) {
						return rootElement;
					}
				}
				TypeElement typeThatImplements = getTypeThatImplements((TypeElement) interfaceDeclaredType.asElement(),
						interfaceElement);
				if (typeThatImplements != null) {
					return typeThatImplements;
				}
			}
		}

		TypeMirror superclass = rootElement.getSuperclass();

		if (superclass != null && superclass.getKind().equals(TypeKind.DECLARED)) {
			TypeElement typeThatImplements = getTypeThatImplements((TypeElement) ((DeclaredType) superclass).asElement(), interfaceElement);

			if (typeThatImplements != null) {
				return typeThatImplements;
			}
		}

		return null;
	}
}