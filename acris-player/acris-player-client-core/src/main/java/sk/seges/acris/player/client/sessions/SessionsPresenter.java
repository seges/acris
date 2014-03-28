package sk.seges.acris.player.client.sessions;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import sk.seges.acris.player.client.configuration.PresenterProvider;
import sk.seges.acris.player.client.event.PlayEvent;
import sk.seges.acris.player.client.event.decoding.EventDecoder;
import sk.seges.acris.player.client.players.Player;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.player.client.session.SessionPresenter;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.client.recorder.support.Recorder;
import sk.seges.acris.recorder.client.session.RecordingSessionDetailParamsJSO;
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
	private final PresenterProvider<SessionPresenter> sessionPresenterProvider;

	private AcceptsOneWidget parentPanel;

	public SessionsPresenter(PresenterProvider<SessionPresenter> sessionPresenterProvider, SessionsDisplay display, IPlayerRemoteServiceAsync playerService) {
		this.display = display;
		this.sessionPresenterProvider = sessionPresenterProvider;
		this.playerService = playerService;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		this.parentPanel = panel;

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
	public void onPlay(final PlayEvent event) {
		SessionPresenter sessionPresenter = sessionPresenterProvider.getPresenter();
		sessionPresenter.start(parentPanel, null);
		sessionPresenter.initialize(event.getRecordingSession());
	}
}