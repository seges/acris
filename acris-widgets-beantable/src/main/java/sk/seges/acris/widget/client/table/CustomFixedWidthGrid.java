package sk.seges.acris.widget.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * extended FixedWidthGrid, which is used in BeanTable, necessary for double-click handler
 * @author marta
 *
 */

@SuppressWarnings("unchecked")
public class CustomFixedWidthGrid extends FixedWidthGrid implements HasDoubleClickHandlers {

	public CustomFixedWidthGrid() {
		super();
		sinkEvents(Event.ONDBLCLICK);
	}

	public CustomFixedWidthGrid(int rows, int columns) {
		super(rows, columns);
		sinkEvents(Event.ONDBLCLICK);
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return super.addDomHandler(handler, DoubleClickEvent.getType());
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		case Event.ONDBLCLICK:
			DoubleClickEvent.fireNativeEvent(event, this);
		}
	}
}
