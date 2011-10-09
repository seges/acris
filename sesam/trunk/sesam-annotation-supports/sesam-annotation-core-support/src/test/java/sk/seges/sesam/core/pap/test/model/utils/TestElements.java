package sk.seges.sesam.core.pap.test.model.utils;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class TestElements implements Elements {

	@Override
	public PackageElement getPackageElement(CharSequence name) {
		return new TestPackageElement(name);
	}

	@Override
	public TypeElement getTypeElement(CharSequence name) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(name.toString());
		} catch (ClassNotFoundException e) {
			return null;
		}
		
		return new TestTypeElement(clazz.isInterface() ? ElementKind.INTERFACE: 
				clazz.isAnnotation() ? ElementKind.ANNOTATION_TYPE:
				clazz.isEnum() ? ElementKind.ENUM: ElementKind.CLASS, clazz.getSimpleName(), clazz.getPackage().toString());
	}

	@Override
	public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(AnnotationMirror a) {
		return null;
	}

	@Override
	public String getDocComment(Element e) {
		return null;
	}

	@Override
	public boolean isDeprecated(Element e) {
		return false;
	}

	@Override
	public Name getBinaryName(TypeElement type) {
		return new TestName(type.toString());
	}

	@Override
	public PackageElement getPackageOf(Element type) {
		if (type instanceof TypeElement) {
			return ((TestTypeElement)type).getPackage();
		}
		
		return null;
	}

	@Override
	public List<? extends Element> getAllMembers(TypeElement type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends AnnotationMirror> getAllAnnotationMirrors(Element e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hides(Element hider, Element hidden) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean overrides(ExecutableElement overrider, ExecutableElement overridden, TypeElement type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getConstantExpression(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printElements(Writer w, Element... elements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Name getName(CharSequence cs) {
		return new TestName(cs.toString());
	}

}
