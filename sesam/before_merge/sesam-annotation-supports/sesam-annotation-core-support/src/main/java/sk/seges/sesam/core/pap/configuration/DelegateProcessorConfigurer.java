package sk.seges.sesam.core.pap.configuration;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public abstract class DelegateProcessorConfigurer extends DefaultProcessorConfigurer {

	protected abstract Class<? extends Annotation> getDelegatedAnnotationClass();

	protected abstract Annotation getAnnotationFromDelegate(Annotation annotationDelegate);

	protected class DelegateAnnotationAccessor extends AnnotationAccessor {

		private final AnnotationMirror annotationDelegate;
		private final Class<? extends Annotation> annotationClass;
		
		public DelegateAnnotationAccessor(MutableProcessingEnvironment processingEnv, AnnotationMirror annotationDelegate, Class<? extends Annotation> annotationClass) {
			super(processingEnv);
			this.annotationDelegate = annotationDelegate;
			this.annotationClass = annotationClass;
		}

		@Override
		public boolean isValid() {
			return true;
		}
		
		public AnnotationMirror getDelegateAnnotationMirrorValue() {
			Annotation annotation = super.toAnnotation(annotationDelegate, annotationClass);
			Annotation annotationFromDelegate = getAnnotationFromDelegate(annotation);
			return toAnnotationMirror(annotationFromDelegate);
		}
	}
	
	protected boolean isDelegateAnnotation(Annotation annotation) {
		Class<? extends Annotation> delegatedAnnotationClass = getDelegatedAnnotationClass();
		
		if (delegatedAnnotationClass == null) {
			return false;
		}
		
		return annotation.annotationType().getCanonicalName().equals(delegatedAnnotationClass.getCanonicalName());
	}

	protected boolean isDelegateAnnotation(AnnotationMirror annotation) {
		Class<? extends Annotation> delegatedAnnotationClass = getDelegatedAnnotationClass();
		
		if (delegatedAnnotationClass == null) {
			return false;
		}
		
		return annotation.getAnnotationType().toString().equals(delegatedAnnotationClass.getCanonicalName());
	}

	protected AnnotationMirror getAnnotationFromDelegate(final AnnotationMirror annotationDelegate) {
		return new DelegateAnnotationAccessor(processingEnv, annotationDelegate, getDelegatedAnnotationClass()).getDelegateAnnotationMirrorValue();
	}

	@Override
	protected AnnotationMirror[] getAnnotationMirrors(VariableElement field) {
		List<AnnotationMirror> result = new ArrayList<AnnotationMirror>();
		for (AnnotationMirror annotation : field.getAnnotationMirrors()) {
			if (isDelegateAnnotation(annotation)) {
				result.add(getAnnotationFromDelegate(annotation));
			} else {
				result.add(annotation);
			}
		}
		
		return result.toArray(new AnnotationMirror[] {});

/*		return field.getAnnotationMirrors().toArray(new AnnotationMirror[] {});

		List<AnnotationMirror> result = new ArrayList<AnnotationMirror>();
		
		List<? extends AnnotationMirror> annotationMirrors = field.getAnnotationMirrors();

		MutableDeclaredType[] supportedAnnotations = getMergedConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);

		for (AnnotationMirror annotation: annotationMirrors) {
			for (MutableDeclaredType supportedAnnotaion: supportedAnnotations) {
				if (isDelegateAnnotation(annotation)) {
					result.add(getAnnotationFromDelegate(annotation));
				} else if (annotation.getAnnotationType().toString().equals(supportedAnnotaion.getCanonicalName())) {
					result.add(annotation);
				}
			}
		}

		return result.toArray(new AnnotationMirror[] {});*/
	}
}