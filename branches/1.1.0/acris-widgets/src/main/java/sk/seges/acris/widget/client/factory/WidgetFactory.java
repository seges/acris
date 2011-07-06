/**
 * 
 */
package sk.seges.acris.widget.client.factory;

import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.FormHolder;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Set of methods replacing multi-parameter constructor where it cannot
 * be applied easily to create widgets. Especially in cases where widgets are
 * created using GWT.create method.
 * 
 * @author ladislav.gazo
 */
public class WidgetFactory {

	public static final String HACK_WIDGET = "hack-widget";
	
	protected WidgetProvider widgetProvider;
	
	public WidgetFactory(WidgetProvider widgetProvider) {
		this.widgetProvider = widgetProvider;
	}
	
	public Label label(String label) {
		Label widget = widgetProvider.createLabel();
		widget.setText(label);
		return widget;
	}

	public Label label(String label, String style) {
		Label widget = label(label);
		widget.setStyleName(style);
		return widget;
	}

	public Button button(String label) {
		Button widget = widgetProvider.createButton();
		widget.setText(label);
		return widget;
	}

	public Button button(String label, ClickHandler handler) {
		Button widget = button(label);
		widget.addClickHandler(handler);
		return widget;
	}

	public PushButton pushButton(String label) {
		PushButton widget = widgetProvider.createPushButton();
		widget.setText(label);
		return widget;
	}

	public PushButton pushButton(String label, ClickHandler handler) {
		PushButton widget = pushButton(label);
		widget.addClickHandler(handler);
		return widget;
	}

	public PushButton pushButton(Image image, ClickHandler handler) {
		PushButton widget = widgetProvider.createPushButton();
		widget.getUpFace().setImage(image);
		widget.addClickHandler(handler);
		return widget;
	}

	public ToggleButton toggleButton() {
		// ToggleButton widget = GWT.create(ToggleButton.class);

		// FIXME: tmp. until themed ToggleButton looks like it must.
		ToggleButton widget = new ToggleButton();
		widget.addStyleName("acris-cmp-toggle-button");
		return widget;
	}

	public ToggleButton toggleButton(Image up, Image down) {
		ToggleButton widget = toggleButton();
		widget.getUpFace().setImage(up);
		widget.getDownFace().setImage(down);
		return widget;
	}

	public TextBox textBox(String style) {
		TextBox widget = widgetProvider.createTextBox();
		widget.setStyleName(style);
		return widget;
	}

	public TextBox textBox(String style, String text) {
		TextBox widget = textBox(style);
		widget.setText(text);
		return widget;
	}

	public TextBox textBox(String style, String text, boolean enabled) {
		TextBox widget = textBox(style);
		widget.setText(text);
		widget.setEnabled(enabled);
		return widget;
	}

	@Deprecated
	public static SimplePanel hackWidget(Widget widget) {
		// TODO: remove this when it will be fixed in themed components
		SimplePanel buttonWrapper = new SimplePanel();
		buttonWrapper.add(widget);
		buttonWrapper.addStyleName(HACK_WIDGET);
		return buttonWrapper;
	}

	/**
	 * @return Modal dialog without autohiding.
	 */
	public Dialog modalDialog() {
		Dialog dialog = dialog();
		dialog.setModal(true);
		dialog.setAutoHideEnabled(false);
		return dialog;
	}

	public Dialog modalAutoHideDialog() {
		Dialog dialog = dialog();
		dialog.setModal(true);
		dialog.setAutoHideEnabled(true);
		return dialog;
	}

	/**
	 * @return Non-modal (modeless), non-autohide dialog.
	 */
	public Dialog modelessDialog() {
		Dialog dialog = dialog();
		dialog.setModal(false);
		dialog.setAutoHideEnabled(false);
		return dialog;
	}

	public Dialog dialog() {
		Dialog dialog = widgetProvider.createDialog();
		dialog.setGlassEnabled(true);
		return dialog;
	}

	public Dialog dialog(boolean autohide, boolean modal) {
		if (autohide == false && modal == false) {
			return modelessDialog();
		} else if (autohide == false && modal == true) {
			return modalDialog();
		} else if (autohide == true && modal == true) {
			return modalAutoHideDialog();
		}
		return dialog();
	}

