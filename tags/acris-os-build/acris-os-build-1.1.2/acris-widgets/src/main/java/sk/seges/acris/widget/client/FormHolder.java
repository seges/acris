package sk.seges.acris.widget.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public interface FormHolder {
	
	Widget asWidget();
	
	void setCaption(String caption);
	void setCaptionWidget(Widget caption);
	void setContent(Widget content);
	void reinitialize();
	
	void addOption(Widget option);
	void addOption(Widget option, ClickHandler hidingClickHandler);
	void addOptionWithoutHiding(Widget option);
	void addOptions(Widget options[]);
	void addOptions(Widget options[], ClickHandler hidingClickHandler);
	void addOptionSeparator();
	
	void show();
	void hide();
	void center();
	
	boolean isShowing();
	void setPopupPositionAndShow(PositionCallback callback);
	void setPopupPosition(int left, int top);
}
