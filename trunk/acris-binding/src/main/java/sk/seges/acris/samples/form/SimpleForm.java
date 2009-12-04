package sk.seges.acris.samples.form;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class SimpleForm extends Composite {
	private StandardFormBase formPanel = GWT.create(StandardFormBase.class);

	protected final Label nameLabel = GWT.create(Label.class);
	protected final TextBox nameField = GWT.create(TextBox.class);

	protected final Label emailLabel = GWT.create(Label.class);
	protected final TextBox emailField = GWT.create(TextBox.class);

	protected final Label companyLabel = GWT.create(Label.class);
	protected final ListBox companyListBox = GWT.create(ListBox.class);

	protected final Label birthdayLabel = GWT.create(Label.class);
	protected final DateBox birthdayDateBox = GWT.create(DateBox.class);

	protected final Label timeLabel = GWT.create(Label.class);
	protected final ListBox timeListBox = GWT.create(ListBox.class);

	protected final Label musicLabel = GWT.create(Label.class);
	protected final CheckBox musicClassicalCheckBox = GWT.create(CheckBox.class);
	protected final CheckBox musicRockCheckBox = GWT.create(CheckBox.class);
	protected final CheckBox musicBluesCheckBox = GWT.create(CheckBox.class);
	
	protected final Label favoriteColorLabel = GWT.create(Label.class);
	protected final RadioButton favoriteColorRedRadioButton = new RadioButton("RedColor");
	protected final RadioButton favoriteColorBlueRadioButton = new RadioButton("BlueColor");

	protected final Label descriptionLabel = GWT.create(Label.class);
	protected final TextArea descriptionTextArea = GWT.create(TextArea.class);

	public SimpleForm() {
		initWidget(formPanel);
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
		
		formPanel.addWidget(nameLabel, nameField);
		formPanel.addWidget(emailLabel, emailField);
		formPanel.addWidget(companyLabel, companyListBox);
		formPanel.addWidget(birthdayLabel, birthdayDateBox);
		formPanel.addWidget(timeLabel, timeListBox);
		
		FlowPanel musicPanel = new FlowPanel();
		musicPanel.add(musicClassicalCheckBox);
		musicPanel.add(musicRockCheckBox);
		musicPanel.add(musicBluesCheckBox);
		formPanel.addWidget(musicLabel, musicPanel);
		
		FlowPanel favoriteColorPanel = new FlowPanel();
		favoriteColorPanel.add(favoriteColorBlueRadioButton);
		favoriteColorPanel.add(favoriteColorRedRadioButton);
		
		formPanel.addWidget(favoriteColorLabel, favoriteColorPanel);
		formPanel.addWidget(descriptionLabel, descriptionTextArea);
		
		setWidth("320px");
	}
	
	private void initLabels() {
		formPanel.setTitle("Simple Form");
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
}