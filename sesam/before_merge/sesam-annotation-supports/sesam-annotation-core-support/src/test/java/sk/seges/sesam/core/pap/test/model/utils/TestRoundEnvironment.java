package sk.seges.sesam.core.pap.test.model.utils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class TestRoundEnvironment implements RoundEnvironment {

	private Set<? extends Element> rootElements = new HashSet<Element>();
	private ProcessingEnvironment processingEnv;
	
	public TestRoundEnvironment(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	@Override
	public boolean processingOver() {
		return false;
	}

	@Override
	public boolean errorRaised() {
		return false;
	}

	public void setRootElements(Set<Class<?>> classes) {
		Set<Element> rootElements = new HashSet<Element>();
		for (Class<?> clazz: classes) {
			TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(clazz.getName());
			if (typeElement != null) {
				rootElements.add(typeElement);
			}
		}
		this.rootElements = rootElements;
	}
	
	@Override
	public Set<? extends Element> getRootElements() {
		return rootElements;
	}

	@Override
	public Set<? extends Element> getElementsAnnotatedWith(TypeElement a) {
		//TODO
		return null;
	}

	@Override
	public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a) {
		Set<Element> result = new HashSet<Element>();
		
		for (Element element: rootElements) {
			if (element.getAnnotation(a) != null) {
				result.add(element);
			}
		}
		
		return result;
	}
}