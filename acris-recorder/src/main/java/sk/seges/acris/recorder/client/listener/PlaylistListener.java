package sk.seges.acris.recorder.client.listener;

import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;

public interface PlaylistListener {
	void eventStarted(AbstractGenericEvent event);
	void eventFinished(AbstractGenericEvent event);
}
