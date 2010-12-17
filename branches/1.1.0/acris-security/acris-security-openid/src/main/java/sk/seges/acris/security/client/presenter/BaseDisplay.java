package sk.seges.acris.security.client.presenter;

import com.google.gwt.user.client.ui.Widget;

public interface BaseDisplay {

	/**
	 * Returns this display as a widget.
	 * @return
	 */
	Widget asWidget();
	
	/**
	 * Hides the display and displays a message instead.
	 * @param message
	 */
	void displayMessage(String message);

	/**
	 * Shows a message in a popup window.
	 * @param message
	 */
	void showMessage(String message);
}
