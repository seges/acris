package sk.seges.acris.recorder.client.listener;

import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;

public interface RecorderListener {
	public void eventRecorded(AbstractGenericEvent event);
}
