package sk.seges.acris.binding.rebind.binding.support;

import java.lang.annotation.Annotation;

import org.gwt.beansbinding.core.client.Converter;
import org.gwt.beansbinding.core.client.Validator;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.providers.annotations.OneToOne;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JField;

class OneToOneBindingCreator extends AbstractBindingCreator<OneToOne> implements IBindingCreator<OneToOne> {
 
	@Override
	public Class<? extends Annotation> getSupportedClass() {
		return OneToOne.class;
	}
	
	@Override
	public String getPropertyValue(OneToOne annotation) {
		return annotation.value();
	}

	@Override
	public boolean generateFields(JField field, BindingField bindingFieldAnnotation)
			throws UnableToCompleteException {
		return true;
	}
	
	@Override
	public boolean generateBinding(JField field, BindingField bindingFieldAnnotation)
			throws UnableToCompleteException {

		Class<Converter<?, ?>> converter = (Class<Converter<?, ?>>) bindingFieldAnnotation.converter();
		String converterInstance = "null";
		if(converter != null && !(converter.equals(Void.class)) && !(converter.equals(Converter.class))) {
			converterInstance = "new " + converter.getCanonicalName() + "()";
		}
		
		Class<Validator<?>> validator = (Class<Validator<?>>) bindingFieldAnnotation.validator();
		String validatorInstance = "null";
		if(validator != null && !(validator.equals(Void.class))) {
			validatorInstance = "new " + validator.getCanonicalName() + "()";
		}
		sourceWriter.println(bindingHolder + ".addBinding(\"" + bindingFieldAnnotation.value() + 
				"\", " + field.getName() + ", \"" + getWidgetBindingAdapterProperty(field) + "\", " + converterInstance + ", " + validatorInstance + ");");
		
		return true;
	}
}