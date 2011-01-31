package sk.seges.corpis.core.pap.dao;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.corpis.core.pap.dao.subprocessor.CrudSubProcessor;
import sk.seges.corpis.core.pap.dao.subprocessor.EntityInstancerSubProcessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.core.pap.ImplementationProcessor;
import sk.seges.sesam.core.pap.model.InputClass.TypeParameter;
import sk.seges.sesam.core.pap.model.InputClass.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.InputClass.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.IEntityInstancer;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({HibernateDaoProcessor.CONFIG_FILE_LOCATION})
public class HibernateDaoProcessor extends ImplementationProcessor {
	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/dao-hibernate.properties";

	static final String DAO_API_CLASS_SUFFIX = "Dao";
	static final String DAO_API_CLASS_PREFIX = "Hibernate";
	
	private TypeElement interfaceElement;
	
	enum DaoConfigurationType implements ConfigurationType {
		PACKAGE_VALIDATOR_PROVIDER("packageValidatorProvider", false, ElementKind.CLASS);
		
		private String key;
		private boolean aditive;
		private ElementKind kind;
		
		DaoConfigurationType(String key, boolean aditive, ElementKind kind) {
			this.key = key;
			this.aditive = aditive;
			this.kind = kind;
		}
		
		@Override
		public ElementKind getKind() {
			return kind;
		}
		
		@Override
		public boolean isAditive() {
			return aditive;
		}
		
		@Override
		public String getKey() {
			return key;
		}
	}
	
