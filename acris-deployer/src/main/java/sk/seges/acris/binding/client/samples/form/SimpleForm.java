package sk.seges.acris.binding.client.samples.form;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.annotations.BindingFieldsBase;
import sk.seges.acris.binding.client.annotations.BindingSpecLoader;
import sk.seges.acris.binding.client.annotations.Generated;
import sk.seges.acris.binding.client.annotations.ValidationStrategy;
import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.acris.binding.client.samples.loaders.CompanyDataLoader;
import sk.seges.acris.binding.client.samples.mocks.Company;
import sk.seges.acris.binding.client.samples.mocks.SimpleBean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

@BindingFieldsBase(updateStrategy=UpdateStrategy.READ_WRITE,validationStrategy=ValidationStrategy.ON_SUBMIT, validationHighlighter = ExampleHighlighter.class)
public class SimpleForm extends StandardFormBase implements IBeanBindingHolder<SimpleBean> {

	protected final Label nameLabel = GWT.create(Label.class);
	@BindingField(SimpleBean.NAME_ATTRIBUTE)
	protected final TextBox nameField = GWT.create(TextBox.class);

	protected final Label emailLabel = GWT.create(Label.class);
	@BindingField(SimpleBean.EMAIL_ATTRIBUTE)
	protected final TextBox emailField = GWT.create(TextBox.class);

	protected final Label companyLabel = GWT.create(Label.class);
	@BindingField(SimpleBean.COMPANY_ATTRIBUTE + "." + Company.NAME_ATTRIBUTE)
	@BindingSpecLoader(CompanyDataLoader.class)
	protected final ListBox companyListBox = GWT.create(ListBox.class);

	protected final Label birthdayLabel = GWT.create(Label.class);
	@BindingField(SimpleBean.DATE_ATTRIBUTE)
	protected final DateBox birthdayDateBox = GWT.create(DateBox.class);

	protected final Label timeLabel = GWT.create(Label.class);
	protected final ListBox timeListBox = GWT.create(ListBox.class);

	protected final Label musicLabel = GWT.create(Label.class);
	protected final CheckBox musicClassicalCheckBox = GWT.create(CheckBox.class);
	protected final CheckBox musicRockCheckBox = GWT.create(CheckBox.class);
	protected final CheckBox musicBluesCheckBox = GWT.create(CheckBox.class);
	
	protected final Label favoriteColorLabel = GWT.create(Label.class);
	protected final RadioButton favoriteColorRedRadioButton = new RadioButton("group", "RedColor");
	protected final RadioButton favoriteColorBlueRadioButton = new RadioButton("group", "BlueColor");

	protected final Label descriptionLabel = GWT.create(Label.class);
	protected final TextArea descriptionTextArea = GWT.create(TextArea.class);

	public SimpleForm() {
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		initLabels();
		
		nameField.setWidth("90%");
		companyListBox.setWidth("90%");
		emailField.setWidth("90%");
		birthdayDateBox.setWidth("90%");
		timeListBox.setWidth("90%");
		descriptionTextArea.setWidth("90%");
		
		addWidget(nameLabel, nameField);
		addWidget(emailLabel, emailField);
		addWidget(companyLabel, companyListBox);
		addWidget(birthdayLabel, birthdayDateBox);
		addWidget(timeLabel, timeListBox);
		
		FlowPanel musicPanel = new FlowPanel();
		musicPanel.add(musicClassicalCheckBox);
		musicPanel.add(musicRockCheckBox);
		musicPanel.add(musicBluesCheckBox);
		addWidget(musicLabel, musicPanel);
		
		FlowPanel favoriteColorPanel = new FlowPanel();
		favoriteColorPanel.add(favoriteColorBlueRadioButton);
		favoriteColorPanel.add(favoriteColorRedRadioButton);
		
		addWidget(favoriteColorLabel, favoriteColorPanel);
		addWidget(descriptionLabel, descriptionTextArea);
		
		setWidth("320px");
	}
	
	private void initLabels() {
		setTitle("Simple Form");
		nameLabel.setText("Name:");
		emailLabel.setText("Email:");
		companyLabel.setText("Company:");
		birthdayLabel.setText("Birthday:");
		timeLabel.setText("Time:");
		musicLabel.setText("Music:");
		favoriteColorLabel.setText("Favorite Color:");
		descriptionLabel.setText("Description:");

		musicClassicalCheckBox.setText("Classical");
		musicRockCheckBox.setText("Rock");
		musicBluesCheckBox.setText("Blues");
		
		favoriteColorBlueRadioButton.setText("Blue");
		favoriteColorRedRadioButton.setText("Red");
	}

	@Override
	@Generated
	public void setBean(SimpleBean bean) {
	}
	
	@Override
	@Generated
	public SimpleBean getBean() {
		return null;
	}
}