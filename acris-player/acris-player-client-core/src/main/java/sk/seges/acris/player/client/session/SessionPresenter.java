package sk.seges.acris.player.client.session;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import sk.seges.acris.player.client.event.decoding.EventsDecoder;
import sk.seges.acris.player.client.players.Player;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;
import sk.seges.acris.recorder.shared.model.dto.RecordingLogDTO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class SessionPresenter extends AbstractActivity {

	private final Player player;
	private final IPlayerRemoteServiceAsync playerService;
    private final CacheMap cacheMap;

	public SessionPresenter(Player player, IPlayerRemoteServiceAsync playerService, CacheMap cacheMap) {
		this.player = player;
		this.playerService = playerService;
        this.cacheMap = cacheMap;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {}

	public void initialize(final RecordingSessionDTO recordingSessionDTO) {

        playerService.getLogs(new PageDTO(0, 0), recordingSessionDTO, new AsyncCallback<PagedResultDTO<List<RecordingLogDTO>>>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(PagedResultDTO<List<RecordingLogDTO>> result) {
				Playlist playlist = new Playlist();

                EventsDecoder eventsDecoder = new EventsDecoder(playerService, recordingSessionDTO.getId(), cacheMap);

                for (RecordingLogDTO recordingLog: result.getResult()) {
					String encodedEvents = recordingLog.getEvent();

                    try {
						List<AbstractGenericEvent> abstractGenericEvents = eventsDecoder.decodeEvents(encodedEvents);

						for (AbstractGenericEvent event: abstractGenericEvents) {
							playlist.addEvent(event);
						}

					} catch (UnsupportedEncodingException e) {
						GWT.log("Unable to decode event", e);
					}
				}

				//player.showPlaylist();
				player.setPlaylist(playlist);
                player.showControlPanel(RootPanel.get());
			}
		});
	}
}