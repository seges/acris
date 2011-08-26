package sk.seges.acris.binding.rebind.binding.support;

import java.lang.annotation.Annotation;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.providers.annotations.ManyToMany;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JField;

class ManyToManyBindingCreator extends AbstractBindingCreator<ManyToMany>
		implements IBindingCreator<ManyToMany> {

	@Override
	public Class<? extends Annotation> getSupportedClass() {
		return ManyToMany.class;
	}

	@Override
	public String getPropertyValue(ManyToMany annotation) {
		return annotation.value();
	}

	@Override
	public boolean generateBinding(JField field, BindingField bindingField)
			throws UnableToCompleteException {
		return false;
	}

	@Override
	public boolean generateFields(JField field, BindingField bindingField)
			throws UnableToCompleteException {
		return false;
	}	
	
}