package sk.seges.sesam.core.pap.configuration;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;

import sk.seges.sesam.core.pap.model.api.NamedType;

public abstract class DelegateProcessorConfigurer extends DefaultProcessorConfigurer {

	protected abstract Class<? extends Annotation>[] getDelegatedAnnotationClasses();

	protected abstract Annotation getAnnotationFromDelegate(Annotation annotationDelegate);

	protected abstract AnnotationMirror getAnnotationFromDelegate(AnnotationMirror annotationDelegate);

	protected boolean isDelegateAnnotation(AnnotationMirror annotation) {
		Class<? extends Annotation>[] delegatedAnnotationClasses = getDelegatedAnnotationClasses();
		
		if (delegatedAnnotationClasses == null) {
			return false;
		}
		
		for (Class<? extends Annotation> delegatedAnnotationClass: delegatedAnnotationClasses) {
			if (annotation.getAnnotationType().toString().equals(delegatedAnnotationClass.getCanonicalName())) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected AnnotationMirror[] getAnnotations(VariableElement field) {

		List<AnnotationMirror> result = new ArrayList<AnnotationMirror>();
		
		List<? extends AnnotationMirror> annotationMirrors = field.getAnnotationMirrors();

		NamedType[] supportedAnnotations = getMergedConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);

		for (AnnotationMirror annotation: annotationMirrors) {
			for (NamedType supportedAnnotaion: supportedAnnotations) {
				if (isDelegateAnnotation(annotation)) {
					result.add(getAnnotationFromDelegate(annotation));
				} else if (annotation.getAnnotationType().toString().equals(supportedAnnotaion.getCanonicalName())) {
					result.add(annotation);
				}
			}
		}

		return result.toArray(new AnnotationMirror[] {});
	}
}