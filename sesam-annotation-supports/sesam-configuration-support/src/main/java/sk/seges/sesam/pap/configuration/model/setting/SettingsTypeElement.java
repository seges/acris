package sk.seges.sesam.pap.configuration.model.setting;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class SettingsTypeElement extends DelegateMutableDeclaredType {

	public static final String SUFFIX = "Settings";

	private final MutableDeclaredType annotationNamedType;
	private DeclaredType annotationType;
	private final MutableProcessingEnvironment processingEnv;
	
	public SettingsTypeElement(AnnotationMirror configurationAnnotation, MutableProcessingEnvironment processingEnv) {
		this.annotationNamedType = toOutputType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(configurationAnnotation.getAnnotationType()));
		this.annotationType = configurationAnnotation.getAnnotationType();
		this.processingEnv = processingEnv;
	}

	public SettingsTypeElement(DeclaredType annotationType, MutableProcessingEnvironment processingEnv) {
		this.annotationNamedType = toOutputType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(annotationType));
		this.annotationType = annotationType;
		this.processingEnv = processingEnv;
	}

	public SettingsTypeElement(MutableDeclaredType annotationMutableType, MutableProcessingEnvironment processingEnv) {
		this.annotationNamedType = toOutputType(annotationMutableType);
		this.annotationType = (DeclaredType)processingEnv.getTypeUtils().fromMutableType(annotationMutableType);
		this.processingEnv = processingEnv;
	}

	private static MutableDeclaredType toOutputType(MutableDeclaredType type) {
		if (type.getEnclosedClass() != null) {
			return type.clone().setEnclosedClass(getOutputName(type.getEnclosedClass()));
		}
		
		return type;
	}
	
	private static MutableDeclaredType getOutputName(MutableDeclaredType type) {
		return type.clone().addClassSufix(SUFFIX);
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

	public AnnotationMirror getAnnotationMirrorForElement(TypeElement typeElement) {
		for (AnnotationMirror annotationMirror: typeElement.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().asElement().asType().equals(annotationType)) {
				return annotationMirror;
			}
		}
		
		return null;
	}
	
	public boolean exists() {
		return processingEnv.getElementUtils().getTypeElement(getCanonicalName()) != null;
	}
}