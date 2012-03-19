package sk.seges.sesam.core.pap.model.mutable.utils;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;

public class MutableElements implements Elements {

	private Elements elements;
	private MutableProcessingEnvironment processingEnv;

	public MutableElements(MutableProcessingEnvironment processingEnv, Elements elements) {
		this.elements = elements;
		this.processingEnv = processingEnv;
	}

	@Override
	public PackageElement getPackageElement(CharSequence name) {
		return this.elements.getPackageElement(name);
	}

	@Override
	public TypeElement getTypeElement(CharSequence name) {
		return this.elements.getTypeElement(name);
	}

	public MutableExecutableElement getExecutableElement(String name) {
		return new MutableExecutable(name, processingEnv);
	}
	
	@Override
	public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(AnnotationMirror a) {
		return this.elements.getElementValuesWithDefaults(a);
	}

	@Override
	public String getDocComment(Element e) {
		return this.elements.getDocComment(e);
	}

	@Override
	public boolean isDeprecated(Element e) {
		return this.elements.isDeprecated(e);
	}

	@Override
	public Name getBinaryName(TypeElement type) {
		return this.elements.getBinaryName(type);
	}

	@Override
	public PackageElement getPackageOf(Element type) {
		return this.elements.getPackageOf(type);
	}

	@Override
	public List<? extends Element> getAllMembers(TypeElement type) {
		return this.elements.getAllMembers(type);
	}

	@Override
	public List<? extends AnnotationMirror> getAllAnnotationMirrors(Element e) {
		return this.elements.getAllAnnotationMirrors(e);
	}

	@Override
	public boolean hides(Element hider, Element hidden) {
		return this.elements.hides(hider, hidden);
	}

	@Override
	public boolean overrides(ExecutableElement overrider, ExecutableElement overridden, TypeElement type) {
		return this.elements.overrides(overrider, overridden, type);
	}

	@Override
	public String getConstantExpression(Object value) {
		return this.elements.getConstantExpression(value);
	}

	@Override
	public void printElements(Writer w, Element... elements) {
		this.elements.printElements(w, elements);
	}

	@Override
	public Name getName(CharSequence cs) {
		return this.elements.getName(cs);
	}
	
	public MutableExecutableElement toMutableElement(ExecutableElement executableElement) {
		return new MutableExecutable(executableElement, processingEnv);
	}
}
