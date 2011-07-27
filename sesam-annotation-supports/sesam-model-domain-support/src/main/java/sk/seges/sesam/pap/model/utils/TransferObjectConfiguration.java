package sk.seges.sesam.pap.model.utils;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping.NotDefinedConverter;
import sk.seges.sesam.pap.model.annotation.TransferObjectMappings;
import sk.seges.sesam.shared.model.converter.DtoConverter;

/**
 * Helper class that provides configuration parameters for the {@link TypeElement}. Type has to have
 * {@link TransferObjectMapping} or {@link TransferObjectMappings} annotation in order to read the configuration. If the
 * type does not holds any of these annotations it is considered as non configuration element and helper class would no
 * provide relevant results
 * 
 * @author Peter Simun (simun@seges.sk)
 */
public class TransferObjectConfiguration {

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
	
	public enum DtoMappingType {
		DOMAIN {

			@Override
			public TypeElement get(TransferObjectConfiguration configuration) {
				return configuration.getDomainType();
			}

			@Override
			public TypeElement get(TransferObjectConfiguration configuration, TypeElement dtoElement) {
				return configuration.getDomainType(dtoElement);
			}
			
		}, CONVERTER {

			@Override
			public TypeElement get(TransferObjectConfiguration configuration) {
				return configuration.getConverter();
			}

			@Override
			public TypeElement get(TransferObjectConfiguration configuration, TypeElement dtoElement) {
				return configuration.getConverter(dtoElement);
			}
			
		}, DTO {

			@Override
			public TypeElement get(TransferObjectConfiguration configuration) {
				return configuration.getDto();
			}

			@Override
			public TypeElement get(TransferObjectConfiguration configuration, TypeElement dtoElement) {
				return dtoElement;
			}
			
		}, CONFIGURATION {

			@Override
			public TypeElement get(TransferObjectConfiguration configuration) {
				return configuration.getConfiguration();
			}

			@Override
			public TypeElement get(TransferObjectConfiguration configuration, TypeElement dtoElement) {
				return configuration.getConfiguration(dtoElement);
			}
			
		};
		
		public abstract TypeElement get(TransferObjectConfiguration configuration);
		public abstract TypeElement get(TransferObjectConfiguration configuration, TypeElement dtoElement);
	}

	private Element element;
	private Set<TransferObjectMapping> mappings = new HashSet<TransferObjectMapping>();

	private ProcessingEnvironment processingEnv;
	
	/**
	 * TypeElement holds {@link TransferObjectMapping} or {@link TransferObjectMappings} annotation
	 */
	public TransferObjectConfiguration(Element element, ProcessingEnvironment processingEnv) {
		this.element = element;
		this.processingEnv = processingEnv;
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
	}

	public TypeElement getDomainType() {
		if (mappings.size() != 1) {
			return null;
		}

		TransferObjectMapping mapping = mappings.iterator().next();

		return getDomainType(mapping);
	}
	
	/**
	 * Returns the domain pair for the DTO type. Domain representation is obtained in more ways:
	 * <ul>
	 * <li>reads domain class from DTO class (from {@link TransferObjectMapping} or {@link TransferObjectMappings}
	 * annotations</li>
	 * <li>reads domain class directly from the {@link TransferObjectMapping} or {@link TransferObjectMappings}</li>
	 * <li>reads domain class from the configuration defined in the {@link TransferObjectMapping} or
	 * {@link TransferObjectMappings}</li>
	 * </ul>
	 * 
	 * @param dtoElement
	 *            Data Transfer Object that should can be converted to/from a domain object
	 * 
	 * @return domain object mapping pair. DTO can be converted to this domain object or back.
	 */
	public TypeElement getDomainType(TypeElement dtoElement) {
		for (TransferObjectMapping mapping : mappings) {
			TypeElement annotationDtoType = getDto(mapping);
			
			if (annotationDtoType.equals(dtoElement)) {
				return getDomainType(dtoElement);
			}
		}
		return null;
	}

