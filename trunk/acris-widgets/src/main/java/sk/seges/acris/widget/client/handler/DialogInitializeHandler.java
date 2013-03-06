package sk.seges.acris.widget.client.handler;

import sk.seges.acris.widget.client.event.DialogInitializeEvent;

import com.google.gwt.event.shared.EventHandler;

public interface DialogInitializeHandler extends EventHandler {

	void onInitialize(DialogInitializeEvent event);
}
