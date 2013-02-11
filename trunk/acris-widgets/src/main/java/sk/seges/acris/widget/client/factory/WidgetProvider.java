package sk.seges.acris.widget.client.factory;

import sk.seges.acris.widget.client.Dialog;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public interface WidgetProvider {

	Label createLabel();

	Button createButton();
	
	CheckBox createCheckBox();
	
	DateBox createDateBox();
	
	Dialog createDialog();
	
	DialogBox createDialogBox();

	ListBox createListBox();
	
	PushButton createPushButton();
	
	TextArea createTextArea();

	TextBox createTextBox();	
}