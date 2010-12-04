package sk.seges.acris.recorder.client.listener;

import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;

public interface PlaylistListener {
	public void eventStarted(AbstractGenericEvent event);
	public void eventFinished(AbstractGenericEvent event);
}
