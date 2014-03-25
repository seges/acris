package sk.seges.acris.player.client.view;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import sk.seges.acris.player.client.event.PlayEvent;
import sk.seges.acris.player.client.event.decoding.EventDecoder;
import sk.seges.acris.player.client.players.Player;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.client.recorder.support.Recorder;
import sk.seges.acris.recorder.shared.model.dto.RecordingLogDTO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SessionsPresenter extends AbstractActivity implements PlayEvent.PlayHandler {

	public interface SessionsDisplay extends IsWidget, PlayEvent.HasPlayHandlers {

		void setData(List<RecordingSessionDTO> sessions);
	}

	private final SessionsDisplay display;
	private final IPlayerRemoteServiceAsync playerService;
	private final Player player;

	public SessionsPresenter(Player player, SessionsDisplay display, IPlayerRemoteServiceAsync playerService) {
		this.display = display;
	    this.player = player;
		this.playerService = playerService;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(display);

		PageDTO page = new PageDTO(0, 20);
		playerService.getSessions(page, null, null, new AsyncCallback<PagedResultDTO<List<RecordingSessionDTO>>>() {
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(PagedResultDTO<List<RecordingSessionDTO>> result) {
				display.setData(result.getResult());
			}
		});

		//TODO deregister somewhere
		display.addPlayHandler(this);
	}

	@Override
	public void onPlay(PlayEvent event) {
		playerService.getLogs(new PageDTO(0, 0), event.getRecordingSession(), new AsyncCallback<PagedResultDTO<List<RecordingLogDTO>>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(PagedResultDTO<List<RecordingLogDTO>> result) {
				Playlist playlist = new Playlist();

				for (RecordingLogDTO recordingLog: result.getResult()) {
					String encodedEvents = recordingLog.getEvent();

					try {
						List<AbstractGenericEvent> abstractGenericEvents = decodeEvents(encodedEvents);

						for (AbstractGenericEvent event: abstractGenericEvents) {
							playlist.addEvent(event);
						}

					} catch (UnsupportedEncodingException e) {
						GWT.log("Unable to decode event" , e);
					}
				}

				player.play(playlist);
			}
		});
	}

	private List<AbstractGenericEvent> decodeEvents(String encodedEvents) throws UnsupportedEncodingException {
		String[] eventParts = encodedEvents.split(Recorder.DELIMITER);

		List<AbstractGenericEvent> result = new ArrayList<AbstractGenericEvent>();

		for (int i = 0; i < eventParts.length / 2; i++) {
			byte[] bytes = eventParts[i * 2].getBytes("ISO-LATIN-1");
			String targetXpath = eventParts[i * 2 + 1];

			AbstractGenericEvent abstractGenericEvent = EventDecoder.decodeEvent(bytes);

			if (abstractGenericEvent != null && abstractGenericEvent instanceof AbstractGenericTargetableEvent) {
				((AbstractGenericTargetableEvent)abstractGenericEvent).setRelatedTargetXpath(targetXpath);
			}

			result.add(abstractGenericEvent);
		}

		return result;
	}
}