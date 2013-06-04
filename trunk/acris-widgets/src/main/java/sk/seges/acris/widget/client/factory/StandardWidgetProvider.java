package sk.seges.acris.widget.client.factory;

import sk.seges.acris.widget.client.Dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class StandardWidgetProvider implements WidgetProvider {

	@Override
	public Label createLabel() {
		return GWT.create(Label.class);
	}

	@Override
	public Button createButton() {
		return GWT.create(Button.class);
	}

	@Override
	public CheckBox createCheckBox() {
		return GWT.create(CheckBox.class);
	}

	@Override
	public DateBox createDateBox() {
		return GWT.create(DateBox.class);
	}

	@Override
	public Dialog createDialog() {
		return GWT.create(Dialog.class);
	}

	@Override
	public DialogBox createDialogBox() {
		return GWT.create(DialogBox.class);
	}

	@Override
	public ListBox createListBox() {
		return GWT.create(ListBox.class);
	}

	@Override
	public PushButton createPushButton() {
		return GWT.create(PushButton.class);
	}

	@Override
	public TextArea createTextArea() {
		return GWT.create(TextArea.class);
	}

	@Override
	public TextBox createTextBox() {
		return GWT.create(TextBox.class);
	}

	@Override
	public Label createMessageButton() {
		// TODO Auto-generated method stub
		return null;
	}
}