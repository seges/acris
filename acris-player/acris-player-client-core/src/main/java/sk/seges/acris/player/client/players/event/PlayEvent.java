package sk.seges.acris.player.client.players.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;

public class PlayEvent extends GwtEvent<PlayEvent.PlayHandler> {

	private static final GwtEvent.Type<PlayHandler> TYPE = new GwtEvent.Type<PlayHandler>();

	private RecordingSessionDTO recordingSession;

	protected PlayEvent() {}

	public PlayEvent(RecordingSessionDTO recordingSession) {
		this.recordingSession = recordingSession;
	}

	public static void fire(HasHandlers source, RecordingSessionDTO recordingSession) {
		source.fireEvent(new PlayEvent(recordingSession));
	}

	public static GwtEvent.Type<PlayHandler> getType() {
		return TYPE;
	}

	@Override
	public GwtEvent.Type<PlayHandler> getAssociatedType() {
		return TYPE;
	}

	public RecordingSessionDTO getRecordingSession() {
		return recordingSession;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlayEvent)) return false;

		PlayEvent playEvent = (PlayEvent) o;

		if (recordingSession != null ? !recordingSession.equals(playEvent.recordingSession) : playEvent.recordingSession != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return recordingSession != null ? recordingSession.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "PlayEvent{recordingSession=" + recordingSession + "}";
	}

	@Override
	protected void dispatch(PlayHandler handler) {
		handler.onPlay(this);
	}

	public interface HasPlayHandlers extends HasHandlers {
		HandlerRegistration addPlayHandler(PlayHandler handler);
	}

	public interface PlayHandler extends EventHandler {
		void onPlay(PlayEvent event);
	}


}
