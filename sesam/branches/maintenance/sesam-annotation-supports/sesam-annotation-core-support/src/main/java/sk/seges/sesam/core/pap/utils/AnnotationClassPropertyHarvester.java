package sk.seges.sesam.core.pap.utils;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.SimpleTypeVisitor6;


public class AnnotationClassPropertyHarvester {

	public static interface AnnotationClassProperty<A> {
		Class<?> getClassProperty(A annotation);
	}

	public static <A extends Annotation> TypeElement getTypeOfClassProperty(A annotation, AnnotationClassProperty<A> annotationClassProperty) {
		try {
			annotationClassProperty.getClassProperty(annotation);
		} catch (MirroredTypeException mte) {
			return mte.getTypeMirror().accept(new SimpleTypeVisitor6<TypeElement, Void>() {
				@Override
				public TypeElement visitDeclared(DeclaredType t, Void p) {
					return ((TypeElement) t.asElement());
				}
			}, null);
		}

		//never happend
		return null;
	}


	public static <A extends Annotation> TypeElement getTypeOfClassProperty(A annotation, AnnotationClassProperty<A> annotationClassProperty, ProcessingEnvironment processingEnv) {
		try {
			Class<?> result = annotationClassProperty.getClassProperty(annotation);
			return processingEnv.getElementUtils().getTypeElement(result.getCanonicalName());
		} catch (MirroredTypeException mte) {
			return mte.getTypeMirror().accept(new SimpleTypeVisitor6<TypeElement, Void>() {
				@Override
				public TypeElement visitDeclared(DeclaredType t, Void p) {
					return ((TypeElement) t.asElement());
				}
			}, null);
		}
	}
}