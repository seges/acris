package sk.seges.acris.player.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import sk.seges.acris.player.client.configuration.PresenterProvider;
import sk.seges.acris.player.client.players.TutorialPlayer;
import sk.seges.acris.player.client.session.SessionPresenter;
import sk.seges.acris.player.client.sessions.SessionsPresenter;
import sk.seges.acris.player.client.sessions.SessionsView;
import sk.seges.acris.player.shared.service.IPlayerRemoteService;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.player.shared.service.ServicesDefinition;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;
import sk.seges.acris.recorder.client.tools.CacheMap;
import sk.seges.acris.recorder.shared.model.dto.RecordingLogDTO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;

public class PlayerAction implements EntryPoint {

	public static final String SESSION_PARAMETER = "session";
	public static final String SESSIONS_PARAMETER = "sessions";

	public void onModuleLoad() {

		if (Window.Location.getParameter(SESSIONS_PARAMETER) != null) {

			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onFailure(Throwable reason) {}

				@Override
				public void onSuccess() {

					final IPlayerRemoteServiceAsync playerService = GWT.create(IPlayerRemoteService.class);
					ServiceDefTarget playerServiceEndPoint = (ServiceDefTarget) playerService;
					playerServiceEndPoint.setServiceEntryPoint(ServicesDefinition.PLAYER_SERVICE);

					final CacheMap cacheMap = new CacheMap(50);

					SessionsPresenter sessionsPresenter = new SessionsPresenter(new PresenterProvider<SessionPresenter>() {
						@Override
						public SessionPresenter getPresenter() {
							return new SessionPresenter(new TutorialPlayer(cacheMap), playerService, cacheMap);
						}
					}, new SessionsView(), playerService);

					SimplePanel panel = new SimplePanel();
					sessionsPresenter.start(panel, new SimpleEventBus());

					RootPanel.get().add(panel);
					//show sessions
				}
			});
		} else if (Window.Location.getParameter(SESSION_PARAMETER) != null) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onFailure(Throwable reason) {}

				@Override
				public void onSuccess() {

					final CacheMap cacheMap = new CacheMap(50);

					final IPlayerRemoteServiceAsync playerService = GWT.create(IPlayerRemoteService.class);
					ServiceDefTarget playerServiceEndPoint = (ServiceDefTarget) playerService;
					playerServiceEndPoint.setServiceEntryPoint(ServicesDefinition.PLAYER_SERVICE);

					playerService.getSession(Long.parseLong(Window.Location.getParameter(SESSION_PARAMETER)), new AsyncCallback<RecordingSessionDTO>() {
						@Override
						public void onFailure(Throwable caught) {
							throw new RuntimeException(caught);
						}

						@Override
						public void onSuccess(RecordingSessionDTO result) {
							SessionPresenter presenter = new SessionPresenter(new TutorialPlayer(cacheMap), playerService, cacheMap);
                            SimplePanel panel = new SimplePanel();
							presenter.start(panel, new SimpleEventBus());
							presenter.initialize(result);

							RootPanel.get().add(panel);
						}
					});
				}
			});
			//play session log
		} else {
			//record
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onFailure(Throwable reason) {}

				@Override
				public void onSuccess() {
					new BatchRecorder(new CacheMap(50)).startRecording();
				}
			});
		}
	}
}
