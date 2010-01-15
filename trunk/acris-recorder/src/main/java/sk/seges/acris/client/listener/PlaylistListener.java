package sk.seges.acris.client.listener;

import sk.seges.acris.rpc.event.generic.AbstractGenericEvent;

public interface PlaylistListener {
	public void eventStarted(AbstractGenericEvent event);
	public void eventFinished(AbstractGenericEvent event);
}
