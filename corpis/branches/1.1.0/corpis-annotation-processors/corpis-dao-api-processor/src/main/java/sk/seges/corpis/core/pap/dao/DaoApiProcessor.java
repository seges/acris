package sk.seges.corpis.core.pap.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.InputClass.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.InputClass.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.dao.ICrudDAO;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({DaoApiProcessor.CONFIG_FILE_LOCATION})
public class DaoApiProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/dao-api.properties";

	static final String DAO_API_CLASS_SUFFIX = "Dao";
	static final String DAO_API_CLASS_PREFIX = "I";
	
	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}
	
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}
	
	@Override
	protected Type[] getImports() {
		return new Type[] {
			NamedType.THIS
		};
	}
	
	public static NamedType getOutputClass(MutableType inputClass, PackageValidatorProvider packageValidatorProvider) {
		PackageValidator packageValidator = packageValidatorProvider.get(inputClass);
		packageValidator.moveTo(LocationType.SERVER).moveTo(LayerType.DAO);
		
		if (packageValidator.isValid()) {
			packageValidator.moveTo(ImplementationType.API);
		} else {
			packageValidator.setType(LayerType.DAO.getName() + "." + ImplementationType.API.getName());
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
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] {
					DataAccessObject.class
			};
		case OUTPUT_INTERFACES:
			return new Type[] {
					TypedClassBuilder.get(ICrudDAO.class, NamedType.THIS)
			};
		}
		return super.getConfigurationTypes(type, typeElement);
	}
	
	@Override
	protected boolean isSupportedAnnotation(Annotation annotation) {
		if (annotation instanceof DataAccessObject) {
			return ((DataAccessObject)annotation).provider().equals(Provider.INTERFACE);
		}
		return super.isSupportedAnnotation(annotation);
	}
}