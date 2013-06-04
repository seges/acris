package sk.seges.acris.widget.client.event;

import sk.seges.acris.widget.client.handler.DialogInitializeHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DialogInitializeEvent extends GwtEvent<DialogInitializeHandler> {

	private static Type<DialogInitializeHandler> TYPE = new Type<DialogInitializeHandler>();
	
	public static void fire(HasHandlers source) {
		DialogInitializeEvent event = new DialogInitializeEvent();
		source.fireEvent(event);
	}
	
	public static Type<DialogInitializeHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<DialogInitializeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DialogInitializeHandler handler) {
		handler.onInitialize(this);
	}
}
