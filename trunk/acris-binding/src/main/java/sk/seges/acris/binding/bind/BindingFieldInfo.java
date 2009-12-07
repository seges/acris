package sk.seges.acris.binding.bind;

import org.gwt.beansbinding.core.client.Validator;

import com.google.gwt.user.client.ui.Widget;

public class BindingFieldInfo {
	
	private Object sourceObject;
	private String sourceProperty;
	private Widget targetWidget;
	private String targetProperty;
	private Validator<?> validator;

	public String getSourceProperty() {
		return sourceProperty;
	}

	public void setSourceProperty(String sourceProperty) {
		this.sourceProperty = sourceProperty;
	}

	public Widget getTargetWidget() {
		return targetWidget;
	}

	public void setTargetWidget(Widget targetWidget) {
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