	public HibernateDaoProcessor() {
		registerSubProcessor(IEntityInstancer.class, new EntityInstancerSubProcessor());
		registerSubProcessor(ICrudDAO.class, new CrudSubProcessor());
	}

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		initSubProcessors(pe);
	}
	
	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}
	
	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}

	public static NamedType getOutputClass(MutableType inputClass, PackageValidatorProvider packageValidatorProvider) {
		PackageValidator packageValidator = packageValidatorProvider.get(inputClass);
		packageValidator.moveTo(LocationType.SERVER).moveTo(LayerType.DAO);
		
		if (packageValidator.isValid()) {
			packageValidator.moveTo(ImplementationType.HIBERNATE);
		} else {
			packageValidator.setType(LayerType.DAO.getName() + "." + ImplementationType.HIBERNATE.getName());
		}

		return inputClass.changePackage(packageValidator.toString())
										  .addClassPrefix(DAO_API_CLASS_PREFIX)
										  .addClassSufix(DAO_API_CLASS_SUFFIX)
										  .addType(TypeParameterBuilder.get("T", NamedType.THIS));
	}
	
	@Override
	protected NamedType[] getTargetClassNames(MutableType inputClass) {
		return new NamedType[] { 
			getOutputClass(inputClass, getPackageValidatorProvider()) 
		};
	}


	@Override
	protected TypeElement getInterfaceElement(Element element, RoundEnvironment roundEnv) {
		if (interfaceElement != null) {
			NamedType interfaceDao = DaoApiProcessor.getOutputClass(getNameTypes().toType(interfaceElement), getPackageValidatorProvider());
			return processingEnv.getElementUtils().getTypeElement(interfaceDao.getCanonicalName());
		}
		
		return null;
	}

	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		if (element.getKind().equals(ElementKind.CLASS)) {
			TypeElement typeElement = (TypeElement)element;
			interfaceElement = getInterfaceClass(typeElement);
		}
		return super.processElement(element, roundEnv);
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {

		if (interfaceElement != null) {
			super.processElement(element, outputName, roundEnv, pw);
		} else {
			new CrudSubProcessor().process(pw, outputName, element, element);
		}
	}
	
	protected AnnotatedElement[] getInterfaceAnnotations() {
		return new AnnotatedElement[] {
		};
	}
	
	protected boolean isDomainInterface(TypeElement typeElement) {
		Annotation annotation = hasAnnotation(typeElement);
		if (annotation == null) {
			return false;
		}
		
		if (annotation instanceof DataAccessObject) {
			return ((DataAccessObject)annotation).provider().equals(Provider.INTERFACE);
		}

		return true;
	}
	
	@Override
	protected Type[] getImports() {
		List<Type> imports = new ArrayList<Type>();
		
		addUnique(imports, getSubProcessorImports());

		if (interfaceElement != null) {
			addUnique(imports, getNameTypes().toType(interfaceElement));
			addUnique(imports, getNameTypes().toType(getInterfaceElement(interfaceElement, null)));
		}

		addUnique(imports, NamedType.THIS);

		return imports.toArray(new Type[] {});
	}
	
	protected TypeElement getInterfaceClass(TypeElement typeElement) {

		if (isDomainInterface(typeElement)) {
			return typeElement;
		}
		
		if (typeElement.getInterfaces() != null) {
			for (TypeMirror typeMirror: typeElement.getInterfaces()) {
				Element interfaceElement = processingEnv.getTypeUtils().asElement(typeMirror);
				if (interfaceElement.getKind().equals(ElementKind.INTERFACE)) {
					TypeElement interfaceTypeElement = (TypeElement)interfaceElement;
					TypeElement interfaceClassElement = getInterfaceClass(interfaceTypeElement);
					
					if (interfaceClassElement != null) {
						return interfaceClassElement;
					}
				}
			}
		}
		
		TypeMirror superClass = typeElement.getSuperclass();
		if (superClass == null) {
			return null;
		}
		Element superClassElement = processingEnv.getTypeUtils().asElement(superClass);
		if (superClassElement == null) {
			return null;
		}
		if (superClassElement.getKind().equals(ElementKind.CLASS)) {
			typeElement = (TypeElement)superClassElement;
			
			TypeElement interfaceClassElement = getInterfaceClass(typeElement);
			
			if (interfaceClassElement != null) {
				return interfaceClassElement;
			}
		}
		
		return null;
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
				
		switch (type) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] {
				DataAccessObject.class
			};
		case OUTPUT_INTERFACES:
			if (interfaceElement == null) {
				return new Type[] {
				};
			}
			
			TypeElement interfaceTypeElement = getInterfaceElement(typeElement, null);
			
			if (interfaceTypeElement.getTypeParameters() == null || interfaceTypeElement.getTypeParameters().size() == 0) {
				return new Type[] {
					getNameTypes().toType(interfaceTypeElement)
				};
			}
			
			TypeParameter[] typeParameters = new TypeParameter[interfaceTypeElement.getTypeParameters().size()];
			int i = 0;
			for (TypeParameterElement typeParameterElement: interfaceTypeElement.getTypeParameters()) {
				if (processingEnv.getTypeUtils().isAssignable(typeParameterElement.asType(), interfaceElement.asType())) {
					typeParameters[i++] = TypeParameterBuilder.get(getNameTypes().toType(interfaceElement));
				} else {
					typeParameters[i++] = TypeParameterBuilder.get(getNameTypes().toType(typeParameterElement));
				}
			}
			return new Type[] {
				TypedClassBuilder.get(getNameTypes().toType(interfaceTypeElement), typeParameters)
			};
		case OUTPUT_SUPERCLASS:
			if (interfaceElement != null) {
				return new Type[] {
						TypedClassBuilder.get(AbstractHibernateCRUD.class, getNameTypes().toType(interfaceElement))
				};
			}
			return new Type[] {
					TypedClassBuilder.get(AbstractHibernateCRUD.class, NamedType.THIS)
			};
		}
		return super.getConfigurationTypes(type, typeElement);
	}
	
	@Override
	protected boolean isSupportedAnnotation(AnnotationMirror annotationMirror) {
		AnnotationValue annotationValueByReturnType = getAnnotationValueByReturnType(toTypeElement(Provider.class), toTypeElement(DataAccessObject.class), annotationMirror);
		
		if (annotationValueByReturnType == null) {
			return super.isSupportedAnnotation(annotationMirror);
		}
		return annotationValueByReturnType.getValue().toString().equals(Provider.HIBERNATE.name());
	}
}