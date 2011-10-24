package sk.seges.acris.binding.client.annotations;

import org.gwt.beansbinding.core.client.Validator;

public class BindingFieldInfo {
	
	private Object sourceObject;
	private String sourceProperty;
	private Object targetWidget;
	private String targetProperty;
	private Validator<?> validator;

	public String getSourceProperty() {
		return sourceProperty;
	}

	public void setSourceProperty(String sourceProperty) {
		this.sourceProperty = sourceProperty;
	}

	public Object getTargetWidget() {
		return targetWidget;
	}

	public void setTargetWidget(Object targetWidget) {
		this.targetWidget = targetWidget;
	}

	public String getTargetProperty() {
		return targetProperty;
	}

	public void setTargetProperty(String targetProperty) {
		this.targetProperty = targetProperty;
	}

	public Object getSourceObject() {
		return sourceObject;
	}

	public void setSourceObject(Object sourceObject) {
		this.sourceObject = sourceObject;
	}
	
	public Validator<?> getValidator() {
		return validator;
	}
	
	public void setValidator(Validator<?> validator) {
		this.validator = validator;
	}
}
