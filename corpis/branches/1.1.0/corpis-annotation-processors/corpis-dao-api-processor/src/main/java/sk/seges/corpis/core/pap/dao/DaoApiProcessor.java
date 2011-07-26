package sk.seges.corpis.core.pap.dao;

import java.lang.reflect.Type;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.dao.ICrudDAO;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DaoApiProcessor extends AbstractConfigurableProcessor {

	static final String DAO_API_CLASS_SUFFIX = "Dao";
	static final String DAO_API_CLASS_PREFIX = "I";

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DaoApiProcessorConfigurer();
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
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_INTERFACES:
			return new Type[] {
					TypedClassBuilder.get(ICrudDAO.class, NamedType.THIS)
			};
		}
		return super.getOutputDefinition(type, typeElement);
	}
	
	@Override
	protected boolean isSupportedAnnotation(AnnotationMirror annotationMirror) {
		AnnotationValue annotationValueByReturnType = getAnnotationValueByReturnType(toTypeElement(Provider.class), toTypeElement(DataAccessObject.class), annotationMirror);
		
		if (annotationValueByReturnType == null) {
			return super.isSupportedAnnotation(annotationMirror);
		}
		return annotationValueByReturnType.getValue().toString().equals(Provider.INTERFACE.name());
	}
}