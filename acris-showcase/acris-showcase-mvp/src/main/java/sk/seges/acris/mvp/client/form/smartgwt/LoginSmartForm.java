package sk.seges.acris.mvp.client.form.smartgwt;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.annotations.BindingFieldsBase;
import sk.seges.acris.binding.client.annotations.ValidationStrategy;
import sk.seges.acris.mvp.client.form.smartgwt.core.TwoColumnSmartForm;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataBeanWrapper;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;


@BindingFieldsBase(updateStrategy = UpdateStrategy.READ_WRITE, validationStrategy = ValidationStrategy.ON_SUBMIT)
public class LoginSmartForm extends TwoColumnSmartForm<UserData<?>> {

	@BindingField(UserDataBeanWrapper.USERNAME)
	protected final TextItem username = GWT.create(TextItem.class);

	@BindingField(UserDataBeanWrapper.PASSWORD)
	protected final PasswordItem password = GWT.create(PasswordItem.class);

	public LoginSmartForm() {
		setWidth(400);
	}

	@Override
	protected void prepareFields() {
		addFormRow(true, withLabel("username", username));
		addFormRow(true, withLabel("description", password));
	}
}
