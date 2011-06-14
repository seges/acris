package sk.seges.acris.widget.client.form;

import com.google.gwt.resources.client.ImageResource;


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