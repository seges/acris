package sk.seges.acris.theme.rebind.specific;

import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;


public abstract class AbstractComponentSpecificProcessor implements ComponentSpecificProcessor {

	public enum Statement {
		CONSTRUCTOR, SUPER_CONSTRUCTOR_ARGS, CLASS;
	}
	
	protected ProcessingEnvironment processingEnv;
	
	protected abstract Class<?> getComponentClass();
	
	@Override
	public boolean supports(TypeElement typeElement) {
		TypeElement checkBoxTypeElement = processingEnv.getElementUtils().getTypeElement(getComponentClass().getCanonicalName());
		return processingEnv.getTypeUtils().isSameType(typeElement.asType(), checkBoxTypeElement.asType());
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	@Override
	public Type[] getImports() {
		return new Type[] {};
	}

}
