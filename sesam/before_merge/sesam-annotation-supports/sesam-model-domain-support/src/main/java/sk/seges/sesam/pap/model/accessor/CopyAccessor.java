package sk.seges.sesam.pap.model.accessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.annotation.Annotations;
import sk.seges.sesam.pap.model.annotation.Copy;

public class CopyAccessor extends AnnotationAccessor {

	private final Set<Annotations> annotations = new HashSet<Annotations>();

	public CopyAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);

		Copy copyAnnotation = getAnnotation(element, Copy.class);
		
		if (copyAnnotation != null) {
			Annotations[] annotations = copyAnnotation.annotations();
			
			if (annotations != null && annotations.length > 0) {
				for (Annotations annotation: annotations) {
					this.annotations.add(annotation);
				}
			}
		}
	}

	private Set<Annotations> getAnnotationsForKind(ElementKind kind) {
		
		Set<Annotations> result = new HashSet<Annotations>();
		
		Iterator<Annotations> iterator = annotations.iterator();

		while (iterator.hasNext()) {
			Annotations annotations = iterator.next();
			
			if (annotations.accessor().supportsKind(kind)) {
				result.add(annotations);
			}
		}
		
		return result;
	}
	
	private boolean supports(Annotations annotations, AnnotationMirror annotationMirror) {

		Class<?>[] typesOf = annotations.typeOf();
		
		if (typesOf != null && typesOf.length > 0) {
			for (Class<?> typeOf: typesOf) {
				if (typeOf.getName().toString().equals(annotationMirror.getAnnotationType().toString())) {
					return true;
				}
			}
		}

		Class<?>[] packagesOf = annotations.packageOf();

		if (packagesOf != null && packagesOf.length > 0) {
			for (Class<?> packageOf: packagesOf) {
				if (packageOf.getPackage().getName().equals(
					processingEnv.getElementUtils().getPackageOf(annotationMirror.getAnnotationType().asElement()).getQualifiedName().toString())) {
					return true;
				}
			}
		}

		return false;
	}
	
	public List<AnnotationMirror> getSupportedAnnotations(Element element) {
		List<AnnotationMirror> result = new ArrayList<AnnotationMirror>();

		if (element == null) {
			return result;
		}
		
		Set<Annotations> annotationsForKind = getAnnotationsForKind(element.getKind());
		
		if (annotationsForKind.size() == 0) {
			return result;
		}
		
		List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();

		for (AnnotationMirror annotationMirror: annotationMirrors) {
			Iterator<Annotations> iterator = annotationsForKind.iterator();
			
			while (iterator.hasNext()) {
				
				Annotations annotationForKind = iterator.next();
				
				if (supports(annotationForKind, annotationMirror)) {
					result.add(annotationMirror);
				}
			}
		}
		
		return result;
	}

	@Override
	public boolean isValid() {
		return annotations.size() > 0;
	}
}