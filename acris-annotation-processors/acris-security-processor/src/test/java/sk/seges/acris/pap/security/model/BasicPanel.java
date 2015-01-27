package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.client.annotations.ManagedSecurity;
import sk.seges.acris.security.client.annotations.RuntimeSecurity;
import sk.seges.acris.security.client.annotations.Secured;
import sk.seges.acris.security.shared.user_management.domain.Permission;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

@Secured("TEXT")
@RuntimeSecurity
@ManagedSecurity
public class BasicPanel {

	@Secured(permission = Permission.VIEW)
	Label label1;

	@Secured
	Label label2;

	@Secured(permission = Permission.EDIT)
	TextBox textBox;
	
	@Secured(permission = Permission.VIEW)
	CheckBox checkBox;
	
	@Secured(permission = Permission.VIEW)
	RadioButton radioButton;
}