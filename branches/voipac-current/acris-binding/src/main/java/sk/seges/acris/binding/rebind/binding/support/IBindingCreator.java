package sk.seges.acris.binding.rebind.binding.support;

import java.lang.annotation.Annotation;

import sk.seges.acris.binding.client.annotations.BindingField;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JField;

public interface IBindingCreator<T extends Annotation> {
	boolean isSupported(Class<? extends Annotation> clazz);
	String getPropertyValue(T annotation);
	
	boolean generateBinding(JField field, BindingField bindingFieldAnnotation) throws UnableToCompleteException;
	boolean generateFields(JField field, BindingField bindingFieldAnnotation) throws UnableToCompleteException;

	String[] getImports(JField field, BindingField bindingFieldAnnotation) throws UnableToCompleteException;
}