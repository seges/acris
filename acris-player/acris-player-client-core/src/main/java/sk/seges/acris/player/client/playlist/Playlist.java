package sk.seges.acris.player.client.playlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import sk.seges.acris.player.client.players.Layers;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

import java.util.LinkedList;
import java.util.List;

public class Playlist {

	private List<AbstractGenericEvent> listEvent = new LinkedList<AbstractGenericEvent>();
	private ListBox playListBox;

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

	public void selectNextEvent() {

	}

	public void show() {
		DialogBox playlist = GWT.create(DialogBox.class);
		DOM.setStyleAttribute(playlist.getElement(), "zIndex", Layers.PLAYLIST_POSITION);
		playlist.setModal(false);
		playListBox = GWT.create(ListBox.class);
		playListBox.setVisibleItemCount(40);

		int i = 0;
		int count = getEventsCount();

		for (int j = 0; j < count; j++) {
			AbstractGenericEvent event = getEvent(j);
			playListBox.addItem(event.toString(true, false));
		}
		playListBox.setSelectedIndex(0);
		playlist.setText("Playlist");
		playlist.setWidget(playListBox);
		playlist.show();
		playlist.setPopupPosition(Window.getClientWidth() - playlist.getOffsetWidth(), 20);
	}
}