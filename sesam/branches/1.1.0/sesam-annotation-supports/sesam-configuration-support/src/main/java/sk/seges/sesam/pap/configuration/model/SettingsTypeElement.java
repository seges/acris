package sk.seges.sesam.pap.configuration.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.DelegateImmutableType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;

public class SettingsTypeElement extends DelegateImmutableType {

	public static final String SUFFIX = "Settings";

	private final ImmutableType annotationNamedType;
	private final NameTypeUtils nameTypeUtils;
	private DeclaredType annotationType;
	private final ProcessingEnvironment processingEnv;
	
	public SettingsTypeElement(AnnotationMirror configurationAnnotation, ProcessingEnvironment processingEnv) {
		this.nameTypeUtils = new NameTypeUtils(processingEnv);
		this.annotationNamedType = toOutputType(nameTypeUtils.toImmutableType(configurationAnnotation.getAnnotationType()));
		this.annotationType = configurationAnnotation.getAnnotationType();
		this.processingEnv = processingEnv;
	}

	public SettingsTypeElement(DeclaredType annotationType, ProcessingEnvironment processingEnv) {
		this.nameTypeUtils = new NameTypeUtils(processingEnv);
		this.annotationNamedType = toOutputType(nameTypeUtils.toImmutableType(annotationType));
		this.annotationType = annotationType;
		this.processingEnv = processingEnv;
	}

	public SettingsTypeElement(ImmutableType annotationNamedType, ProcessingEnvironment processingEnv) {
		this.nameTypeUtils = new NameTypeUtils(processingEnv);
		this.annotationNamedType = toOutputType(annotationNamedType);
		this.annotationType = (DeclaredType)nameTypeUtils.fromType(annotationNamedType);
		this.processingEnv = processingEnv;
	}

	private static ImmutableType toOutputType(ImmutableType type) {
		if (type.getEnclosedClass() != null) {
			type = type.setEnclosedClass(getOutputName((ImmutableType)type.getEnclosedClass()));
		}
		
		return type;
	}
	
	private static ImmutableType getOutputName(ImmutableType type) {
		return type.addClassSufix(SUFFIX);
	}
	
	@Override
	protected ImmutableType getDelegateImmutableType() {
		ImmutableType outputName = getOutputName(annotationNamedType);
		outputName = outputName.setEnclosedClass(annotationNamedType.getEnclosedClass());
		return outputName;
	}

	public AnnotationMirror getAnnotationMirrorForElement(TypeElement typeElement) {
		for (AnnotationMirror annotationMirror: typeElement.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().equals(annotationType)) {
				return annotationMirror;
			}
		}
		
		return null;
	}
	
	public boolean exists() {
		return processingEnv.getElementUtils().getTypeElement(getCanonicalName()) != null;
	}
}