package sk.seges.acris.binding.rebind.binding.support;

import java.lang.annotation.Annotation;

import sk.seges.acris.binding.client.bind.annotations.BindingField;
import sk.seges.acris.binding.client.bind.providers.annotations.OneToOne;

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

		sourceWriter.println(bindingHolder + ".addBinding(\"" + bindingFieldAnnotation.value() + 
				"\", " + field.getName() + ", \"" + getWidgetBindingAdapterProperty(field) + "\");");
		
		return true;
	}
}