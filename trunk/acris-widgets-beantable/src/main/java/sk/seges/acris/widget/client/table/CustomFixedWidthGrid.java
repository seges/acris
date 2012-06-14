package sk.seges.acris.widget.client.table;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * extended FixedWidthGrid, which is used in BeanTable, necessary for
 * double-click handler
 * 
 * @author marta
 * 
 */

@SuppressWarnings("unchecked")
public class CustomFixedWidthGrid extends FixedWidthGrid implements HasDoubleClickHandlers, HasClickHandlers {

	public CustomFixedWidthGrid() {
		super();
		sinkEvents(Event.ONDBLCLICK);
		sinkEvents(Event.ONCLICK);
	}

	public CustomFixedWidthGrid(int rows, int columns) {
		super(rows, columns);
		sinkEvents(Event.ONDBLCLICK);
		sinkEvents(Event.ONCLICK);
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return super.addDomHandler(handler, DoubleClickEvent.getType());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return super.addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		case Event.ONDBLCLICK:
			DoubleClickEvent.fireNativeEvent(event, this);
			break;
		case Event.ONCLICK:
			ClickEvent.fireNativeEvent(event, this);
			break;
		}
	}

	@Override
	public void setHTML(int row, int column, String html) {
		super.setHTML(row, column, SafeHtmlUtils.htmlEscape(html));
		clearIdealWidths();
	}
}
