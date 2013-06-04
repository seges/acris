/**
 * 
 */
package sk.seges.acris.widget.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Set of static methods replacing multi-parameter constructor where it cannot
 * be applied easily to create widgets. Especially in cases where widgets are
 * created using GWT.create method.
 * 
 * @author ladislav.gazo
 */
public class WidgetFactory {
	public static Label label(String label) {
		Label widget = GWT.create(Label.class);
		widget.setText(label);
		return widget;
	}

	public static Label label(String label, String style) {
		Label widget = label(label);
		widget.setStyleName(style);
		return widget;
	}

	public static Button button(String label) {
		Button widget = GWT.create(Button.class);
		widget.setText(label);
		return widget;
	}

	public static Button button(String label, ClickHandler handler) {
		Button widget = button(label);
		widget.addClickHandler(handler);
		return widget;
	}

	public static PushButton pushButton(String label) {
		PushButton widget = GWT.create(PushButton.class);
		widget.setText(label);
		return widget;
	}

	public static PushButton pushButton(String label, ClickHandler handler) {
		PushButton widget = pushButton(label);
		widget.addClickHandler(handler);
		return widget;
	}

	public static PushButton pushButton(Image image, ClickHandler handler) {
		PushButton widget = new PushButton(image, handler);
		return widget;
	}

	public static ToggleButton toggleButton() {
		// ToggleButton widget = GWT.create(ToggleButton.class);

		// FIXME: tmp. until themed ToggleButton looks like it must.
		ToggleButton widget = new ToggleButton();
		widget.addStyleName("acris-cmp-toggle-button");
		return widget;
	}

	public static ToggleButton toggleButton(Image up, Image down) {
		ToggleButton widget = toggleButton();
		widget.getUpFace().setImage(up);
		widget.getDownFace().setImage(down);
		return widget;
	}

	public static TextBox textBox(String style) {
		TextBox widget = GWT.create(TextBox.class);
		widget.setStyleName(style);
		return widget;
	}

	public static TextBox textBox(String style, String text) {
		TextBox widget = textBox(style);
		widget.setText(text);
		return widget;
	}

	public static TextBox textBox(String style, String text, boolean enabled) {
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
		return buttonWrapper;
	}

	/**
	 * @return Modal dialog without autohiding.
	 */
	public static Dialog modalDialog() {
		Dialog dialog = dialog();
		dialog.setModal(true);
		dialog.setAutoHideEnabled(false);
		return dialog;
	}

	public static Dialog modalAutoHideDialog() {
		Dialog dialog = dialog();
		dialog.setModal(true);
		dialog.setAutoHideEnabled(true);
		return dialog;
	}

	/**
	 * @return Non-modal (modeless), non-autohide dialog.
	 */
	public static Dialog modelessDialog() {
		Dialog dialog = dialog();
		dialog.setModal(false);
		dialog.setAutoHideEnabled(false);
		return dialog;
	}

	public static Dialog dialog() {
		Dialog dialog = GWT.create(Dialog.class);
		dialog.setGlassEnabled(true);
		return dialog;
	}

	public static Dialog dialog(boolean autohide, boolean modal) {
		if (autohide == false && modal == false) {
			return modelessDialog();
		} else if (autohide == false && modal == true) {
			return modalDialog();
		} else if (autohide == true && modal == true) {
			return modalAutoHideDialog();
		}
		return dialog();
	}
	
	public static Dialog dialog(boolean autohide, boolean modal, boolean glass) {
		Dialog dialog = dialog(autohide, modal);
		dialog.setGlassEnabled(glass);
		return dialog;
	}

	public static DateBox dateBox() {
		return dateBox("dd.MM.yyyy");
	}

	public static DateBox dateBox(String dateTimeFormatPattern) {
		DateBox dateBox = GWT.create(DateBox.class);
		DateBox.DefaultFormat dateBoxFormat = new DateBox.DefaultFormat(DateTimeFormat
				.getFormat(dateTimeFormatPattern));
		dateBox.setFormat(dateBoxFormat);
		return dateBox;
	}

	public static ListBox listBox(int visibleItemCount) {
		ListBox listBox = GWT.create(ListBox.class);
		listBox.setVisibleItemCount(visibleItemCount);
		return listBox;
	}

	public static CheckBox checkBox() {
		return GWT.create(CheckBox.class);
	}
	
	public static CheckBox checkBox(String text) {
		CheckBox checkBox = checkBox();
		checkBox.setText(text);
		return checkBox;
	}
	
	public static CheckBox checkBox(ValueChangeHandler<Boolean> valueChangeHandler) {
		CheckBox checkBox = checkBox();
		checkBox.addValueChangeHandler(valueChangeHandler);
		return checkBox;
	}
	
	public static CheckBox checkBox(String text, ValueChangeHandler<Boolean> valueChangeHandler) {
		CheckBox checkBox = checkBox(text);
		checkBox.addValueChangeHandler(valueChangeHandler);
		return checkBox;
	}
	

	/**
	 * @param name
	 * @return
	 */
	public static RadioButton radioButton(String name) {
		return new RadioButton(name);
	}

	/**
	 * @param name
	 * @param label
	 * @return
	 */
	public static RadioButton radioButton(String name, String label) {
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
	public static RadioButton radioButton(String name, String label, ValueChangeHandler<Boolean> valueChangeHandler) {
		RadioButton radioButton = radioButton(name, label);
		radioButton.addValueChangeHandler(valueChangeHandler);
		return radioButton;
	}
	
	/**
	 * @param name
	 * @param valueChangeHandler
	 * @return
	 */
	public static RadioButton radioButton(String name, ValueChangeHandler<Boolean> valueChangeHandler) {
		RadioButton radioButton = radioButton(name);
		radioButton.addValueChangeHandler(valueChangeHandler);
		return radioButton;
	}

//	/**
//	 * @param value
//	 *            initial value
//	 * @param min
//	 *            min value
//	 * @param max
//	 *            max value
//	 */
//	public static ValueSpinner valueSpinner(long value, int min, int max) {
//		return new ValueSpinner(value, min, max);
//	}

	
	
	
	
	
	/**
	 * @param formHolder
	 */
	public static void showAndCenter(final FormHolder formHolder) {
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
	public static void showAndCenter(final Dialog dialog) {
		dialog.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = ((Window.getClientWidth() - offsetWidth) / 2) >> 0;
				int top = ((Window.getClientHeight() - offsetHeight) / 2) >> 0;
				dialog.setPopupPosition(left, top);
			}
		});
	}
	
	/**
	 * @param dialog
	 */
	public static void show(final Dialog dialog, final Integer leftVerticalCenterOffset, final Integer topOffset) {
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

	public static TextArea textArea(String style) {
		TextArea textArea = GWT.create(TextArea.class);
		textArea.setStyleName(style);
		return textArea;
	}
	
}
