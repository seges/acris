package sk.seges.acris.recorder.rpc.event.handler;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.recorder.rpc.listener.EventListener;

import com.google.gwt.event.dom.client.ClickHandler;

public abstract class StateClickHandler implements ClickHandler {
	private List<EventListener> eventListeners = new ArrayList<EventListener>();
	
	public void addEventListener(EventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public void removeEventListener(EventListener eventListener) {
		eventListeners.remove(eventListener);
	}

	public void onFailure() {
		for (EventListener eventListener : eventListeners) {
			eventListener.onFailure();
		}
	}
	
	public void onSuccess() {
		for (EventListener eventListener : eventListeners) {
			eventListener.onSuccess();
		}
	};
}
