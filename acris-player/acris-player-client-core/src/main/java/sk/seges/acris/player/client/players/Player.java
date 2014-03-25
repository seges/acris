package sk.seges.acris.player.client.players;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import sk.seges.acris.player.client.objects.Cursor;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.player.client.objects.Cursor;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class Player {

	private final Cursor cursor;
//	private final Tooltip tooltip;
	private Playlist playlist;

	
	public Player(int duration, CacheMap cacheMap) {
		cursor = new Cursor(duration, cacheMap);
//		tooltip = new Tooltip(200);
//		cursor.synchronizeWith(tooltip);
	}

	public boolean isPlaying() {
		return (playlist != null && this.playlist.getEventsCount() > 0);
	}

//	private static final int MINIMUM_DISTANCE = 10; //in pixels

	private boolean showPlaylist = false;
	
	public void showPlaylist() {
		if (isPlaying()) {
			DialogBox playlist = GWT.create(DialogBox.class);
			playlist.setModal(false);
			playlist.setWidth("100px");
			playlist.setPopupPosition(1000, 20);
			ListBox playListList = GWT.create(ListBox.class);
			playListList.setVisibleItemCount(40);
			
			int i = 0;
			int count = this.playlist.getEventsCount();
			
			for (int j = 0; j < count; j++) {
				AbstractGenericEvent event = this.playlist.getEvent(j);
//		    	if (count - 1 > i) {
//		    		event.setSelected(++i, playListList);
//		    	}
		    	playListList.addItem(event.toString(true, false));
		    }
			playlist.setText("Playlist");
			playlist.setWidget(playListList);
			playlist.show();
		} else {
			showPlaylist = true;
		}
	}
	
	public void play(Playlist playlist) {
		this.playlist = playlist;
		if (showPlaylist) {
			showPlaylist();
		}
		RootPanel.get().add(cursor);
		
		int count = playlist.getEventsCount();

//		MouseEvent previousMouseEvent = null;
		
		//CacheMap cacheMap = new CacheMap(50);
		
		for (int i = 0; i < count; i++) {
			AbstractGenericEvent event = playlist.getEvent(i);
			//event.setCacheMap(cacheMap);
//			if (previousMouseEvent != null && 
//					event.getType().equals(MouseMoveEvent.getType().getName())) {
//				double distance = AnimationObject.calculateDistance(((MouseEvent)event).getClientX(), ((MouseEvent)event).getClientY(), 
//						previousMouseEvent.getClientX(), previousMouseEvent.getClientY());
//				if (distance < MINIMUM_DISTANCE) {
//					event.skipEvent();
//					continue;
//				}
//			} else if (previousMouseEvent != null) {
//				previousMouseEvent = null;
//			}

			if (event instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent)event;
				cursor.move(mouseEvent.getAbsoluteClientX(), mouseEvent.getAbsoluteClientY());
				cursor.event(mouseEvent);
			}
			
//			if (event.getType().equals(MouseMoveEvent.getType().getName())) {
//				previousMouseEvent = (MouseEvent)event;
//			}
		}
		
//		for (int i = 0; i < count; i++) {
//			String event = playlist.getEvent(i);
//			if (EObjectType.CURSOR.equals(getObjectType(event))) {
//				EObjectActionType  getObjectActionType
//				String params = getActionParams(event);
//				
//				if (params != null) {
//					if (params.indexOf(",") != -1) {
//						Position position = new Position(params);
//					} else {
//						
//					}
//				}
//				//cursor.
//			} else {
//				//tootip
//			}
//		}
	}

	public void stop() {
		cursor.stop();
		// tooltip.stop();
	}

	public void replay() {
		play(playlist);
	}
}