	public TypeElement getConverter(TypeElement dtoElement) {
		for (TransferObjectMapping mapping : mappings) {
			TypeElement annotationDtoType = getDto(mapping);
			
			if (annotationDtoType.equals(dtoElement)) {
				return getConverter(dtoElement);
			}
		}
		return null;
	}

	public TypeElement getConfiguration(TypeElement dtoElement) {
		for (TransferObjectMapping mapping : mappings) {
			TypeElement annotationDtoType = getDto(mapping);
			
			if (annotationDtoType.equals(dtoElement)) {
				return getConfiguration(dtoElement);
			}
		}
		return null;
	}

	protected TypeElement getDomainType(TransferObjectMapping mapping) {

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
			
			processingEnv.getMessager().printMessage(Kind.ERROR,
						"[ERROR] Invalid converter specified in the mapping. Unable to find domain class type. "
								+ converter.toString(), element);
		}

		// getting the configuration definition
		TypeElement configuration = getConfiguration(mapping);
		if (configuration != null) {
			TypeElement domainClassType = new TransferObjectConfiguration(configuration, processingEnv).getDomain();

			if (domainClassType != null) {
				return domainClassType;
			} else {
				processingEnv.getMessager().printMessage(
						Kind.ERROR,
						"[ERROR] Invalid configuration specified in the mapping. Unable to find domain class type from configuration type "
								+ configuration.toString(), element);
			}
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
		if (mappings.size() != 1) {
			return null;
		}

		return getDomain(mappings.iterator().next());
	}

	public TypeElement getConfiguration() {
		if (mappings.size() != 1) {
			return null;
		}

		return getConfiguration(mappings.iterator().next());
	}

	public TypeElement getConverter() {
		if (mappings.size() != 1) {
			return null;
		}

		return getConverter(mappings.iterator().next());
	}

	public TypeElement getDto() {
		if (mappings.size() != 1) {
			return null;
		}

		return getDto(mappings.iterator().next());
	}

	protected TypeElement getDomain(TransferObjectMapping mapping) {
		TypeElement domainType = NullCheck.checkNull(AnnotationClassPropertyHarvester.getTypeOfClassProperty(mapping,
				new AnnotationClassProperty<TransferObjectMapping>() {

					@Override
					public Class<?> getClassProperty(TransferObjectMapping annotation) {
						return annotation.domainClass();
					}
				}));
		
		if (domainType != null) {
			return domainType;
		}
		
		String domainClassName = NullCheck.checkNull(mapping.domainClassName());
		
		if (domainClassName != null) {
			return processingEnv.getElementUtils().getTypeElement(domainClassName);
		}
		
		return null;
	}

	protected TypeElement getDto(TransferObjectMapping mapping) {
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
		
		return null;
	}

	protected TypeElement getConverter(TransferObjectMapping mapping) {
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
		
		return null;
	}

	protected TypeElement getConfiguration(TransferObjectMapping mapping) {
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
		if (domainClass == null) {
			processingEnv.getMessager().printMessage(
					Kind.ERROR,
					"Invalid converter specified, unable to extract domain class. Please make sure that"
							+ " converter implements " + DtoConverter.class.getCanonicalName()
							+ " interface with properly specified type variables.", element);
		}
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
				holderElement = getImplementedInterface(owner, parameterHolderType);
			} else if (parameterHolderType.getKind().equals(ElementKind.CLASS)) {
				owner = getTypeThatExtends(rootElement, parameterHolderType);
				holderElement = (TypeElement) (DeclaredType) owner.getSuperclass();
			}

			if (owner == null) {
				processingEnv.getMessager().printMessage(
						Kind.ERROR,
						"Invalid converter specified, unable to extract domain class. Please make sure that"
								+ " converter implements " + DtoConverter.class.getCanonicalName()
								+ " interface with properly specified type variables.", element);
				return null;
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