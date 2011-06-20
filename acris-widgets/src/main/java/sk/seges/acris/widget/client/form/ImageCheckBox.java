package sk.seges.acris.widget.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;

public class ImageCheckBox extends CheckBox {

	interface DefaultCheckBoxImageResources extends CheckBoxImageResources {

		@Source("checkbox-checked.png")
		ImageResource checked();

		@Source("checkbox-unchecked.png")
		ImageResource unchecked();

		@Source("checkbox-checked-disabled.png")
		ImageResource checkedDisabled();

		@Source("checkbox-unchecked-disabled.png")
		ImageResource uncheckedDisabled();

	}
	
	private static final String CHECK_BOX_STYLE_CLASS_SUFFIX_DISABLED = "-disabled";
	private static final String CHECK_BOX_STYLE_CLASS_SUFFIX_UNCHECKED = "-unchecked";
	private static final String CHECK_BOX_STYLE_CLASS_SUFFIX_CHECKED = "-checked";	
	private static final String CHECK_BOX_STYLE_CLASS_NAME = "acris-check-box";

	private InputElement inputElement;
	private CheckBoxImageResources checkBoxImageResources;
	
	private String styleClassName = CHECK_BOX_STYLE_CLASS_NAME;

	public ImageCheckBox() {
		this((CheckBoxImageResources)GWT.create(DefaultCheckBoxImageResources.class));
	}

	public ImageCheckBox(CheckBoxImageResources checkBoxImageResources) {
		this(createImageInput(), checkBoxImageResources);
	}

	public ImageCheckBox(com.google.gwt.dom.client.Element element, CheckBoxImageResources checkBoxImageResources) {
		this(element, checkBoxImageResources, "");
	}
	
	public ImageCheckBox(com.google.gwt.dom.client.Element element, CheckBoxImageResources checkBoxImageResources, String stylePrefix) {
	super((Element) element.cast());
		
		this.styleClassName = stylePrefix + styleClassName;
		
		this.inputElement = (InputElement) element.cast();
		this.checkBoxImageResources = checkBoxImageResources;
		
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (isEnabled()) {
					setValue(!getValue());
				}
			}
		});
		setValue(false);
	}
	
	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}
	
	protected ImageResource getCheckBoxImage(boolean checked, boolean enabled) {
		return checked ? (enabled ? checkBoxImageResources.checked() : checkBoxImageResources.checkedDisabled()) : (enabled ? checkBoxImageResources.unchecked() : checkBoxImageResources.uncheckedDisabled());
	}
	
	private String getStyleClassName(boolean checked, boolean enabled) {
		String result = styleClassName + (checked ? CHECK_BOX_STYLE_CLASS_SUFFIX_CHECKED : CHECK_BOX_STYLE_CLASS_SUFFIX_UNCHECKED);
		if (!enabled) {
			result += CHECK_BOX_STYLE_CLASS_SUFFIX_DISABLED;
		}
		return result;
	}
	
	static InputElement createImageInput() {
		return Document.get().createImageInputElement();
	}

	@Override
	public void setEnabled(boolean enabled) {

		stateChanged(getValue(), enabled);
		
		super.setEnabled(enabled);
	}
	
	public void setValue(Boolean value, boolean fireEvents) {
		if (value == null) {
			value = Boolean.FALSE;
		}

		stateChanged(value, isEnabled());
		
		super.setValue(value, fireEvents);
	}
	
	private void stateChanged(boolean value, boolean enabled) {
		if (value != getValue()) {
			inputElement.<com.google.gwt.user.client.Element> cast().removeClassName(getStyleClassName(!value, isEnabled()));
			inputElement.<com.google.gwt.user.client.Element> cast().removeClassName(getStyleClassName(!value, !isEnabled()));
		} else {
			inputElement.<com.google.gwt.user.client.Element> cast().removeClassName(getStyleClassName(getValue(), !enabled));
			inputElement.<com.google.gwt.user.client.Element> cast().removeClassName(getStyleClassName(!getValue(), !enabled));
		}
		inputElement.<com.google.gwt.user.client.Element> cast().addClassName(getStyleClassName(value, enabled));
		
		ImageResource checkBoxImage = getCheckBoxImage(value, enabled);
		//We had to use this ugly hack because sprites are not working correctly in the IE7
		//http://code.google.com/p/google-web-toolkit/issues/detail?id=4521
		//When this will be fixed we should use URL property instead of setting background
		inputElement.getStyle().setProperty("background", new Image(checkBoxImage).getElement().getStyle().getProperty("background"));

//		inputElement.setSrc(image.getUrl());
	}
}