package sk.seges.sesam.pap.configuration.model.parameter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;

public class ModelType extends DelegateMutableDeclaredType {

	public static final String SUFFIX = "Model";

	private final MutableDeclaredType annotationNamedType;
	
	public ModelType(AnnotationMirror configurationAnnotation, MutableProcessingEnvironment processingEnv) {
		this.annotationNamedType = toOutputType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(configurationAnnotation.getAnnotationType()));
	}

	public ModelType(DeclaredType annotationType, MutableProcessingEnvironment processingEnv) {
		this.annotationNamedType = toOutputType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(annotationType));
	}

	public ModelType(MutableDeclaredType annotationMutableType) {
		this.annotationNamedType = toOutputType(annotationMutableType);
	}

	private static MutableDeclaredType toOutputType(MutableDeclaredType type) {
		if (type.getEnclosedClass() != null) {
			return type.clone().setEnclosedClass(getOutputName(type.getEnclosedClass()));
		}
		
		return type;
	}
	
	private static MutableDeclaredType getOutputName(MutableDeclaredType type) {
		return type.clone().changePackage(new DefaultPackageValidatorProvider().get(type).moveTo(LayerType.MODEL)).addClassSufix(SUFFIX);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		MutableDeclaredType outputName = getOutputName(annotationNamedType);
		if (annotationNamedType.getEnclosedClass() != null) {
			outputName = outputName.setEnclosedClass(annotationNamedType.getEnclosedClass().clone());
		}
		outputName.setKind(MutableTypeKind.CLASS);
		return outputName;
	}
}
