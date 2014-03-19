package sk.seges.acris.recorder.client.listener;

import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public interface RecorderListener {
	public void eventRecorded(AbstractGenericEvent event);
}