	public Dialog dialog(boolean autohide, boolean modal, boolean glass) {
		Dialog dialog = dialog(autohide, modal);
		dialog.setGlassEnabled(glass);
		return dialog;
	}

	public DateBox dateBox() {
		return dateBox("dd.MM.yyyy");
	}

	public DateBox dateBox(String dateTimeFormatPattern) {
		DateBox dateBox = widgetProvider.createDateBox();
		DateBox.DefaultFormat dateBoxFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat(dateTimeFormatPattern));
		dateBox.setFormat(dateBoxFormat);
		return dateBox;
	}

	public ListBox listBox(int visibleItemCount) {
		ListBox listBox = widgetProvider.createListBox();
		listBox.setVisibleItemCount(visibleItemCount);
		return listBox;
	}

	public CheckBox checkBox() {
		return widgetProvider.createCheckBox();
	}

	public CheckBox checkBox(String text) {
		CheckBox checkBox = checkBox();
		checkBox.setText(text);
		return checkBox;
	}

	public CheckBox checkBox(ValueChangeHandler<Boolean> valueChangeHandler) {
		CheckBox checkBox = checkBox();
		checkBox.addValueChangeHandler(valueChangeHandler);
		return checkBox;
	}

	public CheckBox checkBox(String text, ValueChangeHandler<Boolean> valueChangeHandler) {
		CheckBox checkBox = checkBox(text);
		checkBox.addValueChangeHandler(valueChangeHandler);
		return checkBox;
	}

	public CheckBox checkBoxWithId(String id) {
		CheckBox checkBox = checkBox();
		checkBox.ensureDebugId(id);
		return checkBox;
	}
	
	public RadioButton radioButton(String name) {
		//TODO Use widget provider
		return new RadioButton(name);
	}

	public RadioButton radioButton(String name, String label) {
		RadioButton radioButton = radioButton(name);
		radioButton.setText(label);
		return radioButton;
	}

	/**
	 * @param name
	 * @param label
	 * @param valueChangeHandler
	 * @return
	 */
	public RadioButton radioButton(String name, String label, ValueChangeHandler<Boolean> valueChangeHandler) {
		RadioButton radioButton = radioButton(name, label);
		radioButton.addValueChangeHandler(valueChangeHandler);
		return radioButton;
	}

	public RadioButton radioButton(String name, ValueChangeHandler<Boolean> valueChangeHandler) {
		RadioButton radioButton = radioButton(name);
		radioButton.addValueChangeHandler(valueChangeHandler);
		return radioButton;
	}

	public void showAndCenter(final FormHolder formHolder) {
		formHolder.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = ((Window.getClientWidth() - offsetWidth) / 2) >> 0;
				int top = ((Window.getClientHeight() - offsetHeight) / 2) >> 0;
				formHolder.setPopupPosition(left, top);
			}
		});
	}

	/**
	 * @param dialog
	 */
	public void showAndCenter(final Dialog dialog) {
		dialog.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = ((Window.getClientWidth() - offsetWidth) / 2) >> 0;
				int top = ((Window.getClientHeight() - offsetHeight) / 2) >> 0;
				dialog.setPopupPosition(left, top);
			}
		});
	}

	public void show(final Dialog dialog, final Integer leftVerticalCenterOffset, final Integer topOffset) {
		dialog.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left;
				if (leftVerticalCenterOffset == null) {
					left = ((Window.getClientWidth() - offsetWidth) / 2) >> 0;
				} else {
					left = (((Window.getClientWidth() - offsetWidth) / 2) >> 0) - leftVerticalCenterOffset;
				}

				int top;
				if (topOffset == null) {
					top = ((Window.getClientHeight() - offsetHeight) / 2) >> 0;
				} else {
					top = topOffset;
				}
				dialog.setPopupPosition(left, top);
			}
		});
	}

	public TextArea textArea(String style) {
		TextArea textArea = widgetProvider.createTextArea();
		textArea.setStyleName(style);
		return textArea;
	}
}
