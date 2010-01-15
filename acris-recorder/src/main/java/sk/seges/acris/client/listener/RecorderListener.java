package sk.seges.acris.client.listener;

import sk.seges.acris.rpc.event.generic.AbstractGenericEvent;

public interface RecorderListener {
	public void eventRecorded(AbstractGenericEvent event);
}
