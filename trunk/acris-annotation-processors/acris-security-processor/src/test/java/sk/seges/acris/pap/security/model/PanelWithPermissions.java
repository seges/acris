package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.client.annotations.Secured;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

@Secured
public class PanelWithPermissions {

	@Secured("TEXT")
	Label label1;

	@Secured("HEADING")
	Label label2;

	@Secured("YOUTUBE")
	TextBox textBox;
	
	@Secured("SUPPORT")
	CheckBox checkBox;
	
	@Secured("GENDER")
	RadioButton radioButton;
}