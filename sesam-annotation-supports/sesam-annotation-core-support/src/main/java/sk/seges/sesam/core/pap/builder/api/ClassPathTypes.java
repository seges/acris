package sk.seges.sesam.core.pap.builder.api;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public interface ClassPathTypes {

    Set<? extends Element> getElementsAnnotatedWith(TypeElement a);
    
    Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a);
}
