package sk.seges.acris.player.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class PlaylistEvent extends GwtEvent<PlaylistEvent.PlaylistEventHandler> {

	private static final Type<PlaylistEventHandler> TYPE = new Type<PlaylistEventHandler>();

	private AbstractGenericEvent event;

	public PlaylistEvent(AbstractGenericEvent event) {
		this.event = event;
	}

	public static void fire(HasHandlers source, AbstractGenericEvent event) {
		source.fireEvent(new PlaylistEvent(event));
	}

	public static Type<PlaylistEventHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<PlaylistEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return "PlaylistEvent{}";
	}

	@Override
	protected void dispatch(PlaylistEventHandler handler) {
		handler.onPlay(this);
	}

	public interface HasPlaylistEventHandlers extends HasHandlers {
		HandlerRegistration addPlaylistEventHandler(PlaylistEventHandler handler);
	}

	public interface PlaylistEventHandler extends EventHandler {
		void onPlay(PlaylistEvent event);
	}
}