package sk.seges.acris.player.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import sk.seges.acris.player.client.configuration.PresenterProvider;
import sk.seges.acris.player.client.players.TutorialPlayer;
import sk.seges.acris.player.client.players.view.ControlPanelStyle;
import sk.seges.acris.player.client.players.view.ResourceBundle;
import sk.seges.acris.player.client.session.SessionPlayer;
import sk.seges.acris.player.client.session.SessionPresenter;
import sk.seges.acris.player.client.sessions.SessionsPresenter;
import sk.seges.acris.player.client.sessions.SessionsView;
import sk.seges.acris.player.shared.service.IPlayerRemoteService;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.player.shared.service.ServicesDefinition;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;
import sk.seges.acris.recorder.client.tools.CacheMap;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;

import javax.print.Doc;

public class PlayerAction implements EntryPoint {

	public static final String SESSION_PARAMETER = "session";
	public static final String SESSIONS_PARAMETER = "sessions";

	public void onModuleLoad() {

		if (Window.Location.getParameter(SESSIONS_PARAMETER) != null) {
            //shows available sessions
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
							return new SessionPresenter(new TutorialPlayer(true, cacheMap), playerService, cacheMap);
						}
					}, new SessionsView(), playerService);

                    ResourceBundle.INSTANCE.css().ensureInjected();

					SimplePanel panel = new SimplePanel();
                    panel.setStyleName(ResourceBundle.INSTANCE.css().sessionHolder());
					sessionsPresenter.start(panel, new SimpleEventBus());

					RootPanel.get().add(panel);
				}
			});
		} else if (Window.Location.getParameter(SESSION_PARAMETER) != null) {
            //play session log
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onFailure(Throwable reason) {}

				@Override
				public void onSuccess() {
                    ResourceBundle.INSTANCE.css().ensureInjected();
                    new SessionPlayer().loadSession(Long.parseLong(Window.Location.getParameter(SESSION_PARAMETER)));
				}
			});
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