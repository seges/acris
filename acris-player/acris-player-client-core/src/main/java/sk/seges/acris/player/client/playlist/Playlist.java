package sk.seges.acris.player.client.playlist;

import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

import java.util.LinkedList;
import java.util.List;

public class Playlist {

	private List<AbstractGenericEvent> listEvent = new LinkedList<AbstractGenericEvent>();

	public void addEvent(AbstractGenericEvent e) {
		listEvent.add(e);
	}

	public int getEventsCount() {
		return listEvent.size();
	}
	
	public AbstractGenericEvent getEvent(int i) {
		return listEvent.get(i);
	}
	
	public void clear() {
		listEvent.clear();
	}
}