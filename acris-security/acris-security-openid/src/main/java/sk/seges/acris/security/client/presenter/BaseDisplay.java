package sk.seges.acris.security.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;

public interface BaseDisplay extends IsWidget {

	void setHeight(String height);

	void setWidth(String width);

	void addStyleName(String className);

	void setStyleName(String className);

	/**
	 * Hides the display and displays a message instead.
	 * 
	 * @param message
	 */
	void displayMessage(String message);

	/**
	 * Shows a message in a popup window.
	 * 
	 * @param message
	 */
	void showMessage(String message);
}
