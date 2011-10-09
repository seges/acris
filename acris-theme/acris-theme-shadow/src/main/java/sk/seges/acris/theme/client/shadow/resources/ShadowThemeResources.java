package sk.seges.acris.theme.client.shadow.resources;

import sk.seges.acris.widget.client.form.CheckBoxImageResources;

import com.google.gwt.resources.client.ImageResource;


public interface ShadowThemeResources extends CheckBoxImageResources {

	@Source("shadow-checkbox-checked.png")
	ImageResource checked();

	@Source("shadow-checkbox-unchecked.png")
	ImageResource unchecked();

	@Source("shadow-checkbox-checked.png")
	ImageResource checkedDisabled();

	@Source("shadow-checkbox-unchecked.png")
	ImageResource uncheckedDisabled();
}