package sk.seges.sesam.core.pap.builder.api;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public interface ClassPathTypes {

    Set<? extends Element> getElementsAnnotatedWith(TypeElement a);
    Set<? extends Element> getElementsAnnotatedWith(TypeElement a, RoundEnvironment roundEnv);
    
    Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a);
    Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a, RoundEnvironment roundEnv);
}
