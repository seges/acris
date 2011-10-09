package sk.seges.acris.binding.rebind.binding.support;

import java.lang.annotation.Annotation;

class BindingComponent {
	public String field;
	public Class<? extends Annotation> bindingType;
	
	public BindingComponent(String field, Class<? extends Annotation> bindingType) {
		this.field = field;
		this.bindingType = bindingType;
	}
}
