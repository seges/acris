package sk.seges.acris.recorder.client.listener;

import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public interface PlaylistListener {
	void eventStarted(AbstractGenericEvent event);
	void eventFinished(AbstractGenericEvent event);
}
