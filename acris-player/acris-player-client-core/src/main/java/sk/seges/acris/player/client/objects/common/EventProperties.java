package sk.seges.acris.player.client.objects.common;

import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;


public class EventProperties {

	private EventMirror eventMirror;
	private AbstractGenericEvent event;

	public AbstractGenericEvent getEvent() {
		return event;
	}

	public void setEvent(AbstractGenericEvent event) {
		this.event = event;
	}

	public EventProperties(EventMirror eventMirror) {
		this.eventMirror = eventMirror;
	}
	
	public EventMirror getEventMirror() {
		return eventMirror;
	}
}